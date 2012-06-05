package mkrishtop.database;

import java.util.ArrayList;
import java.util.HashMap;

import mkrishtop.main.Record;

public interface RecordRepository {
	ArrayList<Record> fetchAll();
	ArrayList<Record> fetch(HashMap<String,String> filter);
	
	boolean delete(HashMap<String,String> filter);
	void add(Record record);
	boolean change(HashMap<String,String> filter, Record record);
	//void saveAll(ArrayList<Book> persons);
}
