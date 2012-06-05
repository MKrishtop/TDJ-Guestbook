package mkrishtop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import mkrishtop.main.Book;
import mkrishtop.main.User;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DerbyBookRepository implements BookRepository{

	private static DerbyBookRepository m_instance;
	private Connection m_connection = null;
	static final Logger logger = Logger.getLogger(DerbyBookRepository.class);
	
	static final String m_addBook = "insert into books(name) values (?)";
	static final String m_deleteBook = "delete from books";
	static final String m_updateBook = "update books";
	static final String m_fetchAllBooksQuery = "select id, name from books";

	public Connection getConnection() {
		return m_connection;
	}

	private DerbyBookRepository() {
		BasicConfigurator.configure();
	}

	public static synchronized DerbyBookRepository getInstance() {
		if (m_instance == null) {
			m_instance = new DerbyBookRepository();
		}
		return m_instance;
	}
	
	public void setConnection(Connection connection) {
		m_connection = connection;
	}
	
	@Override
	public void add(Book book) {
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");
			
			PreparedStatement prepStmt = m_connection.prepareStatement(m_addBook, PreparedStatement.RETURN_GENERATED_KEYS);
			
			prepStmt.setString(1, book.getName());
			prepStmt.executeUpdate();

			ResultSet res = prepStmt.getGeneratedKeys();
			
			if (res.next())
				book.setId(res.getInt(1));

			prepStmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public boolean change(HashMap<String, String> filter,
			Book book) {
		boolean result = false;
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("<s>name", book.getName());
		
		String setString = Converter.getSetString(hmap);
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			if (stmt.executeUpdate(m_updateBook + setString + whereString) != 0) 
				result = true;

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		return result;
	}

	@Override
	public boolean delete(HashMap<String, String> filter) {
		boolean result = false;
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			if (stmt.executeUpdate(m_deleteBook + whereString) != 0) 
				result = true;

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		return result;
	}

	@Override
	public ArrayList<Book> fetch(HashMap<String, String> filter) {
		ArrayList<Book> books = new ArrayList<Book>();
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			ResultSet res = stmt.executeQuery(m_fetchAllBooksQuery + whereString);
			
			while (res.next()) {
				Book book = new Book(res.getInt("id"), res.getString("name"));
				books.add(book);
			}

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		return books;
	}

	@Override
	public ArrayList<Book> fetchAll() {
		return fetch(new HashMap<String, String>());
	}
	
}
