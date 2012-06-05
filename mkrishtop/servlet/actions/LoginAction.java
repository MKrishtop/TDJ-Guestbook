package mkrishtop.servlet.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mkrishtop.core.GuestBook;
import mkrishtop.main.User;
import mkrishtop.servlet.Action;

public class LoginAction implements Action {

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");

		GuestBook guestBook = GuestBook.getInstance();
		guestBook.init(is);

		User currentUser = guestBook.login(request.getParameter("login"),
				request.getParameter("password"));
		
		session.setAttribute("current_user", currentUser);
		
		guestBook.close();
		
		return "/index.jsp";
	}

}
