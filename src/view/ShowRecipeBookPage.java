package view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
	@FXML private CheckBox checkBoxFavorite, cbShowFavorities;
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
				showFoundRecipes(allRecipes);
			} catch (SQLException e) {
				showErrorWindow("Something went wrong: " + e.getMessage());
				l.changeView("SearchPage");
			}
		}
	} 


	@FXML
	private void showNewRecipe(ActionEvent event) {
		if(event == null)
			displayRecipe(allRecipes.get(currentRecipeIndex));
		else if(((Button)event.getSource()).getId().equals("nextRecipeButton") && currentRecipeIndex < allRecipes.size() - 1)
			displayRecipe(allRecipes.get(++currentRecipeIndex));
		else if (((Button)event.getSource()).getId().equals("previousRecipeButton") && currentRecipeIndex > 0)
			displayRecipe(allRecipes.get(--currentRecipeIndex));
	}
	
	@FXML
	private void showFavorities(ActionEvent event) {
		for (UIEventListener l : listeners) {
			try {
				if(cbShowFavorities.isSelected()) {
					allRecipes = l.getUserFavorites();
					showFoundRecipes(allRecipes);
				} else {
					allRecipes = l.getRecipies();
					showFoundRecipes(allRecipes);
				}
			} catch (SQLException e) {
				showErrorWindow("Cannot display recipes: " + e.getMessage());
				l.changeView("searchPage");
			}
		}
	}

	public void showFoundRecipes(ArrayList<Recipe> foundRecipes) {
		if(foundRecipes == null || foundRecipes.size() == 0) {
			for (UIEventListener l : listeners) {
				l.changeView("SearchPage");
			}
			showErrorWindow("No recipes Found");
			return;
		}
		this.allRecipes = foundRecipes;
		this.currentRecipeIndex = 0;
		showNewRecipe(null);
	}
	
	private void displayRecipe(Recipe r) {
		try {
			resetGridPanes();
			checkIfFavorite(r);
		} catch (SQLException e) {
			showErrorWindow("Problem fetching recipe: " + e.getMessage() + "\nPlease try again.");
			for (UIEventListener l : listeners)
				l.changeView("searchRecipe");
			return;
		} catch (NullPointerException e) {
			showErrorWindow("Problem fetching recipe: Page broken\nPlease try again.");
			e.printStackTrace();
			for (UIEventListener l : listeners)
				l.changeView("SearchPage");
			return;
		}
		
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
	
	private void checkIfFavorite(Recipe currentrecipe) throws SQLException {
		for (UIEventListener l : listeners) {
			setStar(l.checkRecipeInUserFavorites(currentrecipe));
		}
	}

	@FXML
	private void blackOrYellowStar(ActionEvent event) throws SQLException {    	
		for (UIEventListener l : listeners) {
			if(checkBoxFavorite.isSelected()) {
				setStar(true);
				l.addRecipeToUserFavorites(allRecipes.get(currentRecipeIndex));
			}
			else {
				setStar(false);
				l.removeRecipeFromUserFavorites(allRecipes.get(currentRecipeIndex));
			}
		}
	}
	
	private void setStar(boolean favorite) {
		checkBoxFavorite.setSelected(favorite);
		yellowStar.setVisible(favorite);
		blackStar.setVisible(!favorite);
	}

	@FXML
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
