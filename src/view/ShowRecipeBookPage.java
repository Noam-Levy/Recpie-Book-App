package view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import listeners.UIEventListener;
import model.Ingredient;
import model.Recipe;

public class ShowRecipeBookPage extends Page implements Initializable {

	@FXML private GridPane GPingredient,GPinstruction;
	@FXML private ImageView blackStar,yellowStar;
	@FXML private CheckBox checkBoxFavorite;
	@FXML private Label lbCookTime,lbCuisine,lbRecipeName,lbServing;
	@FXML private Button nextRecipeButton, previousRecipeButton;
	      private ArrayList<Recipe> allRecipes;
	      private int currentRecipeIndex;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currentRecipeIndex = 0;
		for (UIEventListener l : listeners) {
			try {
				allRecipes = l.getRecipies();
			} catch (SQLException e) {
				showErrorWindow("Something went wrong: " + e.getMessage());
				l.changeView("searchRecipe");
			}
		}
		showNewRecipe(null);
	}

	@FXML
	void blackOrYellowStar(ActionEvent event) {    	
		if(checkBoxFavorite.isSelected()) {
			yellowStar.setVisible(true);
			blackStar.setVisible(false);
		}
		else {
			yellowStar.setVisible(false);
			blackStar.setVisible(true);
		}
	}

	@FXML
	public void showNewRecipe(ActionEvent event) {
		if(event == null)
			showRecipe(allRecipes.get(currentRecipeIndex));
		else if(((Button)event.getSource()).getId().equals("nextRecipeButton") && currentRecipeIndex < allRecipes.size() - 1)
			showRecipe(allRecipes.get(++currentRecipeIndex));
		else if (((Button)event.getSource()).getId().equals("previousRecipeButton") && currentRecipeIndex > 0)
			showRecipe(allRecipes.get(--currentRecipeIndex));
	}
	
	public void showFoundRecipes(ArrayList<Recipe> foundRecipes) {
		if(foundRecipes == null || foundRecipes.size() == 0) {
			for (UIEventListener l : listeners) {
				l.changeView("searchRecipe");
			}
			showErrorWindow("No recipes Found");		
		}
		this.allRecipes = foundRecipes;
		this.currentRecipeIndex = 0;
		showNewRecipe(null);
	}

	private void showRecipe(Recipe r) {
		resetGridPanes();
		lbCookTime.setText(r.getCookTime()+"");
		lbCuisine.setText(getAllCuisine(r));
		lbRecipeName.setText(r.getRecipieName()+"");
		lbServing.setText(r.getServings()+"");
		Ingredient currentIngredient;
		
		int ingredientsSize = r.getIngrediants().size();
		for (int i = 1; i <= ingredientsSize; i++) {
			currentIngredient = r.getIngrediants().get(i-1);
			Label lbAmount = new Label(currentIngredient.getAmount()+"");
			Label lbMeasurement = new Label(currentIngredient.getMeasurement());
			Label lbIngredientName = new Label(currentIngredient.getName());
			Label lbForm = new Label(currentIngredient.getFrom());
			lbAmount.setWrapText(true);
			lbMeasurement.setWrapText(true);
			lbIngredientName.setWrapText(true);
			lbForm.setWrapText(true);
			GPingredient.addRow(i, lbAmount, lbMeasurement, lbIngredientName, lbForm);	
		}
		r.getInstructions().forEach((step,instruction) -> { 
			Label lbStepNum = new Label(step.toString());
			Label lbInstruction =  new Label(instruction);
			lbStepNum.setWrapText(true);
			lbInstruction.setWrapText(true);
			GPinstruction.addRow(step, lbStepNum, lbInstruction);
		});
	}

	private void resetGridPanes() {
		GPingredient.getChildren().retainAll(GPingredient.getChildren().get(0),GPingredient.getChildren().get(1),
											 GPingredient.getChildren().get(2),GPingredient.getChildren().get(3));
		GPinstruction.getChildren().retainAll(GPinstruction.getChildren().get(0),GPinstruction.getChildren().get(1));
	}

	private String getAllCuisine(Recipe r) {
		StringBuffer s = new StringBuffer();
		for (String c : r.getCuisine().values()) {
			s.append(c+",");
		}
		return s.toString();
	}
}
