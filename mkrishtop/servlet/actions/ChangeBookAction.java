package mkrishtop.servlet.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mkrishtop.core.GuestBook;
import mkrishtop.main.Book;
import mkrishtop.main.User;
import mkrishtop.servlet.Action;

public class ChangeBookAction implements Action {
	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		GuestBook guestBook = GuestBook.getInstance();
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");

		guestBook.init(is);

		Book oldBook = new Book(Integer.parseInt(request.getParameter("book_id")), "");
		Book newBook = new Book(request.getParameter("book_name"));
		
		guestBook.changeBook(newBook, oldBook, (User)session.getAttribute("current_user"));
		
		guestBook.close();
		
		return "/admin.jsp";
	}
}
