/*
@name: Gabriel Jucá
@date: 21st Nov 2021
User.java
 */
package ie.cct.travelappbackend.model;

//User class that will be the model of every user object

public class User {
	
	//Every user has a username and password and variables
	private String username;
	private String password;
	
	public User() {
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
