package controller;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import listeners.ModelEventListener;
import listeners.UIEventListener;
import model.Model;
import model.Recipe;
import model.User;
import view.MenuPage;
import view.Page;
import view.ShowRecipeBookPage;

public class Controller implements UIEventListener, ModelEventListener {
	
	private Page currentView;
	private MenuPage menuPage;
	private Model model;
		
	public Controller(Page view, Model model) {
		this.model = model;
		this.currentView = view;
		this.currentView.addListener(this);
		this.menuPage = new MenuPage();
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
			currentView.showErrorWindow("Something went wrong. Please try again");
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
		   I.E - after searching for recipes, the view will change automatically to ShowRecipeBookPage in order to display found recipes */
		menuPage.switchPane(requestedView);
	}




}
