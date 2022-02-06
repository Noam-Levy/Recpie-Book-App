package controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import exceptions.UserRegistrationException;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import listeners.ModelEventListener;
import listeners.UIEventListener;
import model.Model;
import model.Recipe;
import view.MenuPage;
import view.Page;
import view.ShowRecipeBookPage;

public class Controller implements UIEventListener, ModelEventListener {

	private Page currentView;
	private MenuPage menuPage;
	private Model model;

	public Controller(Page view, Model model) {
		this.model = model;
		this.model.addListener(this);
		this.currentView = view;
		this.currentView.addListener(this);
		this.menuPage = new MenuPage();
	}

	public void start(Stage primaryStage) throws IOException {
		URL location = getClass().getResource("/view/menu.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Parent root = (Pane)fxmlLoader.load();
		this.menuPage = (MenuPage)fxmlLoader.getController();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public boolean userLogin(String userName, String password) throws NoSuchAlgorithmException, SQLException, IOException {
		boolean success = model.loginUser(userName, password);
		if(success)	{
			changeView("AllRecipesPage");
			menuPage.enableMenuButtons();
			menuPage.setUserLabel(userName);
		}
		return success;
	}

	@Override
	public void registerUser(String userName, String userPassword) throws NoSuchAlgorithmException, UserRegistrationException, SQLException, IOException {
		boolean success = model.registerUser(userName,userPassword);
		if(success)
			userLogin(userName, userPassword);
	}

	@Override
	public void userLogout() {
		model.logoutUser();
	}

	@Override
	public ArrayList<Recipe> getRecipiesByIngredients(ObservableList<Node> ingredientsList) throws Exception {
		ingredientsList.removeAll(Collections.singleton(null));
		return model.getRecipesByIngredients(ingredientsList);
	}

	@Override
	public ArrayList<Recipe> getRecipiesByCuisine(String text) throws Exception {
		return model.getRecipeByCuisine(text);
	}

	@Override
	public ArrayList<Recipe> getRecipesByName(String text) throws Exception {
		return model.getRecipeByName(text);
	}

	@Override
	public ArrayList<Recipe> getRecipies() throws SQLException, InterruptedException {
		return model.getAllrecipes();
	}

	@Override
	public void showFoundRecipes(ArrayList<Recipe> foundRecipes) {
		if (!(currentView instanceof ShowRecipeBookPage)) {
			changeView("SearchPage");
			showErrorMessage("Something went wrong. Please try again");
			return;
		}
		((ShowRecipeBookPage)this.currentView).showFoundRecipes(foundRecipes);
	}
	
	@Override
	public boolean addRecipeToDB(Recipe r) throws SQLException {
		return model.addRecipeToDB(r);
	}
	
	@Override
	public boolean savePhotoRecipe(Image image) throws IOException {
		return model.savePhotoRcipe(image);
	}


	@Override
	public boolean addRecipeToUserFavorites(Recipe favoriteRecipe) throws SQLException, InterruptedException {
		return model.addToUserFavorites(favoriteRecipe);
	}

	@Override
	public boolean removeRecipeFromUserFavorites(Recipe favoriteRecipe) throws SQLException {
		return model.removeFromUserFavorites(favoriteRecipe);
	}


	@Override
	public boolean checkRecipeInUserFavorites(Recipe r) throws SQLException {
		return model.checkIfRecipeIsFavorite(r);
	}
	
	@Override
	public ArrayList<Recipe> getUserFavorites() throws SQLException, InterruptedException {
		return model.getUserFavorites();
	}


	@Override
	public void setCurrentView(Page currentView) {
		this.currentView = currentView;
	}

	@Override
	public void changeView(String requestedView) {
		/* Allows for other view controllers to request view change.
		   I.E - after searching for recipes, 
		   the view will change automatically to ShowRecipeBookPage in order to display found recipes 
		 */
		try {
			menuPage.switchPane(requestedView);
		} catch (IllegalArgumentException e) {
			showErrorMessage("We encoutered a problem. please try again.");
		}
	}

	@Override
	public void showErrorMessage(String string) {
		Page.showErrorWindow(string);
	}
}