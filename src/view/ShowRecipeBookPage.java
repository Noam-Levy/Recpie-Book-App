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
import javafx.scene.control.Control;
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
		else if(event.getSource().equals(nextRecipeButton))
			;//TODO
		else if (event.getSource().equals(previousRecipeButton))
			;//TODO
	}
	
	public void showFoundRecipes(ArrayList<Recipe> foundRecips) {
		this.allRecipes = foundRecips;
		this.currentRecipeIndex = 0;
		showNewRecipe(null);
	}

	private void showRecipe(Recipe r) {
		lbCookTime.setText(r.getCookTime()+"");
		lbCuisine.setText(getAllCuisine(r));
		lbRecipeName.setText(r.getRecipieName()+"");
		lbServing.setText(r.getServings()+"");
		Ingredient currentIngredient;
		int ingredientsSize = r.getIngrediants().size();
		for (int i = 1; i < ingredientsSize; i++) {
			currentIngredient = r.getIngrediants().get(i-1);
			GPingredient.addRow(i, new Label(currentIngredient.getAmount()+""), new Label(currentIngredient.getMeasurement()),
					new Label(currentIngredient.getName()),new Label(currentIngredient.getFrom()));	
		}
		GPingredient.getChildren().forEach(node -> node.maxWidth(Control.USE_COMPUTED_SIZE));
		r.getInstructions().forEach((step,instruction) -> GPinstruction.addRow(step, new Label(step.toString()), new Label(instruction)));
	}

	private String getAllCuisine(Recipe r) {
		StringBuffer s = new StringBuffer();
		for (String c : r.getCuisine().values()) {
			s.append(c+",");
		}
		return s.toString();
	}
}
