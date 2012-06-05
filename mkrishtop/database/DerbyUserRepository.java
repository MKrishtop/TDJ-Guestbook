package mkrishtop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import mkrishtop.main.User;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DerbyUserRepository implements UserRepository{

	private static DerbyUserRepository m_instance;
	private Connection m_connection = null;
	static final Logger logger = Logger.getLogger(DerbyUserRepository.class);
	
	static final String m_addUser = "insert into users(login, passwd) values (?, ?)";
	static final String m_deleteUser = "delete from users";
	static final String m_updateUser = "update users";
	static final String m_fetchAllUsersQuery = "select id, login, passwd from users";

	public Connection getConnection() {
		return m_connection;
	}

	private DerbyUserRepository() {
		BasicConfigurator.configure();
	}

	public static synchronized DerbyUserRepository getInstance() {
		if (m_instance == null) {
			m_instance = new DerbyUserRepository();
		}
		return m_instance;
	}
	
	public void setConnection(Connection connection) {
		m_connection = connection;
	}
	
	@Override
	public void add(User user) {
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");
			
			PreparedStatement prepStmt = m_connection.prepareStatement(m_addUser, PreparedStatement.RETURN_GENERATED_KEYS);
			
			prepStmt.setString(1, user.getLogin());
			prepStmt.setString(2, user.getPassword());
			prepStmt.executeUpdate();

			ResultSet res = prepStmt.getGeneratedKeys();
			
			if (res.next())
				user.setId(res.getInt(1));

			prepStmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public boolean change(HashMap<String, String> filter,
			User user) {
		boolean result = false;
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("<s>login", user.getLogin());
		hmap.put("<s>passwd", user.getPassword());
		
		String setString = Converter.getSetString(hmap);
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			if (stmt.executeUpdate(m_updateUser + setString + whereString) != 0) 
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
			
			if (stmt.executeUpdate(m_deleteUser + whereString) != 0) 
				result = true;

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		return result;
	}

	@Override
	public ArrayList<User> fetch(HashMap<String, String> filter) {
		ArrayList<User> users = new ArrayList<User>();
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			ResultSet res = stmt.executeQuery(m_fetchAllUsersQuery + whereString);
			
			while (res.next()) {
				User user = new User(res.getInt("id"), res.getString("login").trim(), res.getString("passwd").trim());
				users.add(user);
			}

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		return users;
	}

	@Override
	public ArrayList<User> fetchAll() {
		return fetch(new HashMap<String, String>());
	}
	
}
