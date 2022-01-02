package view;

import java.net.URL;
import java.util.Iterator;
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

public class AddRecipePage extends Page implements Initializable {

	@FXML private GridPane GPingiridiant;
	@FXML private GridPane GPinstruction;
	@FXML private Button btnAddRowIngrediant;
	@FXML private Button btnAddRowInstruction;
	@FXML private ChoiceBox<String> cmbUnit;
	@FXML private TextField txfRecipieCookTime, txfRecipieCuisine, txfRecipieName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// needs to be changed according to real runtime enviroment
		cmbUnit.getItems().add("1");
		cmbUnit.getItems().add("2");
		cmbUnit.getItems().add("3");		
	}

	@FXML
	void addRowIngredient(ActionEvent event) {
		// checks if all opened ingredients rows are filled before allowing to add more
		int allIndexes = GPingiridient.getRowCount()* GPingiridiant.getColumnCount();
		if (!checkGPingiridient(4,allIndexes))
			return;
		// create new choice box and add items. NEEDS TO BE CHAGNED FROM STRING TO INGREDIENT
		ChoiceBox<String> cmbUnitClone = new ChoiceBox<String>();
		for (String s : cmbUnit.getItems()) {
			cmbUnitClone.getItems().add(s);	
		}
		cmbUnitClone.setPrefSize(cmbUnit.getPrefWidth(), cmbUnit.getPrefHeight());
		GPingiridiant.addRow(GPingiridiant.getRowCount(), new TextField(),cmbUnitClone,new TextField(), new TextField());
	}

	@FXML
	void addRowInstruction(ActionEvent event) {
		// checks if all opened instructions rows are filled before allowing to add more
		int allIndexes = GPinstruction.getRowCount()* GPinstruction.getColumnCount();
		if (!checkGPinstruction(0,allIndexes))
			return;
		GPinstruction.addRow(GPinstruction.getRowCount(), new Label(GPinstruction.getRowCount()+""),new TextField());
	}

	@FXML
	void saveRecipie(ActionEvent event) {
		if (txfRecipieCookTime.getText().isEmpty() || txfRecipieCuisine.getText().isEmpty() || txfRecipieName.getText().isEmpty()) 
			return;
		if ( GPingiridiant.getRowCount() == 2) {
			if(!checkGPingiridiant(4,8)) // is it always GPIngredient(4,8)?
				return;
		}
		else {
			int allIndexes = (GPingiridiant.getRowCount()-1)* GPingiridiant.getColumnCount();
			if(!checkGPingiridiant(4,allIndexes))
				return;
		}
		if ( GPinstruction.getRowCount() ==2) {
			if(!checkGPinstruction(3,4)) // is it always GPIngredient(3,4)?
				return;
		}
		else {
			int allIndexes = (GPinstruction.getRowCount()-1)* GPinstruction.getColumnCount();
			if(!checkGPinstruction(3,allIndexes))
				return;
		}
		// add recipe
	}


	private boolean checkGPingredient(int start, int end) {
		for (int i =start ; i < end; i++) {
			if(GPingiridiant.getChildren().get(i) instanceof TextField ) {
				if(((TextField)GPingiridiant.getChildren().get(i)).getText().isBlank()) {
					return false;
				}
			}
			else 
				if(GPingiridiant.getChildren().get(i) instanceof ChoiceBox) {
					if(((ChoiceBox<String>)GPingiridiant.getChildren().get(i)).getValue()==null) {
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
