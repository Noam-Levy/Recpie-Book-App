package view;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import listeners.UIEventListener;
import model.Ingredient;
import model.Recipe;

public class SearchPage extends Page {


	@FXML private ToggleGroup TGsearchOptions;
	@FXML private Button addIngredientToSearchButton, searchButton;
	@FXML private VBox ingredientsSelector, ingredientsBox;
	@FXML private RadioButton searchByCuisine, searchByIngredients, searchByName;
	@FXML private TextField searchField;
	@FXML private Pane searchPage;
	
	@FXML
	protected void initialize() {
		ingredientsSelector.setVisible(false);
		searchButton.setDisable(true);
	}

	@FXML
	void searchByIngredients(ActionEvent event) {
		ingredientsSelector.setVisible(true);
		searchField.setDisable(true);
		searchButton.setDisable(false);
	}
	
	@FXML
	void searchByCuisineOrName(ActionEvent event) {
		ingredientsSelector.setVisible(false);
		searchField.setDisable(false);
		searchButton.setDisable(false);
	}
	
	@FXML
	void searchForRecipe(ActionEvent event) throws Exception {
		ArrayList<Recipe> foundRecipes;
		for (UIEventListener l : listeners) {
			if (searchByIngredients.isSelected())
				foundRecipes = l.getRecipiesByIngredients(ingredientsBox.getChildren());
			else if (searchField.getText().isBlank())
			{
				showErrorWindow("Please enter search data");
				return;
			}
			else {
				if (searchByCuisine.isSelected())
					foundRecipes = l.getRecipiesByCuisine(searchField.getText());
				else
					foundRecipes = l.getRecipieByName(searchField.getText());
			}
			// display all recipes found.
			l.chagneView("AllRecipesPage");
			l.showRecipies(foundRecipes);
		}
	}

   @SuppressWarnings("unchecked") // ingredientsBox contains ChoiceBox<Ingredient> objects only
	@FXML
    void addIngredientsRow(ActionEvent event) {
    	Iterator<Node> it = ingredientsBox.getChildren().iterator();
    	while(it.hasNext()) {
    		if (((ChoiceBox<Ingredient>)it.next()).getValue() == null)
    			return;
    	}
    	ChoiceBox<Ingredient> newBox = new ChoiceBox<Ingredient>();
    	newBox.setPrefWidth(ingredientsBox.getWidth());
    	ingredientsBox.getChildren().add(newBox);
    }


}

