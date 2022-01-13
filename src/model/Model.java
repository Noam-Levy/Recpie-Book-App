package model;

import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.UserRegistrationException;
import listeners.ModelEventListener;

public class Model {
	
	private User loggedUser;
	private ArrayList<ModelEventListener> listeners;
	
	public Model() {
		listeners = new ArrayList<ModelEventListener>();
	}
	
	public void addListener(ModelEventListener listener) {
		listeners.add(listener);
	}
	
	public void registerUser(String userName, String userPassword) throws UserRegistrationException, SQLException {
		if(checkUserExsists(userName))
			throw new UserRegistrationException("Username already exsists");
		User user = new User();
		user.validatePassword(userPassword); // throws UserRegistrationException if fails
		user.setUserName(userName);
		user.setPassword(userPassword);
		if(addUserToDB(user)); // throws SQLException if fails
			this.loggedUser = user;
	}
	
	public boolean checkUserExsists(String userName) throws SQLException {
		return DBManager.getInstance().searchUser(userName);
	}

	public boolean addUserToDB(User user) throws UserRegistrationException {
		try {
			return DBManager.getInstance().addUser(user);
		} catch (SQLException e) {
			throw new UserRegistrationException("Could not regiser user: " + e.getMessage());
		}
	}
	
	public ArrayList<Recipe> getUserFavorites() throws SQLException {
		return DBManager.getInstance().getUserFavorites(loggedUser.getUserID());
	}


}
