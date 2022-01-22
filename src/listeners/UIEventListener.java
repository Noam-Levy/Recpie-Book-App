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

	ArrayList<Recipe> getRecipiesByIngredients(ObservableList<Node> ingredientsList);
	ArrayList<Recipe> getRecipiesByCuisine(String text);
	ArrayList<Recipe> getRecipieByName(String text);
	void setCurrentView(Page currentView);
	void changeView(String requestedView);
	void showRecipies(ArrayList<Recipe> foundRecipes);
	void registerUser(String userName, String userPassword) throws UserRegistrationException, SQLException, NoSuchAlgorithmException, IOException;
	void userLogout();
	boolean userLogin(String userName, String password) throws SQLException, NoSuchAlgorithmException, IOException;

}
