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

	@FXML
	private GridPane GPingiridiant;

	@FXML
	private GridPane GPinstruction;

	@FXML
	private Button btnAddRowIngrediant;

	@FXML
	private Button btnAddRowInstruction;
	@FXML
	private ChoiceBox<String> cmbUnit;



	@FXML
	void addRowIngrediant(ActionEvent event) {
		// need to check if all filled before make new row

		int allIndexes = GPingiridiant.getRowCount()* GPingiridiant.getColumnCount();
		if (!checkGPingiridiant(4,allIndexes))
			return;
	
		
		ChoiceBox<String> cmbUnitClone = new ChoiceBox<String>();
		for (String s : cmbUnit.getItems()) {
			cmbUnitClone.getItems().add(s);	
		}
		cmbUnitClone.setPrefSize(cmbUnit.getPrefWidth(), cmbUnit.getPrefHeight());
		GPingiridiant.addRow(GPingiridiant.getRowCount(), new TextField(),cmbUnitClone,new TextField(), new TextField());
	}

	@FXML
	void addRowInstruction(ActionEvent event) {
		int allIndexes = GPinstruction.getRowCount()* GPinstruction.getColumnCount();
		
		if (!checkGPinstruction(0,allIndexes))
			return;
		
		GPinstruction.addRow(GPinstruction.getRowCount(), new Label(GPinstruction.getRowCount()+""),new TextField());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		cmbUnit.getItems().add("1");
		cmbUnit.getItems().add("2");
		cmbUnit.getItems().add("3");		
	}

	@FXML
	private TextField txfRecipieCookTime;

	@FXML
	private TextField txfRecipieCuisine;

	@FXML
	private TextField txfRecipieName;



	@FXML
	void saveRecipie(ActionEvent event) {

		if (txfRecipieCookTime.getText().isEmpty() || txfRecipieCuisine.getText().isEmpty() || txfRecipieName.getText().isEmpty()) 
			return;

		if ( GPingiridiant.getRowCount() ==2) {
			if(!checkGPingiridiant(4,8))
				return;
		}
		else {
			int allIndexes = (GPingiridiant.getRowCount()-1)* GPingiridiant.getColumnCount();
			if(!checkGPingiridiant(4,allIndexes))
				return;
		}
		
		if ( GPinstruction.getRowCount() ==2) {
			if(!checkGPinstruction(3,4))
				return;
		}
		else {
			int allIndexes = (GPinstruction.getRowCount()-1)* GPinstruction.getColumnCount();
			if(!checkGPinstruction(3,allIndexes))
				return;
		}
			
		// add recipe
		
		
	}


	private boolean checkGPingiridiant(int start , int end) {
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
	
	

	private boolean checkGPinstruction(int start , int end) {
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
