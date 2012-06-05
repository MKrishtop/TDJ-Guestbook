package mkrishtop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import mkrishtop.main.Record;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DerbyRecordRepository implements RecordRepository{

	private static DerbyRecordRepository m_instance;
	private Connection m_connection = null;
	static final Logger logger = Logger.getLogger(DerbyRecordRepository.class);
	
	static final String m_addRecord = "insert into records(data, status, root_record, user_id) values (?, ?, ?, ?)";
	static final String m_deleteRecord = "delete from records";
	static final String m_updateRecord = "update records";
	static final String m_fetchAllRecordsQuery = "select id, data, status, root_record, user_id from records";

	public Connection getConnection() {
		return m_connection;
	}

	private DerbyRecordRepository() {
		BasicConfigurator.configure();
	}

	public static synchronized DerbyRecordRepository getInstance() {
		if (m_instance == null) {
			m_instance = new DerbyRecordRepository();
		}
		return m_instance;
	}
	
	public void setConnection(Connection connection) {
		m_connection = connection;
	}
	
	@Override
	public void add(Record record) {
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");
			
			PreparedStatement prepStmt = m_connection.prepareStatement(m_addRecord, PreparedStatement.RETURN_GENERATED_KEYS);
			
			prepStmt.setString(1, record.getData());
			prepStmt.setInt(2, record.getStatus());
			prepStmt.setInt(3, record.getRootRecord());
			prepStmt.setInt(4, record.getUserId());
			prepStmt.executeUpdate();

			ResultSet res = prepStmt.getGeneratedKeys();
			
			if (res.next())
				record.setId(res.getInt(1));

			prepStmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public boolean change(HashMap<String, String> filter,
			Record record) {
		boolean result = false;
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("<s>data", record.getData());
		hmap.put("status", Integer.toString(record.getStatus()));
		hmap.put("root_record", Integer.toString(record.getRootRecord()));
		hmap.put("user_id", Integer.toString(record.getUserId()));
		
		String setString = Converter.getSetString(hmap);
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			if (stmt.executeUpdate(m_updateRecord + setString + whereString) != 0) 
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
			
			if (stmt.executeUpdate(m_deleteRecord + whereString) != 0) 
				result = true;

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		return result;
	}

	@Override
	public ArrayList<Record> fetch(HashMap<String, String> filter) {
		LinkedList<Record> records = new LinkedList<Record>();
		String whereString = Converter.getWhereString(filter);
		
		try {
			if (m_connection == null)
				throw new SQLException(
						"Repository haven't connected to database yet.");

			Statement stmt = m_connection.createStatement();
			
			ResultSet res = stmt.executeQuery(m_fetchAllRecordsQuery + whereString);
			
			while (res.next()) {
				Record record = new Record(res.getInt("id"), 
						res.getString("data"), res.getInt("status"), res.getInt("user_id"), res.getInt("root_record"));
				records.add(record);
			}

			stmt.close();
		} catch (SQLException ex) {
			logger.error(ex.toString());
		}
		
		ArrayList<Record> reversedRecords = new ArrayList<Record>();
		
		Iterator<Record> itr = records.descendingIterator();
		
		while (itr.hasNext()) {
			reversedRecords.add(itr.next());
		}
		
		return reversedRecords;
	}

	@Override
	public ArrayList<Record> fetchAll() {
		return fetch(new HashMap<String, String>());
	}
	
}
