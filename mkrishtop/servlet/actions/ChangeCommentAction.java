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

public class ChangeCommentAction implements Action {

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		GuestBook guestBook = GuestBook.getInstance();
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");

		guestBook.init(is);

		Book book = guestBook.getBookById(Integer.parseInt(request.getParameter("book_id")));
		User user = guestBook.getUserById(Integer.parseInt(request.getParameter("user_id")));
		Record oldComment = guestBook.getRecordById(Integer.parseInt(request.getParameter("comment_id")));

		Boolean forAll = Boolean.parseBoolean(request.getParameter("for_all"));
		Record newComment = new Record(request.getParameter("comment_data"), forAll ? 0 : 1, user.getId());
		
		guestBook.changeRecord(book, oldComment, newComment, user);
		
		guestBook.close();
		
		return "/book.jsp?book_id=" + book.getId();
	}

}
