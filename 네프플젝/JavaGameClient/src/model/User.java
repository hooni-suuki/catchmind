package model;

public class User {
	public String id;
	public String name;
	public String password;
	public String userimg;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setID(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getuserimg(){
		return userimg;
	}
	public void serUserimg(String userimg) {
		this.userimg = userimg;
	}	
}
