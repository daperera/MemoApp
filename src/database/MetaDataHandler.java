package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.dbutils.DbUtils;

import processing.Config;

class MetaDataHandler {

	private static final String DATABASE_NOT_FOUND_MESSAGE = "Fatal: database not found.";


	/*
	 *  function called in order to alter one of the table of the database.
	 *  
	 * @param oldTableName : 	the current name of the table to be modified.
	 * 
	 * @param newTableName : 	the new name of this table. This parameter is required even if the name of the table
	 * 							is not modified.
	 * 
	 * @param newColumnLabel : 	The columns to be added to this table. These columns are not copied from the old table
	 * 							but created from scratch, and are left empty. When copying an already existing column,
	 * 							it is necessary to map its name using the oldToNewColumnLabelMapping variable.
	 * 
	 * @param oldToNewColumnLabelMapping : 	Mapping of the name of the existing columns to their new name. The name
	 * 										of the column that are not dropped must be mapped to name, even if their
	 * 										name is not modified. When using this function to drop a column, it is 
	 * 										necessary not to add its label in this variable. This argument cannot be
	 * 										null or empty, since a table must possess at least one column.
	 */
	public static boolean alterTable(	String oldTableName, 
			String newTableName, 
			ArrayList<String> newColumnLabel, 
			HashMap<String, String> oldToNewColumnLabelMapping) {

		/* 
		Format of the alter table query 

		BEGIN TRANSACTION;

		CREATE TABLE backup(c'_1, ..., c'_n) AS SELECT c_1, ..., c_n FROM oldTableName;
		ALTER TABLE backup ADD COLUMN c'_(n+1);
		...
		ALTER TABLE backup ADD COLUMN c'_(n+p);
		DROP TABLE oldTableName;
		ALTER TABLE backup RENAME TO newTableName;

		COMMIT;
		 */
		
		//handling incorrect inputs
		if(oldTableName == null || oldTableName.equals("")) {
			return false;
		}
		if (newTableName == null){
			newTableName = oldTableName;
		}
		if(newTableName.equals("")) {
			return false;
		} 
		if(newColumnLabel == null) {
			newColumnLabel = new ArrayList<String>();
		}
		//we do not allow a table to have no columns
		if(oldToNewColumnLabelMapping == null || oldToNewColumnLabelMapping.isEmpty()) { 
			return false;
		}
		
		//defining convenience variables
		String oldColumnLabelArg = String.join(", ", oldToNewColumnLabelMapping.keySet());
		String newColumnLabelArg = "(" + String.join(", ", oldToNewColumnLabelMapping.values()) + ")"; //the case where oldToNewColumnLabelMapping.values() is empty has already been taken care of
		ArrayList<String> addColumnsQueries = constructAddColumnsQueries(newColumnLabel);
				


		//constructing the queries
		ArrayList<String> sqlQueries = new ArrayList<String>();
		sqlQueries.add("CREATE TABLE backup" + newColumnLabelArg + ";");
		sqlQueries.add("INSERT INTO backup" + newColumnLabelArg + " SELECT " + oldColumnLabelArg + " FROM " + oldTableName + ";");
		sqlQueries.addAll(addColumnsQueries);
		sqlQueries.add("DROP TABLE " + oldTableName + ";");
		sqlQueries.add("ALTER TABLE backup RENAME TO " + newTableName + ";");
		
		System.out.println("alter table queries: " + sqlQueries);
		
		//processing the query and returning
		return readWriteQuery(sqlQueries);
	}
	

	private static ArrayList<String> constructAddColumnsQueries(ArrayList<String> newColumnLabels) {
		ArrayList<String> queries = new ArrayList<String>();
		for(int i=0; i<newColumnLabels.size(); i++) {
			queries.add(i, "ALTER TABLE backup ADD COLUMN " + newColumnLabels.get(i) + ";");
		}
		return queries;
	}

