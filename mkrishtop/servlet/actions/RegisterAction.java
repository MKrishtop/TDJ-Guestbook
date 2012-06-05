package mkrishtop.servlet.actions;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mkrishtop.core.GuestBook;
import mkrishtop.main.User;
import mkrishtop.servlet.Action;

public class RegisterAction implements Action {

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		GuestBook guestBook = GuestBook.getInstance();
		InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");

		guestBook.init(is);

		if (request.getParameter("password").equals(request.getParameter("repeat_password"))) {
			User newUser = new User(request.getParameter("login"),request.getParameter("password"));
			guestBook.newUser(newUser);
		}
		
		guestBook.close();
		
		return "/index.jsp";
	}

}
