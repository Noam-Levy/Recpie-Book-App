package listeners;

import java.util.ArrayList;

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

}
