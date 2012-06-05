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
		
		<%
       		if (session.getAttribute("current_user") != null && guestBook.isUserHaveSuperuserAccess(((User)(session.getAttribute("current_user"))))) {
       		ArrayList<Book> allBooks = guestBook.getAllBooks();
     	%>
		<p align="left" style="color:#000000; font-size:30px">
			Book List
		</p>
		<table border=3px>
			<tr>
				<td>
					<b>Action</b>
				</td>
				<td>
					<b>Book</b>
				</td>
				<td>
					<b>Moderators</b>
				</td>
			</tr>
			<% for (Book book : allBooks) { %>
			<tr>
				<td>
					<table>
						<tr>
							<a href="admin.jsp?action=change_book&book_id=<%=Integer.toString(book.getId())%>">
								Change book
							</a>
						</tr><br>
						<tr>
							<a href="deleteBookAction.perform?book_id=<%=Integer.toString(book.getId())%>">
								Delete book
							</a>
						</tr><br>
						<tr>
							<a href="admin.jsp?action=add_moder&book_id=<%=Integer.toString(book.getId())%>">
								Add moder
							</a>
						</tr><br>
						<tr>
							<a href="admin.jsp?action=remove_moder&book_id=<%=Integer.toString(book.getId())%>">
								Remove moder
							</a>
						</tr>
					</table>
				</td>
				<td>
					<%= book.getName() %>
				</td>
				<td>
					<table border=1px>
						<% for (User moder : guestBook.getModers(book)) { %>
						<tr>
							<td><%= moder.getLogin() %></td>
						</tr>
						<% } %>
					</table>
				</td>
			</tr>
			<% } %>
		</table>
		
		<a href="admin.jsp?action=add_book&book_id=-1">
			Add...
		</a>
		
		<br><br><br>
		<% if (request.getParameter("action") != null) {
		String actionName = (String) request.getParameter("action");
		int bookId = Integer.parseInt(request.getParameter("book_id"));
		
		if (actionName.equals("add_book")) {
		%>
		<table border=3px>
			<tr>
				<td>
		<form method="post" action ="addBookAction.perform">
			book name:
			<input type="text" name="book_name">
			<p>
			<input type="submit" value="Add book">
			<p>
		</form>
				</td>
			</tr>
		</table>
		<br>
		
		<%
       		} else if (actionName.equals("change_book")) {
       	%>

		<table border=3px>
			<tr>
				<td>
		<form method="post" action ="changeBookAction.perform">
			book #<%=request.getParameter("book_id") %> name:
			<input type="text" name="book_name">
			<p>
			<input type="hidden" name="book_id" value=<%=request.getParameter("book_id") %>>
			<input type="submit" value="Change book">
			<p>
		</form>
				</td>
			</tr>
		</table>
		<br>
		
		<%
       		} else if (actionName.equals("add_moder")) {
       	%>

		<table border=3px>
			<tr>
				<td>
		<form method="post" action ="addModerAction.perform">
			login:
			<select name="moder_id" size="1">
				<% for (User user : guestBook.getAllUsers()) { 
				   if (guestBook.isUserHaveSuperuserAccess(user)) continue; %>
				<option value=<%= user.getId() %>><%= user.getLogin() %></option>
				<% } %>
			</select>
			<br>
			<input type="hidden" name="book_id" value=<%=request.getParameter("book_id") %>>
			<input type="submit" value="Add moder">
		</form>
		</td>
			</tr>
		</table>
		<br>
		
		<%
       		} else if (actionName.equals("remove_moder")) {
       	%>

		<table border=3px>
			<tr>
				<td>
					<form method="post" action ="removeModerAction.perform">
						login:
						<select name="moder_id" size="1">
							<% for (User user : guestBook.getModers(guestBook.getBookById(Integer.parseInt(request.getParameter("book_id"))))) { 
				   				if (guestBook.isUserHaveSuperuserAccess(user)) continue; %>
							<option value=<%= user.getId() %>><%= user.getLogin() %></option>
							<% } %>
						</select>
						<br>
						<input type="hidden" name="book_id" value=<%=request.getParameter("book_id") %>>
						<input type="submit" value="Remove moder">
					</form>
				</td>
			</tr>
		</table>
		<%
       		}
     	%>
     	<%
       		}
     	%>
		<%
       		}
     	%>
     	<% guestBook.close();%>
	</body>
