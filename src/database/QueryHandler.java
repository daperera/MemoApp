package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import processing.Config;

class QueryHandler {
	
	/**
	 *  function called in order to alter one of the table of the database
	 *  
	 * @param oldTableName : 	the current name of the table to be modified
	 * 
	 * @param newTableName : 	the new name of this table. This parameter is required even if the name of the table
	 * 							is not modified.
	 * 
	 * @param newColumnLabel : 	The columns to be added to this table. These columns are not copied from the old table
	 * 							but created from scratch, and are left empty. When copying an already existing column,
	 * 							it is necessary to map its name using the oldToNewColumnLabelMapping variable
	 * 
	 * @param oldToNewColumnLabelMapping : 	Mapping of the name of the existing columns to their new name. The name
	 * 										of the column that are not dropped must be mapped to name, even if their
	 * 										name is not modified. When using this function to drop a column, it is 
	 * 										necessary not to add its label in this variable
	 */
	public void alterTable(	String oldTableName, 
							String newTableName, 
							ArrayList<String> newColumnLabel, 
							HashMap<String, String> oldToNewColumnLabelMapping) {
		
		/* 
		Here is format of the alter table query 
		
		BEGIN TRANSACTION;
		
		CREATE TABLE backup(c'_1, ..., c'_n) AS SELECT c_1, ..., c_n FROM oldTableName;
		ALTER TABLE backup ADD COLUMN c'_(n+1);
		...
		ALTER TABLE backup ADD COLUMN c'_(n+p);
		DROP TABLE oldTableName;
		ALTER TABLE backup RENAME TO newTableName;
		
		COMMIT;
		*/
		
		//defining convenience variables
		String newColumnLabelArg = computeColumnLabelArg(newColumnLabel);
		String oldColumnLabelArg = computeColumnLabelArg(oldToNewColumnLabelMapping.keySet());
		String addColumnsArg = computeAddColumnsArg(newColumnLabel);

		
		//constructing the query
		String sqlQuery = "BEGIN TRANSACTION;"
					+ "CREATE TABLE backup(" + newColumnLabelArg + ") AS SELECT " + oldColumnLabelArg + " FROM " + oldTableName + ";"
					+ addColumnsArg
					+ "DROP TABLE backup;"
					+ "ALTER TABLE backup RENAME TO " + newTableName + ";"
					+ "COMMIT;";
		
		//processing the query
		readWriteQuery(sqlQuery);
	}
	
	
	/**
	 * function called by the Database object when a part of the application wants to query the database
	 * 
	 * @param sqlQuery : 	The query to be processed by the database
	 * 
	 * @return 	this function returns a QueryResult object allowing the querier to browse the result from 
	 * 			his query. 
	 */
	public static QueryResult query(String sqlQuery) {
		String firstWord = sqlQuery.split(" ")[0];
		ResultSet result = null;
		
		//according to the type of query, either readOnlyQuery or readWriteQuery function is called
		switch(firstWord) {
			case "SELECT":
				result = readOnlyQuery(sqlQuery);
				break;
				
			case "UPDATE":
			case "INSERT":
			case "DELETE":
				readWriteQuery(sqlQuery);
				break;
				
			default:
				System.out.println("This type of query is not allowed : " + firstWord);
				break;
		}
		
		// null result in case of a write operation query
		return new QueryResult(result);
	}
	
	//called for query that does not modify the database
	private static ResultSet readOnlyQuery(String sqlQuery) {
		System.out.println("read only query called");
		ResultSet result = null;
		
		//computing the result of the query
		try {
			//connecting to database
			Connection connection = QueryHandler.connect();
			
			//processing query
			Statement statement = connection.createStatement();
			result = statement.executeQuery(sqlQuery);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return result;
	}
	
	//called for query that may modify the database
	private static void readWriteQuery(String sqlQuery) {
		System.out.println("read/write query called");
		try {
			
			//connecting to database
			Connection connection = QueryHandler.connect();
			
			//processing query
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	// this convenience function convert a collection of String to the string "element_1, element2, ... , element_n"
	private static String computeColumnLabelArg(Collection<String> columnLabels) {
		String sqlQueryArg = "";
		for(String columnLabel : columnLabels) {
			sqlQueryArg += columnLabel + ", ";
		}
		return sqlQueryArg.replaceAll(", $", "");
	}
	
	//this convetience function create the "ALTER TABLE backup ADD c'_(n+k);" part of the query
	private static String computeAddColumnsArg(Collection<String> columnLabels) {
		String sqlQueryArg = "";
		for(String columnLabel : columnLabels) {
			sqlQueryArg += "ALTER TABLE backup ADD COLUMN " + columnLabel + ";";
		}
		return sqlQueryArg;
	}
	
	//initiate a connection to the database
	private static Connection connect() {
		String url = "jdbc:sqlite:" + Config.getUserDirectoryLocation() + "/database";
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.getMessage();
		}
		
		return connection;
	}
	
}
