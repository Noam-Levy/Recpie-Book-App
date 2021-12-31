package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MenuController implements Initializable {

	private Pane view;
	@FXML
	private BorderPane bp;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}


	@FXML
	private void showSearchPage(ActionEvent event) {
		switchPane("SearchPage");

	}
	
	@FXML
	private void showAddPage(ActionEvent event) {
		switchPane("AddPage");
	}
	
	@FXML
	private void showAllRecipiesPage(ActionEvent event) {
		switchPane("AllRecipiesPage");
	}


	public void switchPane(String fileName)  {

		try {
			view = FXMLLoader.load(getClass().getResource(fileName+".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		bp.setCenter(view);
	}
	
	@FXML
	public void changeTextOnSearch(String str) {
		
	}




}