	public static boolean addTable(String name) {
		//constructs the query
		String query = "CREATE TABLE IF NOT EXISTS " + name + "(DEFAULT_COL);";

		//process the query and returns
		return readWriteQuery(query);
	}

	public static boolean renameTable(String oldName, String newName) {
		//constructs the query
		String query = "ALTER TABLE " + oldName + " RENAME TO " + newName + ";";

		//process the query and returns
		return readWriteQuery(query);
	}

	public static boolean deleteTable(String name) {
		//constructs the query
		String query = "DROP TABLE IF EXISTS " + name + ";";

		//process the query and returns
		return readWriteQuery(query);
	}


	public static boolean addColumn(String tableName, String columnName) {
		//constructs the query
		String query = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + ";";

		//process the query and returns
		return readWriteQuery(query);
	}


	public static void deleteColumn(String tableName, String columnName) {
		ArrayList<String> columnNames = getColumnsNames(tableName);
		HashMap<String, String> columnNameMapping = new HashMap<String, String>();
		for(String columnNameB  : columnNames) {
			if(!columnName.equals(columnNameB)) {
				columnNameMapping.put(columnNameB, columnNameB);
			}
		}
		alterTable(tableName, tableName, null, columnNameMapping);
	}

	public static void renameColumn(String tableName, String oldColumnName, String newColumnName) {
		ArrayList<String> columnNames = getColumnsNames(tableName);
		HashMap<String, String> columnNameMapping = new HashMap<String, String>();
		for(String columnNameB  : columnNames) {
			if(columnNameB.equals(oldColumnName)) {
				columnNameMapping.put(oldColumnName, newColumnName);
			} else {
				columnNameMapping.put(columnNameB, columnNameB);
			}
		}
		alterTable(tableName, tableName, null, columnNameMapping);
	}

	public static ArrayList<String> getColumnsNames(String tableName) {
		ArrayList<String> columnsNames = new ArrayList<String>();

		//querying the table names
		try {
			//connecting to database
			Connection connection = connect();

			//processing query
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet resultSet = metadata.getColumns(null, null, tableName, null);



			//browsing results
			while (resultSet.next()) {
				columnsNames.add(resultSet.getString(4));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return columnsNames;
	}

	//return the database tables names
	public static ArrayList<String> getTablesNames() {
		ArrayList<String> tablesNames = new ArrayList<String>();

		//querying the table names
		try {
			//connecting to database
			Connection connection = connect();

			//processing query
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet resultSet = metadata.getTables(null, null, "%", new String[] {"TABLE"});



			//browsing results
			while (resultSet.next()) {
				tablesNames.add(resultSet.getString(3));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tablesNames;
	}

	//called for query that may modify the database
	private static boolean readWriteQuery(String sqlQuery) {
		System.out.println("read/write query called");
		boolean success = true;
		Connection connection = null;
		PreparedStatement statement = null;
		try {

			//connecting to database
			connection = connect();

			//processing query
			statement = connection.prepareStatement(sqlQuery);
			statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			success = false;
		} finally {
			DbUtils.closeQuietly(connection);
	        DbUtils.closeQuietly(statement);
		}
		return success;
	}
	
	//called for query that may modify the database
		private static boolean readWriteQuery(ArrayList<String> sqlQueries) {
			System.out.println("read/write query called");
			boolean success = true;
			Connection connection = null;
			Statement statement = null;
			try {

				//connecting to database
				connection = connect();

				//processing query
				statement = connection.createStatement();
				connection.setAutoCommit(false);
				for(String query : sqlQueries) {
					statement.executeUpdate(query);
				}
				connection.setAutoCommit(true);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				success = false;
			} finally {
				DbUtils.closeQuietly(connection);
		        DbUtils.closeQuietly(statement);
			}
			return success;
		}

	//initiate a connection to the database
	private static Connection connect() {
		String url = "jdbc:sqlite:" + Config.getUserDirectoryLocation() + "/database";
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(DATABASE_NOT_FOUND_MESSAGE);
		}

		return connection;
	}

}
