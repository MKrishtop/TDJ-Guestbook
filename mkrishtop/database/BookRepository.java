package mkrishtop.database;

import java.util.ArrayList;
import java.util.HashMap;

import mkrishtop.main.Book;

public interface BookRepository {
	ArrayList<Book> fetchAll();
	ArrayList<Book> fetch(HashMap<String,String> filter);
	
	boolean delete(HashMap<String,String> filter);
	void add(Book book);
	boolean change(HashMap<String,String> filter, Book book);
	//void saveAll(ArrayList<Book> persons);
}
