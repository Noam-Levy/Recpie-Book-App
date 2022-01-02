package view;

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
import model.Ingredient;

public class SearchPage {


	@FXML private ToggleGroup TGsearchOptions;
	@FXML private Button addIngredientToSearchButton, searchButton;
	@FXML private VBox ingredientsSelector, ingredientsBox;
	@FXML private RadioButton searchByCuisine, searchByIngredients, searchByName;
	@FXML private TextField searchField;
	@FXML private Pane searchPage;
	
	 @FXML
	    private ChoiceBox<String> cmbINGRED;
 	



	@FXML
	protected void initialize() {
		ingredientsSelector.setVisible(false);
		searchButton.setDisable(true);

		cmbINGRED.getItems().add("1");
    	cmbINGRED.getItems().add("2");
    	cmbINGRED.getItems().add("3");
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
	void searchForRecipe(ActionEvent event) {
		// TO DO
	}

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
    	
    	
//    	Iterator<Node> it = ingredientsBox.getChildren().iterator();
//    	while(it.hasNext()) {
//    		if ((ChoiceBox)it.next(). == null)
//    			return;
//    	}
//    	ChoiceBox<Ingredient> newBox = new ChoiceBox<Ingredient>();
//    	newBox.setPrefWidth(ingredientsBox.getWidth());
//    	ingredientsBox.getChildren().add(newBox);
    }


}

