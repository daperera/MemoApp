package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import processing.Config;

class QueryHandler {
	
	
	
	
	/*
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
