package mkrishtop.database;

import java.util.ArrayList;
import java.util.HashMap;

import mkrishtop.main.User;

public interface UserRepository {
	ArrayList<User> fetchAll();
	ArrayList<User> fetch(HashMap<String,String> filter);
	
	boolean delete(HashMap<String,String> filter);
	void add(User user);
	boolean change(HashMap<String,String> filter, User user);
	//void saveAll(ArrayList<Book> persons);
}
