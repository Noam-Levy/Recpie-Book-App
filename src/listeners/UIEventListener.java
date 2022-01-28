package listeners;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

import exceptions.UserRegistrationException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.Recipe;
import view.Page;

public interface UIEventListener {

	ArrayList<Recipe> getRecipiesByIngredients(ObservableList<Node> ingredientsList) throws Exception;
	ArrayList<Recipe> getRecipiesByCuisine(String text) throws Exception;
	ArrayList<Recipe> getRecipesByName(String text) throws Exception;
	ArrayList<Recipe> getRecipies() throws SQLException;
	ArrayList<Recipe> getUserFavorites() throws SQLException;
	boolean checkRecipeInUserFavorites(Recipe r) throws SQLException;
	boolean removeRecipeFromUserFavorites(Recipe favoriteRecipe) throws SQLException;
	boolean addRecipeToUserFavorites(Recipe favoriteRecipe) throws SQLException;
	void setCurrentView(Page currentView);
	void changeView(String requestedView);
	void registerUser(String userName, String userPassword) throws UserRegistrationException, SQLException, NoSuchAlgorithmException, IOException;
	void userLogout();
	boolean userLogin(String userName, String password) throws SQLException, NoSuchAlgorithmException, IOException;
	void showFoundRecipes(ArrayList<Recipe> foundRecipes);
}
