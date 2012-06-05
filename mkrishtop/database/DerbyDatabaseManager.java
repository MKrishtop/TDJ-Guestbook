package mkrishtop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DerbyDatabaseManager {
	private static DerbyDatabaseManager m_instance;
	private Connection m_connection = null;
	static final Logger logger = Logger.getLogger(DerbyDatabaseManager.class);

	public Connection getConnection() {
		return m_connection;
	}

	private DerbyDatabaseManager() {
		BasicConfigurator.configure();
	}

	public static synchronized DerbyDatabaseManager getInstance() {
		if (m_instance == null) {
			m_instance = new DerbyDatabaseManager();
		}
		return m_instance;
	}

	public void connect(PropertiesReader propsReader) {
		if (m_connection != null) {
			disconnect();
		}
		
		try {
			Class.forName(propsReader.getProperty("db.driver.class"));

			m_connection = DriverManager.getConnection(propsReader
					.getProperty("db.connection.url"), propsReader
					.getProperty("db.username"), propsReader
					.getProperty("db.password"));

			if (!isTableExist("users")) {
				createTable("users");
			}
			if (!isTableExist("books")) {
				createTable("books");
			}
			if (!isTableExist("records")) {
				createTable("records");
			}


		} catch (SQLException ex) {
			logger.error(ex.toString());
		} catch (ClassNotFoundException ex) {
			logger.error(ex.toString());
		}
	}
	
	public void disconnect() {
		try {
			m_connection.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		m_connection = null;
	}
	
	private boolean isTableExist(String tablename) {
		try {
			ResultSet tablesResult = m_connection.getMetaData().getTables(null, null, tablename.toUpperCase(), null);
			if (tablesResult.next())
				return true;
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		return false;
	}
	
	private void createTable(String tablename) {
		String creationQuery;
		if (tablename.equals("users")) {
			creationQuery = "create table users(" +
					"id integer not null generated always as identity (start with 1, increment by 1), " +
					"login char(100) unique, " +
					"passwd char(100), " +
					"constraint user_id primary key (id))";
		} else if (tablename.equals("books")) {
			creationQuery = "create table books(" +
					"id integer not null generated always as identity (start with 1, increment by 1), " +
					"name char(100) unique, " +
					"constraint book_id primary key (id))";
		} else if (tablename.equals("records")) {
			creationQuery = "create table records(" +
					"id integer not null generated always as identity (start with 1, increment by 1), " +
					"data long varchar, " +
					"status int, " +
					"root_record int, " +
					"user_id int, " +
					"constraint record_id primary key (id))";
		} else{
			return;
		}
		
		try {
			Statement stmt = m_connection.createStatement();

			stmt.execute(creationQuery);
			
			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
	}
}
