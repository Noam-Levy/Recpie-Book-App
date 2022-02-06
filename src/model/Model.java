package model;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.parser.ParseException;
import exceptions.UserRegistrationException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import listeners.ModelEventListener;

public class Model {

	private User loggedUser;
	private ArrayList<ModelEventListener> listeners;

	private ArrayList<Recipe> allRecipes, userFavorites;

	public Model() throws SQLException, InterruptedException {
		listeners = new ArrayList<ModelEventListener>();
		allRecipes = getAllrecipes();
		userFavorites = getUserFavorites();
	}

	public void addListener(ModelEventListener listener) {
		listeners.add(listener);
	}

	public boolean loginUser(String userName, String password) throws SQLException, NoSuchAlgorithmException, IOException {
		User match = DBManager.getInstance().getUser(userName);
		if(match == null || !PasswordManager.getInstance().checkPassowrd(password, match.getPassword()))
			return false;
		try {
			this.loggedUser = match;
			userFavorites = getUserFavorites();
		} catch (SQLException | InterruptedException e) {
			return false;
		}
		return true;
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

	public ArrayList<Recipe> getAllrecipes() throws SQLException, InterruptedException {
		if (this.allRecipes == null || this.allRecipes.isEmpty())
			allRecipes = DBManager.getInstance().showAllRecipies();
		return this.allRecipes;
	}

	public boolean addRecipeToDB(Recipe newRecipe) {
		boolean addedToDB = false;
		try {
			addedToDB = DBManager.getInstance().addRecipe(newRecipe);
		} catch(SQLException e) {
			return false;
		}
		if(addedToDB)
			allRecipes.add(newRecipe);
		return addedToDB;
	}

	public ArrayList<Recipe> getUserFavorites() throws SQLException, InterruptedException {
		if(loggedUser == null)
			return null;
		if (this.userFavorites == null || this.userFavorites.isEmpty()) 
			userFavorites = DBManager.getInstance().getUserFavorites(loggedUser.getUserID());
		return userFavorites;
	}	

	public boolean addToUserFavorites(Recipe favoriteRecipe) throws SQLException, InterruptedException { 
		if(favoriteRecipe == null || this.loggedUser == null)
			return false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DBManager.getInstance().addRecipeToUserFavorites(loggedUser.getUserID()+"",favoriteRecipe.getRecipeID());
				} catch (SQLException e) {}
			}
		}).start();
		return this.userFavorites.add(favoriteRecipe);	
	}

	public boolean removeFromUserFavorites(Recipe favoriteRecipe) throws SQLException {
		if(favoriteRecipe == null)
			return false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DBManager.getInstance().removeRecipeFromUserFavorites(loggedUser.getUserID()+"",favoriteRecipe.getRecipeID());
				} catch (SQLException e) {}
			}
		}).start();
		return userFavorites.remove(favoriteRecipe);
	}

	public boolean checkIfRecipeIsFavorite(Recipe favoriteRecipe) throws SQLException {
		if(favoriteRecipe == null || this.userFavorites == null)
			return false;
		return userFavorites.contains(favoriteRecipe);
	}

	public ArrayList<Recipe> getRecipeByName(String recipeName) throws Exception {
		/*
		 * Searches for recipes with similar name in the DB and against spoonacular API
		 */
		ArrayList<Recipe> foundRecipes = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// will not add nulls - checked in RecipeFetcher
					ArrayList<Recipe> foundByAPI = RecipeFetcher.getInstance().searchRecipe(recipeName);
					for (Recipe r : foundByAPI) {
						executor.execute(new Runnable() {
							@Override
							public void run() {
								if(addRecipeToDB(r))
									foundRecipes.add(r);
							}
						});
					}
				} catch (IOException | InterruptedException | ParseException | SQLException | NoSuchAlgorithmException e) {
					return;
				}
			}
		});
		t1.run();
		executor.shutdown();
		for (Recipe r : allRecipes) {
			if (r.getRecipeName().equalsIgnoreCase(recipeName) || r.getRecipeName().contains(recipeName))
				foundRecipes.add(r);
		}
		t1.join();
		return foundRecipes; 
	}

	public ArrayList<Recipe> getRecipeByCuisine(String cuisine) throws Exception {
		/*
		 * Searches for recipes from a given cuisine in the DB and against spoonacular API
		 */
		ArrayList<Recipe> foundRecipes = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// will not add nulls - checked in RecipeFetcher
					ArrayList<Recipe> foundByAPI = RecipeFetcher.getInstance().searchRecipeByCuisine(cuisine);
					for (Recipe r : foundByAPI) {
						executor.execute(new Runnable() {
							@Override
							public void run() {
								if(addRecipeToDB(r))
									foundRecipes.add(r);
							}
						});
					}
				} catch (IOException | InterruptedException | ParseException | SQLException | NoSuchAlgorithmException e) {
					return;
				}
			}
		});
		t1.run();
		executor.shutdown();
		for (Recipe r : allRecipes) {
			if(r.getCuisine().containsValue(cuisine))
				foundRecipes.add(r);
		}
		t1.join();
		return foundRecipes; 
	}

	@SuppressWarnings("unchecked") // choice boxes are of type String
	public ArrayList<Recipe> getRecipesByIngredients(ObservableList<Node> ingredientsList) throws NoSuchAlgorithmException, IOException, Exception {
		/*
		 * Simultaneously searches for recipes that include one or more ingredients from a given list 
		 * in the DB and against spoonacular API 
		 */
		int size = ingredientsList.size();
		Ingredient[] ingredients = new Ingredient[size];
		for (int i = 0; i < size; i++) {
			ingredients[i] = DBManager.getInstance().searchIngredient(((ChoiceBox<String>)ingredientsList.get(i)).getValue());
		}

		ArrayList<Recipe> foundRecipes = new ArrayList<>();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// will not add nulls - checked in DBManager.searchRecipeByIngredients()
					foundRecipes.addAll(DBManager.getInstance().searchRecipeByIngredients(ingredients));
				} catch (Exception e) {}
			}
		});

		ExecutorService executor = Executors.newFixedThreadPool(5);
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// will not add nulls - checked in RecipeFetcher
					ArrayList<Recipe> foundByAPI = RecipeFetcher.getInstance().searchRecipesByIngredients(ingredients);
					for (Recipe r : foundByAPI) {
						executor.execute(new Runnable() {
							@Override
							public void run() {
								if(addRecipeToDB(r))
									foundRecipes.add(r);
							}
						});
					}
				} catch (IOException | InterruptedException | ParseException | SQLException | NoSuchAlgorithmException e) {
					return;
				}
			}
		});
		
		t1.start(); 	t2.start(); 		
		executor.shutdown();
		t1.join();		t2.join();

		return foundRecipes;
	}
}
