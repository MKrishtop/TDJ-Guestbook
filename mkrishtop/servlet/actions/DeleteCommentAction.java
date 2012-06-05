package mkrishtop.servlet.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mkrishtop.core.GuestBook;
import mkrishtop.main.Book;
import mkrishtop.main.Record;
import mkrishtop.main.User;
import mkrishtop.servlet.Action;

public class DeleteCommentAction implements Action {

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		GuestBook guestBook = GuestBook.getInstance();
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");

		guestBook.init(is);

		Book book = guestBook.getBookById(Integer.parseInt(request.getParameter("book_id")));
		User user = (User)session.getAttribute("current_user");
		Record comment = guestBook.getRecordById(Integer.parseInt(request.getParameter("comment_id")));
		
		guestBook.deleteRecord(book, comment, user);
		
		guestBook.close();
		
		return "/book.jsp?book_id=" + book.getId();
	}
}
