package gui.java.service;

import database.Database;

public class DatabaseProxy {
	
	public void query(String sqlRequest) {
		Database.query(sqlRequest);
	}
	
}
