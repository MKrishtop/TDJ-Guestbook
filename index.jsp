<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*,mkrishtop.main.*,mkrishtop.core.*,java.io.InputStream" %>

	<head>
		<title>
			mkrishtop guest book
		</title>
	</head>
	<body>
		<a href="index.jsp">
			Books list
		</a>
		&nbsp;&nbsp;
		
		<%
			GuestBook guestBook = GuestBook.getInstance();
			InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
			guestBook.init(is);
			if (!guestBook.isAlreadyHaveSuperuser()) {
				User superuser = new User("admin", "admin");
				guestBook.setSuperuserIfNotExists(superuser);
			}
		%>
		
		<%
       		if (session.getAttribute("current_user") != null && guestBook.isUserHaveSuperuserAccess(((User)(session.getAttribute("current_user"))))) {
     	%>
		<a href="admin.jsp">
			Administration
		</a>
		<%
       		}
     	%>
     	
		<p>

		<%
       		if (session.getAttribute("current_user") == null) {
     	%>
		
		<form method="post" action ="loginAction.perform">
			Login:&nbsp;
			<input type="text" name="login">&nbsp;&nbsp;
			Password:&nbsp;
			<input type="password" name="password">&nbsp;&nbsp;
			<input type="submit" value="Login">
		</form>
		,&nbsp;or &nbsp;&nbsp;
		<a href="registration.jsp">
			register
		</a>
		<%
       		} else {
     	%>
		Welcome, <%=((User)(session.getAttribute("current_user"))).getLogin()%>&nbsp;&nbsp;
		<a href="logoutAction.perform">
			logout
		</a>
		<%
       		}
     	%>
		
		<%  ArrayList<Book> allBooks = guestBook.getAllBooks();%>
		<p align="left" style="color:#000000; font-size:30px">
			Books List
		</p>
		<table border=2px>
         	<tr>
				<td>
					<b>Name</b>
				</td>
				<td>
					<b>Access Level</b>
				</td>
			</tr>
			<% for (Book book : allBooks) { %>
			<tr>
				<td>
					<a href="book.jsp?book_id=<%= book.getId() %>">
						<%= book.getName() %>
					</a>
				</td>
				<td>
					<% if (session.getAttribute("current_user") != null && guestBook.isUserHaveModeratorAccess(((User)(session.getAttribute("current_user"))), book)) { %>
					Moderator
					<% } else
       					if (session.getAttribute("current_user") != null && guestBook.isUserHaveSuperuserAccess(((User)(session.getAttribute("current_user"))))) {
     				%>
					Administrator
					<%
       					} else {
     				%>
     				Guest
     				<%
       					}
     				%>
				</td>
			</tr>
			<% } %>
		</table>
		
		<%
			guestBook.close();
		%>
	</body>
