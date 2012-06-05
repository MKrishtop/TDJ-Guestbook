package mkrishtop.tests;

import java.util.HashMap;

import mkrishtop.database.DerbyDatabaseManager;
import mkrishtop.database.DerbyRecordRepository;
import mkrishtop.database.PropertiesReader;
import mkrishtop.main.Record;

public class TestRecordDB {
	public static void main(String[] args) {
		System.out.println("Testing records db.");
		
		PropertiesReader properties = new PropertiesReader("config.properties");
		
		DerbyDatabaseManager databaseManager = DerbyDatabaseManager.getInstance();
		databaseManager.connect(properties);
		
		DerbyRecordRepository recordRepo = DerbyRecordRepository.getInstance();
		recordRepo.setConnection(databaseManager.getConnection());
		
		System.out.println("creation...");
		
		Record rec1 = new Record("Hi there, guyz", 0, 1); 
		recordRepo.add(rec1);
		recordRepo.add(new Record("Hi, moder", 1, 1));
		Record rec2 = new Record("first ping", 0, 2); 
		rec2.setRootRecord(rec1.getId());
		recordRepo.add(rec2);
		
		for (Record record : recordRepo.fetchAll()) {
			System.out.println(record.getData());
		}
		
		System.out.println("changing...");
		
		HashMap<String, String> map_filter2 = new HashMap<String, String>();
		map_filter2.put("id", Integer.toString(rec1.getId()));
		recordRepo.delete(map_filter2);
		
		HashMap<String, String> map_filter1 = new HashMap<String, String>();
		map_filter1.put("id", Integer.toString(rec2.getId()));	
		Record updatedRecord = new Record("first ping(changed)", 1, 2);
		recordRepo.change(map_filter1, updatedRecord);
		
		for (Record record : recordRepo.fetchAll()) {
			System.out.println(record.getData());
		}
		
		System.out.println("deleting...");

		recordRepo.delete(new HashMap<String, String>());
		for (Record record : recordRepo.fetchAll()) {
			System.out.println(record.getData());
		}	
		
		databaseManager.disconnect();
		
		System.out.println("All tests are done.");
	}
}
