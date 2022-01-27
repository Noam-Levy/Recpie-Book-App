package view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import listeners.UIEventListener;
import model.DBManager;
import model.Ingredient;
import model.Recipe;

public class AddRecipePage extends Page implements Initializable {

	@FXML private GridPane GPingredient;
	@FXML private GridPane GPinstruction;
	@FXML private Button btnAddRowIngredient;
	@FXML private Button btnAddRowInstruction;
	@FXML private ChoiceBox<String> cmbUnit;
	@FXML private TextField txfRecipieCookTime, txfRecipieCuisine, txfRecipieName, txfServings, tfAmount;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addListenerToTextField(tfAmount);
		try {
			ArrayList<String> measurements = DBManager.getInstance().getMeasurements();
			for (String s : measurements) {
				cmbUnit.getItems().add(s);
			}
		} catch (SQLException e) {
			showErrorWindow("Something went wrong. please try again");
			for (UIEventListener l : listeners) {
				l.changeView("allRecipesPage");
			}
		}	
	}

	@FXML
	void addRowIngredient(ActionEvent event) {
		// checks if all available ingredients rows are filled before allowing to add more
		if (!checkGPingredient())
			return;
		ChoiceBox<String> cmbUnitClone = new ChoiceBox<String>();
		for (String s : cmbUnit.getItems()) {
			cmbUnitClone.getItems().add(s);	
		}
		cmbUnitClone.setPrefSize(cmbUnit.getPrefWidth(), cmbUnit.getPrefHeight());
		TextField newTF = new TextField();
		addListenerToTextField(newTF);
		GPingredient.addRow(GPingredient.getRowCount(), newTF,cmbUnitClone,new TextField(), new TextField());
	}

	@FXML
	void addRowInstruction(ActionEvent event) {
		// checks if all available instructions rows are filled before allowing to add more
		if (!checkGPinstruction())
			return;
		Label l= new Label(GPinstruction.getRowCount()+"");
		l.setStyle("-fx-text-fill: black");
		GPinstruction.addRow(GPinstruction.getRowCount(),l,new TextField());
	}

	@FXML
	void saveRecipie(ActionEvent event) throws SQLException {
		if (txfRecipieName.getText().isEmpty() ||txfRecipieCookTime.getText().isEmpty() || txfServings.getText().isEmpty()) 
			return;
		if (!checkIfGPIsComplete()) 
			return ;
		createRecipe();
	}

	private void addListenerToTextField(TextField tf) {
		/*
		 * This function adds ChangeListener to all "amount" text fields generated
		 * so the user will be able to type numbers only.
		 */
		tf.textProperty().addListener(new ChangeListener<String> () {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.matches("\\d*")) {
					tf.setText(newValue.replaceAll("[^\\d]", ""));
				}	
			}
		});
	}

	private void createRecipe() throws SQLException {
		String recipeName  = txfRecipieName.getText();
		String recipeCookTime  = txfRecipieCookTime.getText();
		String recipeServing  = txfServings.getText();
		String recipeCuisine  = txfRecipieCuisine.getText();
		ArrayList<Ingredient> recipeIngredients = createRecipeIngredient();
		HashMap<Integer,String> recipeInstructions = createRecipeInstructions();
		
		Recipe r = new Recipe("",recipeName,Integer.parseInt(recipeCookTime),Integer.parseInt(recipeServing));
		r.setIngredients(recipeIngredients);
		r.setInstructions(recipeInstructions);
		String cuisineID = DBManager.getInstance().getCuisineID(recipeCuisine);
		r.addCuisine(cuisineID, recipeCuisine);
		DBManager.getInstance().addRecipe(r);
	}

	private HashMap<Integer, String> createRecipeInstructions() {
		HashMap<Integer, String> instructions = new HashMap<>();
		for (int k = 1; k < GPinstruction.getRowCount(); k++) {
			String stepK  = ((TextField)getNodeByCoordinate(GPinstruction,k,1)).getText();
			instructions.put(k, stepK);
		}
		return instructions;
	}

	private ArrayList<Ingredient> createRecipeIngredient() throws SQLException {
		ArrayList<Ingredient> recipeIngredient = new ArrayList<Ingredient>();
		for (int i = 1; i < GPingredient.getRowCount();  i++) {
			recipeIngredient.add(ingredientFromIndex(i));
		}
		return recipeIngredient;
	}

	private Ingredient ingredientFromIndex(int index) throws SQLException {
		Ingredient ingredient;
		String name = getNodeText(getNodeByCoordinate(GPingredient,index,2));
		String unit = getNodeText(getNodeByCoordinate(GPingredient,index,1));
		String form = getNodeText(getNodeByCoordinate(GPingredient,index,3));
		float amount = Float.parseFloat(getNodeText(getNodeByCoordinate(GPingredient,index,0)));

		ingredient = DBManager.getInstance().searchIngredient(name);
		if(ingredient == null)	
			ingredient = DBManager.getInstance().addIngredient(name);

		ingredient.setAmount(amount);
		ingredient.setFrom(form);
		ingredient.setMeasurement(unit);
		return ingredient ;
	}

	private Node getNodeByCoordinate (GridPane gridPane , int row, int column) {
		Node n=  gridPane.getChildren().get(column + gridPane.getColumnCount()*row);
		return n;
	}

	@SuppressWarnings("unchecked") // choice boxes are of type String
	private String getNodeText(Node n) {
		try {
			return ((TextField)n).getText().toString();
		} catch(ClassCastException e) {}
		return ((ChoiceBox<String>)n).getValue().toString();
	}

	private boolean checkIfGPIsComplete() {
		return checkGPingredient()&&checkGPinstruction();
	}

	private boolean checkGPingredient() {
		for (int i = 1; i < GPingredient.getRowCount(); i++) {
			for (int j = 0; j < 3; j++) {
				if(getNodeText(getNodeByCoordinate(GPingredient,i,j)).isBlank())
					return false;
			}
		}
		return true;
	}

	private boolean checkGPinstruction() {
		for (int i = 1; i < GPinstruction.getRowCount(); i++) {
			if(getNodeByCoordinate(GPinstruction,i,1)==null)
				return false;
		}

		return true;
	}

}
