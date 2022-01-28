package model;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.UserRegistrationException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
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

	public boolean addToUserFavorites(Recipe favoriteRecipe) throws SQLException {
		return DBManager.getInstance().addRecipeToUserFavorites(loggedUser.getUserID()+"",favoriteRecipe.getRecipeID());	
	}

	public boolean removeFromUserFavorites(Recipe favoriteRecipe) throws SQLException {
		return DBManager.getInstance().removeRecipeFromUserFavorites(loggedUser.getUserID()+"",favoriteRecipe.getRecipeID());	
	}

	public boolean checkIfRecipeIsFavorite(Recipe favoriteRecipe) throws SQLException {
		return DBManager.getInstance().checkIfRecipeIsFavorite(loggedUser.getUserID()+"",favoriteRecipe.getRecipeID());	
	}

	public ArrayList<Recipe> getRecipeByName(String recipeName) throws Exception {
		ArrayList<Recipe> foundRecipes = DBManager.getInstance().searchRecipeByName(recipeName);
		if(foundRecipes == null)
			try {
				foundRecipes = RecipeFetcher.getInstance().searchRecipe(recipeName);
			} catch(Exception e) {
				return null;
			}
		return foundRecipes; 
	}

	public ArrayList<Recipe> getRecipeByCuisine(String cuisine) throws Exception {
		ArrayList<Recipe> foundRecipes;
		foundRecipes = DBManager.getInstance().searchRecipeByCuisine(cuisine);
		if(foundRecipes == null)
			try {
				foundRecipes = RecipeFetcher.getInstance().searchRecipeByCuisine(cuisine);
			} catch(Exception e) {
				return null;
			}
		return foundRecipes;
	}

	@SuppressWarnings("unchecked") // choice boxes are of type String
	public ArrayList<Recipe> getRecipesByIngredients(ObservableList<Node> ingredientsList) throws NoSuchAlgorithmException, IOException, Exception {
		int size = ingredientsList.size();
		Ingredient[] ingredients = new Ingredient[size];
		for (int i = 0; i < size; i++) {
			ingredients[i] = DBManager.getInstance().searchIngredient(((ChoiceBox<String>)ingredientsList.get(i)).getValue());
		}
		ArrayList<Recipe> foundRecipes = DBManager.getInstance().searchRecipeByIngredients(ingredients);
		if(foundRecipes.size() == 0)
			foundRecipes = RecipeFetcher.getInstance().searchRecipesByIngredients(ingredients);
		return foundRecipes;
	}

}
