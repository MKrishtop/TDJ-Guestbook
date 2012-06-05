package mkrishtop.servlet;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mkrishtop.core.GuestBook;
import mkrishtop.main.Book;
import mkrishtop.main.Record;
import mkrishtop.main.User;

public class TomcatGuestBook extends HttpServlet {

    protected ActionFactory factory = new ActionFactory();

    public TomcatGuestBook() {
         super();
    }

    protected String getActionName(HttpServletRequest request) {
         String path = request.getServletPath();
         return path.substring(1, path.lastIndexOf("."));
    }

    public void service(HttpServletRequest request, HttpServletResponse response) 
      throws 
    	ServletException, IOException {
         Action action = factory.create(getActionName(request));
         String url = action.perform(request, response);
         if (url != null)
              getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}
