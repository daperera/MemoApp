package database;

public class Database {
	
	private static volatile Database instance = null;
	
	private Database() {
		
	}
	
	public final static void query(String sqlRequest) {
		// check that an instance has been created
		if(instance == null) {
			synchronized(Database.class) {
				if(instance == null) {
					Database.instance = new Database();
				}
			}
		}
		System.out.println("query processed : " + sqlRequest);
	}
	
	public final static void synchronize() {
		// check that an instance has been created
		if(instance == null) {
			synchronized(Database.class) {
				if(instance == null) {
					Database.instance = new Database();
				}
			}
		}
		System.out.println("database synchronized");
	}
	
}
