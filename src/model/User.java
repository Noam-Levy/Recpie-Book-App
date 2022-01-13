package model;

import java.util.Arrays;

import exceptions.UserRegistrationException;

public class User {
	
	private static int userIDGenerator = 1000;
	private char[] illegalCharacters = {'-','\'','\\','#','(',')',':','[',']','{','}'};
	
	private int userID;
	private String userName;
	private String password; // stored encrypted.
	
	public User() {
		this.userID = userIDGenerator;
		this.userName = "";
		this.password = "";
		userIDGenerator ++;
	}
	
	public User(int userID, String userName, String password, String APIKey) {
		this.userID = userID;
		this.userName = userName;
		this.password = Encrypt(password);
	}
	
	public boolean validatePassword(String password) throws UserRegistrationException {
		
		if(password.isBlank() || password.length() < 8)
			throw new UserRegistrationException("Password is too short. must be 8-16 characters long."); // code for password is too short
		if (password.length() > 16)
			throw new UserRegistrationException("Password is too long. must be 8-16 characters long."); // code for password is too long
		for (char c : illegalCharacters) {
			if(password.indexOf(c) != -1)
				throw new UserRegistrationException("Password cannot contain: " 
													+ Arrays.toString(illegalCharacters).replace("[", "").replace("]", ""));
		}
			return true;
	}

	private String Encrypt(String password) {
		//TODO
		return null;
	}
	

	public int getUserID() {
		return userID;
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
		this.password = Encrypt(password);
	}
}
