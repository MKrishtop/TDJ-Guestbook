package mkrishtop.core;

import java.io.InputStream;
import java.util.ArrayList;

import mkrishtop.main.Book;
import mkrishtop.main.Record;
import mkrishtop.main.User;

public interface GuestBookInterface {
	void init(InputStream is);
	User login(String login, String password);
	void setSuperuserIfNotExists(User superuser);
	void close();
	
	ArrayList<Book> getBooks(User moders);
	ArrayList<Book> getAllBooks();
	void addBook(Book book, User superuser);
	void changeBook(Book newBook, Book oldBook, User superuser);
	void deleteBook(Book book, User superuser);

	ArrayList<User> getModers(Book book);
	ArrayList<User> getAllModers();
	void addModerator(User moderator, Book book, User superuser);
	void removeModerator(User moderator, Book book, User superuser);
	
	public ArrayList<User> getAllUsers();
	void newUser(User user);
	void changeUser(User oldUser,User newUser, User superuser);
	void deleteUser(User user, User superuser);

	ArrayList<Record> getAnswers(Record record);
	ArrayList<Record> getRecords(Book book, User user);
	void addRecord(Book book, Record record, User user);
	void changeRecord(Book book, Record oldRecord, Record newRecord, User user);
	void deleteRecord(Book book, Record record, User user);
	
	User getUserById(int id);
	Record getRecordById(int id);
	Book getBookById(int id);
}
