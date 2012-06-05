package mkrishtop.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import mkrishtop.database.DerbyBookRepository;
import mkrishtop.database.DerbyDatabaseConnector;
import mkrishtop.database.DerbyDatabaseManager;
import mkrishtop.database.DerbyRecordRepository;
import mkrishtop.database.DerbyUserRepository;
import mkrishtop.database.PropertiesReader;
import mkrishtop.exception.ConnectionTableException;
import mkrishtop.main.Book;
import mkrishtop.main.Record;
import mkrishtop.main.User;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class GuestBook implements GuestBookInterface {
	private DerbyRecordRepository m_recordRepo = null;
	private DerbyBookRepository m_bookRepo = null;
	private DerbyUserRepository m_userRepo = null;
	private DerbyDatabaseManager m_databaseManager = null;
	private DerbyDatabaseConnector m_databaseConnector = null;
	private User m_superuser = null;
	private static GuestBook m_instance;
	private static final Logger logger = Logger.getLogger(GuestBook.class);

	private GuestBook() {
		BasicConfigurator.configure();
	}

	public static synchronized GuestBook getInstance() {
		if (m_instance == null) {
			m_instance = new GuestBook();
		}
		return m_instance;
	}

	@Override
	public void init(InputStream is) {
		PropertiesReader properties = new PropertiesReader(is);

		m_databaseManager = DerbyDatabaseManager.getInstance();
		m_databaseManager.connect(properties);

		m_recordRepo = DerbyRecordRepository.getInstance();
		m_bookRepo = DerbyBookRepository.getInstance();
		m_userRepo = DerbyUserRepository.getInstance();
		m_databaseConnector = DerbyDatabaseConnector.getInstance();
		
		m_recordRepo.setConnection(m_databaseManager.getConnection());
		m_bookRepo.setConnection(m_databaseManager.getConnection());
		m_userRepo.setConnection(m_databaseManager.getConnection());
		m_databaseConnector.setConnection(m_databaseManager.getConnection());
	}
	
	@Override
	public void close() {
		m_databaseManager.disconnect();
	}

	@Override
	public User login(String login, String password) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("<s>login", login);
		filter.put("<s>passwd", password);

		try {
			return m_userRepo.fetch(filter).get(0);
		} catch (IndexOutOfBoundsException ex) {
			return new User(-1, "guest", "guest");
		}
	}

	@Override
	public ArrayList<Book> getAllBooks() {
		return m_bookRepo.fetchAll();
	}
	
	@Override
	public ArrayList<User> getAllUsers() {
		return m_userRepo.fetchAll();
	}

	@Override
	public ArrayList<User> getAllModers() {
		ArrayList<User> moders = new ArrayList<User>();

		ArrayList<Book> books = m_bookRepo.fetchAll();
		ArrayList<User> users = m_userRepo.fetchAll();

		for (Book book : books) {
			for (User user : users) {
				if (m_databaseConnector.isConnected("books", "users", book
						.getId(), user.getId())) {
					if (moders.indexOf(user) == -1) moders.add(user);
//					users.remove(user);
				}
			}
		}

		return moders;
	}

	@Override
	public ArrayList<Record> getAnswers(Record record) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("root_record", Integer.toString(record.getId()));
		return m_recordRepo.fetch(filter);
	}

	@Override
	public ArrayList<Book> getBooks(User moder) {
		ArrayList<Book> books = new ArrayList<Book>();

		for (Book book : m_bookRepo.fetchAll()) {
			if (m_databaseConnector.isConnected("books", "users", book.getId(),
					moder.getId())) {
				try {
					if (m_databaseConnector.getAccessLevel("books", "users",
							book.getId(), moder.getId()) == 1) {
						books.add(book);
					}
				} catch (ConnectionTableException ex) {
					logger.error(ex.toString());
				}
			}
		}

		return books;
	}

	@Override
	public ArrayList<User> getModers(Book book) {
		ArrayList<User> users = new ArrayList<User>();

		for (User user : m_userRepo.fetchAll()) {
			if (m_databaseConnector.isConnected("books", "users", book.getId(),
					user.getId())) {
				try {
					if (m_databaseConnector.getAccessLevel("books", "users",
							book.getId(), user.getId()) == 1) {
						users.add(user);
					}
				} catch (ConnectionTableException ex) {
					logger.error(ex.toString());
				}
			}
		}

		return users;
	}

	@Override
	public ArrayList<Record> getRecords(Book book, User user) {
		ArrayList<Record> records = new ArrayList<Record>();

		for (Record record : m_recordRepo.fetchAll()) {
			try {
				if (m_databaseConnector.isConnected("books", "records", book
						.getId(), record.getId())
						&& (m_databaseConnector.getAccessLevel("books",
								"users", book.getId(), user.getId()) >= record
								.getStatus())
								&& record.getRootRecord()==-1) {
					records.add(record);
				}
			} catch (ConnectionTableException ex) {
				logger.error(ex.toString());
			}
		}

		return records;
	}

	@Override
	public void addBook(Book book, User superuser) {
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		m_bookRepo.add(book);
		try {
			m_databaseConnector.connect("books", "users", book.getId(),
					superuser.getId());
		} catch (ConnectionTableException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public void changeBook(Book newBook, Book oldBook, User superuser) {
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(oldBook.getId()));

		m_bookRepo.change(filter, newBook);
	}

	@Override
	public void deleteBook(Book book, User superuser) {
		//TODO: delete not by id
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		for (Record record : m_recordRepo.fetchAll()) {
			if (m_databaseConnector.isConnected("books", "records", book
					.getId(), record.getId())) {
				HashMap<String, String> filter = new HashMap<String, String>();
				filter.put("id", Integer.toString(record.getId()));
				m_recordRepo.delete(filter);
				try {
					m_databaseConnector.disconnect("books", "records", book
							.getId(), record.getId());
				} catch (ConnectionTableException ex) {
					logger.error(ex.toString());
				}
			}
		}

		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(book.getId()));
		m_bookRepo.delete(filter);

		for (User user : m_userRepo.fetchAll()) {
			if (m_databaseConnector.isConnected("books", "users", book.getId(),
					user.getId())) {
				try {
					m_databaseConnector.disconnect("books", "users", book
							.getId(), user.getId());
				} catch (ConnectionTableException ex) {
					logger.error(ex.toString());
				}
			}
		}
	}

	@Override
	public void addRecord(Book book, Record record, User user) {
		record.setUserId(user.getId());

		m_recordRepo.add(record);

		try {
			m_databaseConnector.connect("records", "books", record.getId(),
					book.getId());
		} catch (ConnectionTableException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public void changeRecord(Book book, Record oldRecord, Record newRecord,
			User user) {
		if (!(oldRecord.getUserId() == user.getId() || isUserHaveModeratorAccess(
				user, book)))
			return;

		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(oldRecord.getId()));

		m_recordRepo.change(filter, newRecord);
	}

	@Override
	public void deleteRecord(Book book, Record record, User user) {
		//TODO: delete not by id delete connection
		if (user == null || user.getId() == -1)
			return;
		
		if (!(record.getUserId() == user.getId() || isUserHaveModeratorAccess(
				user, book)))
			return;

		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(record.getId()));

		m_recordRepo.delete(filter);
	}

	public boolean isUserHaveSuperuserAccess(User superuser) {
		if (superuser.getId() == -1 || m_superuser.getId() != superuser.getId())
			return false;
		return true;
	}

	public boolean isAlreadyHaveSuperuser() {
		if (m_superuser == null)
			return false;
		return true;
	}
	
	public boolean isUserHaveModeratorAccess(User user, Book book) {
		try {
			if (user.getId() == -1
					|| (m_databaseConnector.getAccessLevel("books", "users",
							book.getId(), user.getId()) < 1)) {
				return false;
			}
		} catch (ConnectionTableException ex) {
			logger.error(ex.toString());
		}
		return true;
	}

	@Override
	public void setSuperuserIfNotExists(User user) {
		
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("<s>login", user.getLogin());
		filter.put("<s>passwd", user.getPassword());

		try {
			m_superuser = m_userRepo.fetch(filter).get(0);
		} catch (IndexOutOfBoundsException ex) {
			m_superuser = user;
			m_userRepo.add(m_superuser);
		}
	}

	@Override
	public void addModerator(User moderator, Book book, User superuser) {
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		if (moderator == null || moderator.getId() == -1)
			return;

		try {
			m_databaseConnector.connect("users", "books", moderator.getId(),
					book.getId());
			m_databaseConnector.setAccessLevel("users", "books", moderator
					.getId(), book.getId(), 1);
		} catch (ConnectionTableException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public void removeModerator(User moderator, Book book, User superuser) {
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		if (moderator == null || moderator.getId() == -1)
			return;

		try {
			m_databaseConnector.disconnect("users", "books", moderator.getId(),
					book.getId());
		} catch (ConnectionTableException ex) {
			logger.error(ex.toString());
		}
	}

	@Override
	public void changeUser(User oldUser, User newUser, User superuser) {
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(oldUser.getId()));

		m_userRepo.change(filter, newUser);
	}

	@Override
	public void deleteUser(User user, User superuser) {
		//TODO: delete not by id
		if (!isUserHaveSuperuserAccess(superuser))
			return;

		for (Book book : m_bookRepo.fetchAll()) {
			if (m_databaseConnector.isConnected("books", "users", book.getId(),
					user.getId())) {
				try {
					m_databaseConnector.disconnect("books", "users", book
							.getId(), user.getId());
				} catch (ConnectionTableException ex) {
					logger.error(ex.toString());
				}
			}
		}

		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(user.getId()));

		m_userRepo.delete(filter);
	}

	@Override
	public void newUser(User user) {

		m_userRepo.add(user);
	}

	@Override
	public Book getBookById(int id) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(id));
		
		ArrayList<Book> books = m_bookRepo.fetch(filter);
		
		try {
			return books.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	@Override
	public Record getRecordById(int id) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(id));
		
		ArrayList<Record> records = m_recordRepo.fetch(filter);
		
		try {
			return records.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	@Override
	public User getUserById(int id) {
		HashMap<String, String> filter = new HashMap<String, String>();
		filter.put("id", Integer.toString(id));
		
		ArrayList<User> users = m_userRepo.fetch(filter);
		
		try {
			return users.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return new User(-1, "guest", "guest");
		}
	}	
}
