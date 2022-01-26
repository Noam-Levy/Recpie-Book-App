package model;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.UserRegistrationException;
import listeners.ModelEventListener;

public class Model {
	
	private User loggedUser;
//	private RecipeFetcher recipeFetcher; //declaration
	private ArrayList<ModelEventListener> listeners;
	
	public Model() {
		listeners = new ArrayList<ModelEventListener>();
	}
	
	public void addListener(ModelEventListener listener) {
		listeners.add(listener);
	}
	
	public boolean loginUser(String userName, String password) throws SQLException, NoSuchAlgorithmException, IOException {
		User match = DBManager.getInstance().getUser(userName);
		if(match == null || !PasswordManager.getInstance().checkPassowrd(password, match.getPassword()))
			return false;
		else {
			this.loggedUser = match;
			return true;
		}
	}
	
	public void logoutUser() {
		this.loggedUser = null;
	}
	
	public boolean registerUser(String userName, String userPassword) throws UserRegistrationException, SQLException, NoSuchAlgorithmException {
		if(checkUserExsists(userName))
			throw new UserRegistrationException("Username already exsists");
		User user = new User();
		user.validatePassword(userPassword); // throws UserRegistrationException if fails
		user.setUserName(userName);
		try {
			user.setPassword(userPassword); // throws NoSuchAlgorithmException if fails
		} catch (IOException e) {
			for (ModelEventListener l : listeners) {
				l.showErrorMessage(e.getMessage());
			}
		}
		if(addUserToDB(user)) // throws SQLException if fails
			return true;
		return false;
	}
	
	public boolean checkUserExsists(String userName) throws SQLException, NoSuchAlgorithmException {
		return DBManager.getInstance().searchUser(userName);
	}

	public boolean addUserToDB(User user) throws UserRegistrationException, NoSuchAlgorithmException {
		try {
			return DBManager.getInstance().addUser(user);
		} catch (SQLException e) {
			throw new UserRegistrationException("Could not regiser user: " + e.getMessage());
		}
	}
	
	public ArrayList<Recipe> getUserFavorites() throws SQLException {
		return DBManager.getInstance().getUserFavorites(loggedUser.getUserID());
	}

	
	
	public ArrayList<Recipe> getRecipeByName(String recipeName) throws Exception {
		 return RecipeFetcher.getInstance().searchRecipe(recipeName);
		
	}
	public ArrayList<Recipe> getRecipeByCuisine(String cuisine) throws Exception {
		return	DBManager.getInstance().searchRecipeByCuisine(cuisine);
		 
		
	}

}
