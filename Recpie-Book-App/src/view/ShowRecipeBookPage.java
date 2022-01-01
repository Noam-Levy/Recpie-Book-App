package view;

import java.util.ArrayList;

import model.Recipe;

public class ShowRecipeBookPage extends Page {

	public void showRecipes(ArrayList<Recipe> foundRecipes) {
		if (foundRecipes.size() == 0)
		{
			showErrorWindow("No recipes found");
			return;
		}
		// fill recipe pages...
	}



}
