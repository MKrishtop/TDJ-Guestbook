package mkrishtop.servlet.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mkrishtop.core.GuestBook;
import mkrishtop.servlet.Action;
import mkrishtop.main.*;

public class AddBookAction implements Action {

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		GuestBook guestBook = GuestBook.getInstance();
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");

		guestBook.init(is);

		Book newBook = new Book(request.getParameter("book_name"));
		
		guestBook.addBook(newBook, (User)session.getAttribute("current_user"));
		
		guestBook.close();
		
		return "/admin.jsp";
	}

}
