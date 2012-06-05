package mkrishtop.main;

import java.util.ArrayList;

public class User {
	private int id = -1;
	private String login = "";
	private String password = "";
	
	public User(int id, String login, String passwd) {
		this.id = id;
		this.login = login;
		this.password = passwd;
	}
	
	public User(String login, String passwd) {
		this.login = login;
		this.password = passwd;
	}
	
	public String toString() {
		return "user_"+id+"_"+login+"_" +password;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
