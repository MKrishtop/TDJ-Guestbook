package mkrishtop.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mkrishtop.core.GuestBook;
import mkrishtop.exception.ConnectionAlreadyExistsException;
import mkrishtop.exception.ConnectionDoesntExistsException;
import mkrishtop.exception.ConnectionTableAccessLevelException;
import mkrishtop.exception.ConnectionTableException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DerbyDatabaseConnector {
	private static DerbyDatabaseConnector m_instance;
	private Connection m_connection = null;
	static final Logger logger = Logger.getLogger(DerbyDatabaseManager.class);

	private DerbyDatabaseConnector() {
		BasicConfigurator.configure();
	}

	public void setConnection(Connection connection) {
		m_connection = connection;
	}
	
	public static synchronized DerbyDatabaseConnector getInstance() {
		if (m_instance == null) {
			m_instance = new DerbyDatabaseConnector();
		}
		return m_instance;
	}

	public void setAccessLevel(String table1, String table2, int key1,
			int key2, int level) throws ConnectionTableException {
		String connectionTableName = getConnectionTableName(table1, table2);

		if (!((table1.equalsIgnoreCase("books") && table2
				.equalsIgnoreCase("users")) || (table1
				.equalsIgnoreCase("users") && table2.equalsIgnoreCase("books"))))
			throw new ConnectionTableAccessLevelException();

		if (!isConnectionTableExist(connectionTableName)) {
			connectionTableName = getConnectionTableName(table2, table1);
			int tmpKey = key1;
			key1 = key2;
			key2 = tmpKey;
			String tmpTableName = table1;
			table1 = table2;
			table2 = tmpTableName;
			if (!isConnectionTableExist(connectionTableName)) {
				throw new ConnectionTableException();
			}
		}

		try {
			Statement stmt = m_connection.createStatement();
			stmt.execute("update " + connectionTableName + " set access_level="
					+ Integer.toString(level) + " where key1="
					+ Integer.toString(key1) + " and key2="
					+ Integer.toString(key2));
			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
	}

	public int getAccessLevel(String table1, String table2, int key1, int key2)
			throws ConnectionTableException {
		int result = 0;
		String connectionTableName = getConnectionTableName(table1, table2);

		if (!((table1.equalsIgnoreCase("books") && table2
				.equalsIgnoreCase("users")) || (table1
				.equalsIgnoreCase("users") && table2.equalsIgnoreCase("books"))))
			throw new ConnectionTableAccessLevelException();

		if (!isConnectionTableExist(connectionTableName)) {
			connectionTableName = getConnectionTableName(table2, table1);
			int tmpKey = key1;
			key1 = key2;
			key2 = tmpKey;
			String tmpTableName = table1;
			table1 = table2;
			table2 = tmpTableName;
			if (!isConnectionTableExist(connectionTableName)) {
				throw new ConnectionTableException();
			}
		}

		try {
			Statement stmt = m_connection.createStatement();
			ResultSet res = stmt.executeQuery("select * from "
					+ connectionTableName + " where key1="
					+ Integer.toString(key1) + " and key2="
					+ Integer.toString(key2));

			if (res.next())
				result = res.getInt("access_level");

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}

		return result;
	}

	public void connect(String table1, String table2, int key1, int key2)
			throws ConnectionTableException {
		String connectionTableName = getConnectionTableName(table1, table2);

		if (!isConnectionTableExist(connectionTableName)) {
			connectionTableName = getConnectionTableName(table2, table1);
			int tmpkey = key1;
			key1 = key2;
			key2 = tmpkey;
			String tmpTableName = table1;
			table1 = table2;
			table2 = tmpTableName;
			if (!isConnectionTableExist(connectionTableName)) {
				createConnectionTable(connectionTableName);
			}
		}

		if (!isConnected(table1, table2, key1, key2)) {
			try {
				Statement stmt = m_connection.createStatement();
				stmt.execute("insert into " + connectionTableName
						+ "(key1, key2) values(" + Integer.toString(key1)
						+ ", " + Integer.toString(key2) + ")");
				stmt.close();
			} catch (SQLException ex) {
				logger.error(ex.toString());
			}
		} else
			throw new ConnectionAlreadyExistsException();
	}

	public void disconnect(String table1, String table2, int key1, int key2)
			throws ConnectionTableException {
		String connectionTableName = getConnectionTableName(table1, table2);

		if (!isConnectionTableExist(connectionTableName)) {
			connectionTableName = getConnectionTableName(table2, table1);
			int tmpkey = key1;
			key1 = key2;
			key2 = tmpkey;
			String tmpTableName = table1;
			table1 = table2;
			table2 = tmpTableName;
			if (!isConnectionTableExist(connectionTableName)) {
				throw new ConnectionTableException();
			}
		}

		if (isConnected(table1, table2, key1, key2)) {
			try {
				Statement stmt = m_connection.createStatement();
				stmt.execute("delete from " + connectionTableName
						+ " where key1=" + Integer.toString(key1)
						+ " and key2=" + Integer.toString(key2));
				stmt.close();
			} catch (SQLException ex) {
				logger.error(ex.toString());
			}
		} else
			throw new ConnectionDoesntExistsException();
	}

	public boolean isConnected(String table1, String table2, int key1, int key2) {
		boolean result = false;
		String connectionTableName = getConnectionTableName(table1, table2);

		if (!isConnectionTableExist(connectionTableName)) {
			connectionTableName = getConnectionTableName(table2, table1);
			int tmpkey = key1;
			key1 = key2;
			key2 = tmpkey;
			String tmpTableName = table1;
			table1 = table2;
			table2 = tmpTableName;
			if (!isConnectionTableExist(connectionTableName)) {
				return result;
			}
		}

		try {
			Statement stmt = m_connection.createStatement();
			ResultSet resSet = stmt.executeQuery("select * from "
					+ connectionTableName + " where key1="
					+ Integer.toString(key1) + " and key2="
					+ Integer.toString(key2));
			if (resSet.next()) {
				result = true;
			}
			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		return result;
	}

	private boolean isConnectionTableExist(String connectionTableName) {
		try {
			ResultSet tablesResult = m_connection.getMetaData().getTables(null,
					null, connectionTableName.toUpperCase(), null);
			if (tablesResult.next())
				return true;
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		return false;
	}

	private void createConnectionTable(String connectionTableName) {
		boolean isUserBookConnection = connectionTableName
				.equalsIgnoreCase("connect_books_users")
				|| connectionTableName.equalsIgnoreCase("connect_users_books");
		String privelegiesStr = isUserBookConnection ? ", access_level integer"
				: "";

		try {
			Statement stmt = m_connection.createStatement();
			stmt.execute("create table " + connectionTableName
					+ "(key1 integer, key2 integer" + privelegiesStr + ")");
			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
	}

	private String getConnectionTableName(String table1, String table2) {
		return "connect_" + table1 + "_" + table2;
	}
}
