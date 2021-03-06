package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import listeners.UIEventListener;
import model.DBManager;
import model.Ingredient;
import model.Recipe;

public class AddRecipePage extends Page implements Initializable {

	@FXML private GridPane GPingredient, GPinstruction;
	@FXML private Pane addPhotoRecipe, addTextRecipe;
	@FXML private ImageView recipeImageView, DragDropImage;
	@FXML private Button btnAddRowIngredient, btnAddRowInstruction;
	@FXML private CheckBox checkIfPhoto;
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
		cmbUnitClone.setStyle("-fx-background-color:  #ebf2ed; -fx-border-color:  #574443; -fx-border-radius: 5 ; -fx-background-radius: 5;");
		TextField newTF1 = new TextField();
		addListenerToTextField(newTF1);
		newTF1.setStyle("-fx-background-color:  #ebf2ed; -fx-border-color:  #574443; -fx-border-radius: 5 ; -fx-background-radius: 5;");
		TextField newTF2 = new TextField();
		newTF2.setStyle("-fx-background-color:  #ebf2ed; -fx-border-color:  #574443; -fx-border-radius: 5 ; -fx-background-radius: 5;");
		TextField newTF3 = new TextField();
		newTF3.setStyle("-fx-background-color:  #ebf2ed; -fx-border-color:  #574443; -fx-border-radius: 5 ; -fx-background-radius: 5;");
		GPingredient.addRow(GPingredient.getRowCount(), newTF1,cmbUnitClone,newTF2, newTF3);
	}

	@FXML
	void addRowInstruction(ActionEvent event) {
		// checks if all available instructions rows are filled before allowing to add more
		if (!checkGPinstruction())
			return;
		Label l= new Label(GPinstruction.getRowCount()+"");
		l.setStyle("-fx-text-fill: black ; ");
		TextField newTF = new TextField();
		newTF.setStyle("-fx-background-color:  #ebf2ed; -fx-border-color:  #574443; -fx-border-radius: 5 ; -fx-background-radius: 5;");
		GPinstruction.addRow(GPinstruction.getRowCount(),l,newTF);
	}

	@FXML
	void saveRecipie(ActionEvent event) {
		if (txfRecipieName.getText().isEmpty() ||txfRecipieCookTime.getText().isEmpty() || txfServings.getText().isEmpty() || !checkIfGPIsComplete()) 
			showErrorWindow("Please fill all data fields (Cuisine is not required)");
		try {
			if(!createRecipe())
				showErrorWindow("Couldnt add recipe. please try again");
			else
				showSuccessWindow("Recipe added successfully");
		} catch (SQLException e) {
			showErrorWindow(e.getMessage());
		}
		clearFields();
	}

	@SuppressWarnings("unchecked") // Choice boxes are of type field
	private void clearFields() {
		resetGridPanes() ;
		txfRecipieName.clear();
		txfRecipieCuisine.clear();
		txfRecipieCookTime.clear();
		txfServings.clear();
		((TextField)GPingredient.getChildren().get(4)).clear();
		((ChoiceBox<String>)GPingredient.getChildren().get(5)).setValue(null);
		((TextField)GPingredient.getChildren().get(6)).clear();
		((TextField)GPingredient.getChildren().get(7)).clear();
		((TextField)GPinstruction.getChildren().get(3)).clear();
	}

	@FXML
	private void resetGridPanes() {
		GPingredient.getChildren().retainAll(GPingredient.getChildren().get(0),GPingredient.getChildren().get(1),
				GPingredient.getChildren().get(2),GPingredient.getChildren().get(3),GPingredient.getChildren().get(4)
				,GPingredient.getChildren().get(5),GPingredient.getChildren().get(6),GPingredient.getChildren().get(7));

		GPinstruction.getChildren().retainAll(GPinstruction.getChildren().get(0),GPinstruction.getChildren().get(1)
				,GPinstruction.getChildren().get(2),GPinstruction.getChildren().get(3));
	}

	@FXML
	private void textOrPhoto(ActionEvent event) {    	
		setDragPhoto(checkIfPhoto.isSelected());
	}

	@FXML
	void handleDrag(DragEvent event) {
		if (event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	@FXML
	void handleDrop(DragEvent event) {
		List<File> files = event.getDragboard().getFiles();
		Image img;
		try {
			img = new Image(new FileInputStream(files.get(0)));
			recipeImageView.setImage(img);
			DragDropImage.setVisible(false);
		} catch (FileNotFoundException e) {
			showErrorWindow("Unable to load file");
		}
	}

	@FXML
	void savePhotoRecipe(ActionEvent event) {
		try {
			boolean saved = false;
			for (UIEventListener l : listeners)
				saved = l.savePhotoRecipe(recipeImageView.getImage());
			if(saved) {
				showSuccessWindow("Recipe saved");
			} else
				showErrorWindow("Unable to save recipe");
		} catch (IOException e) {
			showErrorWindow("Faild to save recipe: " + e.getMessage());
		}
		setDragPhoto(true);
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

	private boolean createRecipe() throws SQLException {
		String recipeName  = txfRecipieName.getText();
		String recipeCookTime  = txfRecipieCookTime.getText();
		String recipeServing  = txfServings.getText();
		String recipeCuisine  = txfRecipieCuisine.getText();
		ArrayList<Ingredient> recipeIngredients = createRecipeIngredient();
		HashMap<Integer,String> recipeInstructions = createRecipeInstructions();

		Recipe r = new Recipe(null,recipeName,Integer.parseInt(recipeCookTime),Integer.parseInt(recipeServing));
		r.setIngredients(recipeIngredients);
		r.setInstructions(recipeInstructions);
		if(!recipeCuisine.isBlank()) {
			String cuisineID = DBManager.getInstance().getCuisineID(recipeCuisine);
			r.addCuisine(cuisineID, recipeCuisine);
		}
		for (UIEventListener l : listeners) {
			if(!l.addRecipeToDB(r))
				return false;
		}
		return true;
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
		return checkGPingredient( )&& checkGPinstruction();
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
		for (int i = 1; i < GPinstruction.getRowCount(); i++)
			if(getNodeByCoordinate(GPinstruction,i,1)==null)
				return false;
		return true;
	}

	private void setDragPhoto(boolean state) {
		addTextRecipe.setVisible(!state);
		addPhotoRecipe.setVisible(state);
		DragDropImage.setVisible(state);
		recipeImageView.setImage(null);
	}

}
