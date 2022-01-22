package controller;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import exceptions.UserRegistrationException;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	public void userLogout() {
		model.logoutUser();
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
	public ArrayList<Recipe> getRecipiesByIngredients(ObservableList<Node> ingredientsList) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Recipe> getRecipiesByCuisine(String text) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ArrayList<Recipe> getRecipieByName(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showRecipies(ArrayList<Recipe> foundRecipes) {
		if (!(currentView instanceof ShowRecipeBookPage))
		{
			Page.showErrorWindow("Something went wrong. Please try again");
			return;
		}	
		((ShowRecipeBookPage)currentView).showRecipes(foundRecipes);
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
