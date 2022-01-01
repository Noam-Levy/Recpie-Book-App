package listeners;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import model.Recipe;

public interface UIEventListener {

	ArrayList<Recipe> getRecipiesByIngredients(ObservableList<Node> ingredientsList);
	ArrayList<Recipe> getRecipiesByCuisine(String text);
	ArrayList<Recipe> getRecipieByName(String text);
	void chagneView(String string);
	void showRecipies(ArrayList<Recipe> foundRecipes);

}
