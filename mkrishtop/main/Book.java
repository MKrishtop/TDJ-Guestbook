package mkrishtop.main;

public class Book {
	private int id = -1;
	private String name = "";
	
	public Book(String name) {
		this.name = name;
	}
	
	public Book(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "book_" + id + "_" + name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
