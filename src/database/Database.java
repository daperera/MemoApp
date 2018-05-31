package database;

import java.util.ArrayList;
import java.util.HashMap;

public final class Database {
	
	

	/**
	 * function called by the Database object when a part of the application wants to query the database
	 * 
	 * @param sqlQuery : 	The query to be processed by the database
	 * 
	 * @return 	this function returns a QueryResult object allowing the querier to browse the result from 
	 * 			his query. 
	 */
	public static QueryResult query(String sqlQuery) {
		return QueryHandler.query(sqlQuery);
	}
	
	/**
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
	public static boolean alterTable(String oldTableName, 
							String newTableName, 
							ArrayList<String> newColumnLabel, 
							HashMap<String, String> oldToNewColumnLabelMapping) {
		return MetaDataHandler.alterTable(oldTableName, newTableName, newColumnLabel, oldToNewColumnLabelMapping);
	}
	
	public static ArrayList<String> getTablesNames() {
		return MetaDataHandler.getTablesNames();
	}
	
	public static boolean addTable(String name) {
		return MetaDataHandler.addTable(name);
	}
	
	public static boolean renameTable(String oldName, String newName) {
		return MetaDataHandler.renameTable(oldName, newName);
	}
	
	public static boolean deleteTable(String name) {
		return MetaDataHandler.deleteTable(name);
	}
	
	public static void synchronize() {
		System.out.println("database synchronized");
	}

	public static void deleteColumn(String tableName, String column) {
		MetaDataHandler.deleteColumn(tableName, column);
	}

	public static ArrayList<String> getColumnsNames(String tableName) {
		return MetaDataHandler.getColumnsNames(tableName);
	}

	public static void renameColumn(String tableName, String oldColumnName, String newColumnName) {
		MetaDataHandler.renameColumn(tableName, oldColumnName, newColumnName);
	}

	public static void addColumn(String tableName, String columnName) {
		MetaDataHandler.addColumn(tableName, columnName);
	}
	
	
}
