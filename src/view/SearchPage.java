package view;

import java.sql.SQLException;
import java.util.ArrayList;
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
import model.DBManager;
import model.Ingredient;
import model.Recipe;

public class SearchPage extends Page{


	@FXML private ToggleGroup TGsearchOptions;
	@FXML private Button addIngredientToSearchButton, searchButton;
	@FXML private VBox ingredientsBox;
	@FXML private Pane ingredientsSelector;   
	@FXML private RadioButton searchByCuisine, searchByIngredients, searchByName;
	@FXML private TextField searchField;
	@FXML private Pane searchPage;
	@FXML private ChoiceBox<String> cmbINGRED;


	@FXML
	protected void initialize() {
		ingredientsSelector.setVisible(false);
		searchButton.setDisable(true);
		ArrayList<Ingredient> ingredientList;
		try {
			ingredientList = DBManager.getInstance().getIngredients();
			for (Ingredient i : ingredientList) {
				cmbINGRED.getItems().add(i.getName());
			}
		} catch (SQLException e) {
			showErrorWindow("Something happned. please try again." + e.getMessage());
			searchByCuisineOrName(null);
		}
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

	@SuppressWarnings("unchecked") // Choice boxes are of type string
	@FXML
	void searchForRecipe(ActionEvent event) {
		ArrayList<Recipe> foundRecipes;
		for (UIEventListener l : listeners) {
			if (searchByIngredients.isSelected()) {
				if(((ChoiceBox<String>)ingredientsBox.getChildren().get(0)).getValue() == null) {
					showErrorWindow("Please select as least one ingredient");
					return;
				}
				foundRecipes = l.getRecipiesByIngredients(ingredientsBox.getChildren());	
			}
			else if (searchField.getText().isBlank()) {
				showErrorWindow("Please enter search data");
				return;
			}
			else {
				if (searchByCuisine.isSelected())
					foundRecipes = l.getRecipiesByCuisine(searchField.getText());
				else
					foundRecipes = l.getRecipesByName(searchField.getText());
			}
			// display all recipes found.
			l.changeView("AllRecipesPage");
			l.showFoundRecipes(foundRecipes);
		}
	}

	@SuppressWarnings("unchecked") // choice boxes of type String
	@FXML
	void addIngredientsRow(ActionEvent event) {
		for (Node c : ingredientsBox.getChildren()) {
			if (((ChoiceBox<String>)c).getValue() == null)
				return;
		}
		ChoiceBox<String> cmbINGREDClone = new ChoiceBox<String>();
		for (String s : cmbINGRED.getItems()) {
			cmbINGREDClone.getItems().add(s);	
		}
		cmbINGREDClone.setPrefSize(cmbINGRED.getPrefWidth(), cmbINGRED.getPrefHeight());
		ingredientsBox.getChildren().add(cmbINGREDClone);
	}
}

