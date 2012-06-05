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
		
		<% Book currentBook = guestBook.getBookById(Integer.parseInt(request.getParameter("book_id")));%>
			
		<p align="left" style="color:#000000; font-size:30px">
			<%= currentBook.getName() %> Book
		</p>
		<table border=3px>
			<tr>
				<td>
					<b>Action</b>
				</td>
				<td>
					<b>User</b>
				</td>
				<td>
					<b>Comment</b>
				</td>
				<td>
					<b>Answers</b>
				</td>
			</tr>
			
			<% User currentUser =  (session.getAttribute("current_user") != null) ? (User)(session.getAttribute("current_user")) : (new User("guest","guest"));
			for (Record record : guestBook.getRecords(currentBook, currentUser)) { %>
			<tr>
				<td>
					<table>
						<tr>
							<a href="book.jsp?book_id=<%= request.getParameter("book_id") %>&action=add_answer&comment_id=<%=Integer.toString(record.getId())%>">
								Answer
							</a>
						</tr><br>
						<tr>
							<a href="book.jsp?book_id=<%= request.getParameter("book_id") %>&action=change_comment&comment_id=<%=Integer.toString(record.getId())%>">
								Change
							</a>
						</tr><br>
						<tr>
							<a href="deleteCommentAction.perform?book_id=<%= request.getParameter("book_id") %>&comment_id=<%=Integer.toString(record.getId())%>">
								Delete
							</a>
						</tr>
					</table>
				</td>
				<td>
					<%= guestBook.getUserById(record.getUserId()).getLogin() %>
				</td>
				<td>
					<%= record.getData() %>
				</td>
				<td>
					<table border=1px>
						<% for (Record answer : guestBook.getAnswers(record)) { %>
						<tr>
							<td>
								<%= guestBook.getUserById(answer.getUserId()).getLogin() %>
							</td>
							<td>
								<%= answer.getData() %>
							</td>
						</tr>
						<% } %>
					</table>
				</td>
			</tr>
			<% } %>
			
		</table>
		<a href="book.jsp?book_id=<%= request.getParameter("book_id") %>&action=add_comment&comment_id=-1">
			Add...
		</a>
		<br><br><br>
		
		<% if (request.getParameter("action") != null) {
		String actionName = (String) request.getParameter("action");
		String commentId = request.getParameter("comment_id");
		
		if (actionName.equals("add_comment")) {
		%>
		<table border=3px>
			<tr>
				<td>
					<form method="post" action ="addCommentAction.perform">
						for all:
						<input type="radio" name="for_all" value="true" checked>
						&nbsp;&nbsp;for moderators:
						<input type="radio" name="for_all" value="false">
						<p>
						comment:
						<textarea name="comment_data" rows=7 cols=30 value="">
						</textarea>
						<input type="hidden" name="book_id" value=<%=currentBook.getId() %>>
						<input type="hidden" name="user_id" value=<%=currentUser.getId() %>>
						<p>
						<input type="submit" value="Add comment">
						<p>
					</form>
				</td>
			</tr>
		</table><br>
		
		<%
       		} else if (actionName.equals("add_answer")) {
       	%>
       	
		<table border=3px>
			<tr>
				<td>
					<form method="post" action ="addAnswerAction.perform">
						for all:
						<input type="radio" name="for_all" value="true" checked>
						&nbsp;&nbsp;for moderators:
						<input type="radio" name="for_all" value="false">
						<p>
						comment:
						<textarea name="comment_data" rows=7 cols=30 value="">
						</textarea>
						<input type="hidden" name="comment_id" value=<%= commentId %>>
						<input type="hidden" name="book_id" value=<%=currentBook.getId() %>>
						<input type="hidden" name="user_id" value=<%=currentUser.getId() %>>
						<p>
						<input type="submit" value="Add answer">
						<p>
					</form>
				</td>
			</tr>
		</table><br>
		
		<%
       		} else if (actionName.equals("change_comment")) {
       	%>
       	
		<table border=3px>
			<tr>
				<td>
					<form method="post" action ="changeCommentAction.perform">
						for all:
						<input type="radio" name="for_all" value="true" checked>
						&nbsp;&nbsp;for moderators:
						<input type="radio" name="for_all"  value="false">
						<p>
						comment:
						<textarea name="comment_data" rows=7 cols=30 value="">
						</textarea>
						<input type="hidden" name="comment_id" value=<%= commentId %>>
						<input type="hidden" name="book_id" value=<%=currentBook.getId()%>>
						<input type="hidden" name="user_id" value=<%=currentUser.getId()%>>
						<p>
						<input type="submit" value="Change comment">
						<p>
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
     	<% guestBook.close();%>
	</body>
