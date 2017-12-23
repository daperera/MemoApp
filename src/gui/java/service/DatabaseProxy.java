package gui.java.service;

import database.Database;
import database.QueryResult;

public class DatabaseProxy {
	
	public QueryResult query(String sqlRequest) {
		QueryResult result = Database.query(sqlRequest);
		return result;
	}
	
}
