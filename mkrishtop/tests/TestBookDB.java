package mkrishtop.tests;

import java.util.HashMap;

import mkrishtop.database.DerbyBookRepository;
import mkrishtop.database.DerbyDatabaseManager;
import mkrishtop.database.PropertiesReader;
import mkrishtop.main.Book;

public class TestBookDB {
	public static void main(String[] args) {
		System.out.println("Testing books db.");
		
		PropertiesReader properties = new PropertiesReader("config.properties");
		
		DerbyDatabaseManager databaseManager = DerbyDatabaseManager.getInstance();
		databaseManager.connect(properties);
		
		DerbyBookRepository bookRepo = DerbyBookRepository.getInstance();
		bookRepo.setConnection(databaseManager.getConnection());
		
		System.out.println("creation...");
		
		bookRepo.add(new Book("Abook1"));
		bookRepo.add(new Book("Bbook2"));
		bookRepo.add(new Book("Cbook3"));
		
		for (Book book : bookRepo.fetchAll()) {
			System.out.println(book.getName());
		}
		
		System.out.println("changing...");
		
		HashMap<String, String> map_filter2 = new HashMap<String, String>();
		map_filter2.put("<s>name", "Bbook2");
		bookRepo.delete(map_filter2);
		
		HashMap<String, String> map_filter1 = new HashMap<String, String>();
		map_filter1.put("<s>name", "Cbook3");		
		Book updatedBook = new Book("Cbook4");
		bookRepo.change(map_filter1, updatedBook);
		
		for (Book book : bookRepo.fetchAll()) {
			System.out.println(book.getName());
		}
		
		System.out.println("deleting...");

		bookRepo.delete(new HashMap<String, String>());
		for (Book book : bookRepo.fetchAll()) {
			System.out.println(book.getName());
		}	
		
		databaseManager.disconnect();
		
		System.out.println("All tests are done.");
	}
}
