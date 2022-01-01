package model;

public class User {
	
	private String userID;
	private String userName;
	private String password; // stored hashed.
	private String APIKey; // stored hashed.
	
	public User(String userID, String userName, String password, String APIKey) {
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.APIKey = APIKey;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAPIKey() {
		return APIKey;
	}

	public void setAPIKey(String aPIKey) {
		APIKey = aPIKey;
	}

}
