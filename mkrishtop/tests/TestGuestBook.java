package mkrishtop.tests;

import java.io.InputStream;

import mkrishtop.core.GuestBook;
import mkrishtop.main.Book;
import mkrishtop.main.Record;
import mkrishtop.main.User;

public class TestGuestBook {
//	public static void main(String[] args) {
//		GuestBook guestBook = GuestBook.getInstance();
//		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
//
//		guestBook.init(is);
//		
//
//		
//		guestBook.setSuperuserIfNotExists(new User("admin", "tiger"));
////		guestBook.addSuperuserToDB();
//		
//		User superuser = guestBook.login("admin", "tiger");
//		System.out.println(superuser.getLogin());
//		
//		User user1 = new User("user1", "123");
//		User user2 = new User("user2", "234");
//		User user3 = new User("user3", "345");
//		
//		Book Abook1 = new Book("Abook1");
//		Book Bbook2 = new Book("Bbook2");
//		Book Zbook3 = new Book("Zbook3");
//		
//		Record rec1 = new Record("first record", 0, superuser.getId());
//		Record rec2 = new Record("second record", 0, superuser.getId());
//		Record rec3 = new Record("third record", 0, superuser.getId());
//		
//		guestBook.newUser(user1);
//		guestBook.newUser(user2);
//		guestBook.newUser(user3);
//		
//		guestBook.addBook(Abook1, superuser);
//		guestBook.addBook(Bbook2, superuser);
//		guestBook.addBook(Zbook3, superuser);
//		
//		guestBook.addRecord(Abook1, rec1, superuser);
//		guestBook.addRecord(Abook1, rec2, user2);
//		rec3.setRootRecord(rec1.getId());
//		guestBook.addRecord(Abook1, rec3, superuser);
//		
//		guestBook.changeBook(new Book("Zbook4"), Zbook3, superuser);
//		for (Book book : guestBook.getAllBooks()) {
//			System.out.println(book);
//		}
//		System.out.println();
//		
//		guestBook.addModerator(user1, Abook1, superuser);
//		guestBook.addModerator(user2, Abook1, superuser);	
//		
//		for (User moder : guestBook.getAllModers()) {
//			System.out.println(moder);
//		}
//		System.out.println();
//		
//		for (User moder : guestBook.getModers(Abook1)) {
//			System.out.println(moder);
//		}
//		System.out.println();
//		
//		for (Book book : guestBook.getBooks(user1)) {
//			System.out.println(book);
//		}
//		System.out.println();
//		
//		guestBook.changeRecord(Abook1, rec3, new Record("third record(modified)", 0, user3.getId(), rec2.getId()), superuser);
//		for (Record rec : guestBook.getRecords(Abook1, user1)) {
//			System.out.println(rec);
//			for (Record answer : guestBook.getAnswers(rec)) {
//				System.out.println("\t" + answer);
//			}
//		}
//		System.out.println();
//		
//		guestBook.removeModerator(user1, Abook1, superuser);
//		guestBook.removeModerator(user2, Abook1, superuser);
//		
//		
//		guestBook.deleteRecord(Abook1, rec1, superuser);
//		guestBook.deleteRecord(Abook1, rec2, superuser);
//		guestBook.deleteRecord(Abook1, rec3, superuser);
//
//		guestBook.deleteBook(Abook1, superuser);
//		guestBook.deleteBook(Bbook2, superuser);
//		guestBook.deleteBook(Zbook3, superuser);
//		
//		guestBook.deleteUser(user1, superuser);
//		guestBook.deleteUser(user2, superuser);
//		guestBook.deleteUser(user3, superuser);
//		
//		guestBook.close();
//	}
}
