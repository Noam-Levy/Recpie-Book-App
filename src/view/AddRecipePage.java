package view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.DBManager;
import model.Ingredient;

public class AddRecipePage extends Page implements Initializable {

	@FXML private GridPane GPingredient;
	@FXML private GridPane GPinstruction;
	@FXML private Button btnAddRowIngredient;
	@FXML private Button btnAddRowInstruction;
	@FXML private ChoiceBox<String> cmbUnit;
	@FXML private TextField txfRecipieCookTime, txfRecipieCuisine, txfRecipieName ,  txfServings;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			ArrayList<String> measurements = DBManager.getInstance().getMeasurements();
			for (String s : measurements) {
				cmbUnit.getItems().add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@FXML
	void addRowIngredient(ActionEvent event) {
		// checks if all opened ingredients rows are filled before allowing to add more
		int allIndexes = GPingredient.getRowCount()* GPingredient.getColumnCount();
		if (!checkGPingredient(4,allIndexes))
			return;
		// create new choice box and add items. NEEDS TO BE CHAGNED FROM STRING TO INGREDIENT
		ChoiceBox<String> cmbUnitClone = new ChoiceBox<String>();
		for (String s : cmbUnit.getItems()) {
			cmbUnitClone.getItems().add(s);	
		}
		cmbUnitClone.setPrefSize(cmbUnit.getPrefWidth(), cmbUnit.getPrefHeight());
		GPingredient.addRow(GPingredient.getRowCount(), new TextField(),cmbUnitClone,new TextField(), new TextField());
	}

	@FXML
	void addRowInstruction(ActionEvent event) {
		// checks if all opened instructions rows are filled before allowing to add more
		int allIndexes = GPinstruction.getRowCount()* GPinstruction.getColumnCount();
		if (!checkGPinstruction(0,allIndexes))
			return;
		Label l= new Label(GPinstruction.getRowCount()+"");
		l.setStyle("-fx-text-fill: black");
		GPinstruction.addRow(GPinstruction.getRowCount(),l,new TextField());
	}

	@FXML
	void saveRecipie(ActionEvent event) throws SQLException {
		if (txfRecipieName.getText().isEmpty() ||txfRecipieCookTime.getText().isEmpty() 
				|| txfServings.getText().isEmpty()|| txfRecipieCuisine.getText().isEmpty() ) 
			return;
		
		if (!checkIfGPIsComplete()) 
			return ;
	
		createRecipe();

	}

	
	
	
	private void createRecipe() throws SQLException {
		String recipeName  = txfRecipieName.getText();
		String recipeCookTime  = txfRecipieCookTime.getText();
		String recipeServing  = txfServings.getText();
		String recipeCuisine  = txfRecipieCuisine.getText();
		
		System.out.printf("The recipe name is: %s \nCook time is : %s "
				+ "\nCuisine type is : %s \nServing amount is : %s \n",recipeName,recipeCookTime,recipeServing,recipeCuisine);
		
		
		ArrayList<Ingredient> recipeIngredient = createRecipeIngredient();
		printArrayList(recipeIngredient);
		
		HashMap<Integer,String> recipeInstraction = createRecipeInstraction();
		printHashMap(recipeInstraction);
		
		
			
	}
	
	
	
	private void printHashMap(HashMap<Integer, String> recipeInstraction) {
		recipeInstraction.forEach((step, instraction) -> System.out.println("step "+ step + ": " + instraction));
	}

	private HashMap<Integer, String> createRecipeInstraction() {
		HashMap<Integer, String> instraction = new HashMap<>();
		
		for (int k = 1; k < GPinstruction.getRowCount(); k++) {
			String stepK  = ((TextField)getNodeByCoordinate(GPinstruction,k,1)).getText().toString();;
			instraction.put(k, stepK);
		}
		return instraction;
	}

	
	private ArrayList<Ingredient> createRecipeIngredient() throws SQLException {
		ArrayList<Ingredient> recipeIngredient = new ArrayList<Ingredient>();
		
		for (int i = 1; i <GPingredient.getRowCount();  i++) {
			recipeIngredient.add(ingredientFromIndex(i));
		}
		return recipeIngredient;
	}

	private void printArrayList(ArrayList<Ingredient> recipeIngredient) {
		for (Ingredient i : recipeIngredient) {
			System.out.println(i);		
		}
	}

	private Ingredient ingredientFromIndex(int index) throws SQLException {
		Ingredient ingredient;
		String name = ((TextField)getNodeByCoordinate(GPingredient,index,2)).getText().toString();
		float amount=Float.parseFloat(((TextField)getNodeByCoordinate(GPingredient,index,0)).getText());
		String unit=((ChoiceBox<String>)getNodeByCoordinate(GPingredient,index,1)).getValue().toString();
		String form=((TextField)getNodeByCoordinate(GPingredient,index,3)).getText().toString();
		
		
		ingredient = DBManager.getInstance().searchIngredient(name);
		if(ingredient == null) {	
			ingredient = DBManager.getInstance().addIngredient(name);
		}
		
		ingredient.setAmount(amount);
		ingredient.setFrom(form);
		ingredient.setMeasurement(unit);
		
		return ingredient ;
	}
	
	private Node getNodeByCoordinate (GridPane gridPane , int row, int column) {
	    Node n=  gridPane.getChildren().get(column + gridPane.getColumnCount()*row);
	    return n;
	}
	
	

	
	
	
	
	
	

	private boolean checkIfGPIsComplete() {
		
		if ( GPingredient.getRowCount() == 2) {
			if(!checkGPingredient(4,8)) // is it always GPIngredient(4,8)?
				return false;
		}
		else {
			int allIndexes = (GPingredient.getRowCount()-1)* GPingredient.getColumnCount();
			if(!checkGPingredient(4,allIndexes))
				return false;
		}
		if ( GPinstruction.getRowCount() ==2) {
			if(!checkGPinstruction(2,4)) // is it always GPIngredient(2,4)?
				return false;
		}
		else {
			int allIndexes = (GPinstruction.getRowCount()-1)* GPinstruction.getColumnCount();
			if(!checkGPinstruction(3,allIndexes))
				return false;
		}
		
		return true;
	}

	private boolean checkGPingredient(int start, int end) {
		for (int i =start ; i < end; i++) {
			if(GPingredient.getChildren().get(i) instanceof TextField ) {
				if(((TextField)GPingredient.getChildren().get(i)).getText().isBlank()) {
					return false;
				}
			}
			else 
				if(GPingredient.getChildren().get(i) instanceof ChoiceBox) {
					if(((ChoiceBox<String>)GPingredient.getChildren().get(i)).getValue()==null) {
						return false ;
					}
				}
		}
		return true;
	}
	
	private boolean checkGPinstruction(int start, int end) {
		for (int i = start; i < end; i++) {
			if(GPinstruction.getChildren().get(i) instanceof TextField ) {
				if(((TextField)GPinstruction.getChildren().get(i)).getText().isBlank()) {
					return false;
				}
			}
		}
		return true;
	}
}
