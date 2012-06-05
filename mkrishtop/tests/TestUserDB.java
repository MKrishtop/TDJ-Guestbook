package mkrishtop.tests;

import java.util.HashMap;

import mkrishtop.database.DerbyDatabaseManager;
import mkrishtop.database.DerbyUserRepository;
import mkrishtop.database.PropertiesReader;
import mkrishtop.main.User;

public class TestUserDB {
	public static void main (String[] args) {
		System.out.println("Testing users db.");
		
		PropertiesReader properties = new PropertiesReader("config.properties");
		
		DerbyDatabaseManager databaseManager = DerbyDatabaseManager.getInstance();
		databaseManager.connect(properties);
		
		DerbyUserRepository userRepo = DerbyUserRepository.getInstance();
		userRepo.setConnection(databaseManager.getConnection());
		
		System.out.println("creation...");
		
		User superModerator = new User(properties.getLogin(), properties.getPassword());
		userRepo.add(superModerator);
		userRepo.add(new User("vasiya", "123"));
		userRepo.add(new User("koliya", "321"));
		
		for (User user : userRepo.fetchAll()) {
			System.out.println(user.getLogin());
		}
		
		System.out.println("changing...");
		
		HashMap<String, String> map_filter2 = new HashMap<String, String>();
		map_filter2.put("<s>login", "koliya");
		userRepo.delete(map_filter2);
		
		HashMap<String, String> map_filter1 = new HashMap<String, String>();
		map_filter1.put("<s>login", "vasiya");		
		
		User updatedUser = new User("vasiya1", "123");
		userRepo.change(map_filter1, updatedUser);
		
		for (User user : userRepo.fetchAll()) {
			System.out.println(user.getLogin());
		}
		
		System.out.println("deleting...");

		userRepo.delete(new HashMap<String, String>());
		for (User user : userRepo.fetchAll()) {
			System.out.println(user.getLogin());
		}	
		
		databaseManager.disconnect();
		
		System.out.println("All tests are done.");
	}
}
