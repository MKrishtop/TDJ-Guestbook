package mkrishtop.tests;

import mkrishtop.database.DerbyDatabaseConnector;
import mkrishtop.database.DerbyDatabaseManager;
import mkrishtop.database.PropertiesReader;
import mkrishtop.exception.ConnectionTableException;

public class TestDBConnector {
	public static void main(String[] args) {
		System.out.println("Testing db connect.");
		
		PropertiesReader properties = new PropertiesReader("config.properties");
		
		DerbyDatabaseManager databaseManager = DerbyDatabaseManager.getInstance();
		databaseManager.connect(properties);
		
		try {
		DerbyDatabaseConnector dbConnect = DerbyDatabaseConnector.getInstance();
		dbConnect.setConnection(databaseManager.getConnection());
		
		dbConnect.connect("users", "records", 1, 1);
//		dbConnect.connect("users", "records", 1, 1);
		dbConnect.connect("users", "books", 3, 2);
		dbConnect.connect("users", "books", 5, 7);
		dbConnect.connect("records", "books", 2, 1);
		
		dbConnect.setAccessLevel("books", "users", 2, 3, 1);
		dbConnect.setAccessLevel("books", "users", 7, 5, 2);
		
		System.out.println("true = " + dbConnect.isConnected("users", "records", 1, 1));
		System.out.println("false = " + dbConnect.isConnected("users", "records", 1, 2));
		System.out.println("false = " + dbConnect.isConnected("users1", "records", 1, 1));
		System.out.println("true = " + dbConnect.isConnected("records", "users", 1, 1));
		System.out.println("true = " + dbConnect.isConnected("books", "records", 1, 2));
		System.out.println("true = " + dbConnect.isConnected("books", "users", 2, 3));
		System.out.println("true = " + dbConnect.isConnected("users", "books", 5, 7));
		
		System.out.println("1 = " + dbConnect.getAccessLevel("books", "users", 2, 3));
		System.out.println("2 = " + dbConnect.getAccessLevel("books", "users", 7, 5));
		System.out.println("0 = " + dbConnect.getAccessLevel("books", "users", 1, 3));
		
		dbConnect.disconnect("users", "records", 1, 1);
//		dbConnect.disconnect("users", "records", 1, 1);
		dbConnect.disconnect("users", "books", 3, 2);
		dbConnect.disconnect("users", "books", 5, 7);
		dbConnect.disconnect("records", "books", 2, 1);
		
		} catch (ConnectionTableException ex) {
			ex.printStackTrace();
		}
		
		databaseManager.disconnect();
		
		System.out.println("All tests are done.");
	}
}
