package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResult {
	private final ResultSet resultSet;
	
	public QueryResult(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	public String getById(int id) {
		String result = null;
		try {
			result = resultSet.getString(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getByLabel(String label) {
		String result = null;
		try {
			result = resultSet.getString(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean next()  {
		boolean result = false;
		try {
			result = resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
