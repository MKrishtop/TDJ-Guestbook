package mkrishtop.main;

public class Record {
	private int id = -1;
	private String data = "";
	private int status = -1;
	private int rootRecord = -1;
	private int userId = -1;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Record(String data, int status, int userId) {
		this.data = data;
		this.status = status;
		this.userId = userId;
	}
	
	public Record(String data, int status, int userId, int rootRecord) {
		this.data = data;
		this.status = status;
		this.userId = userId;
		this.rootRecord = rootRecord;
	}
	
	public Record(int id, String data, int status, int userId) {
		this.id = id;
		this.data = data;
		this.status = status;
		this.userId = userId;
	}
	
	public Record(int id, String data, int status, int userId, int rootRecord) {
		this.id = id;
		this.data = data;
		this.status = status;
		this.userId = userId;
		this.rootRecord = rootRecord;
	}
	
	public String toString() {
		return "record_"+id+"_"+data+"_" +status+"_"+rootRecord+"_"+userId;
	}
	
	public int getRootRecord() {
		return rootRecord;
	}
	public void setRootRecord(int rootRecord) {
		this.rootRecord = rootRecord;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
