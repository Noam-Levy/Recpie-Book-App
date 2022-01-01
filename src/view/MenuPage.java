package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MenuPage extends Page {

	private Pane pane;
	@FXML
	private BorderPane bp;
	
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
		switchPane("AllRecipesPage");
	}
	
	public final Page switchPane(String fileName)  {
		/*
		 * The switchPane function effectively implements the command design pattern 
		 * in order to change the displayed user screen and alter the MVC controller behavior during runtime.  
		 */
		try {
			pane = FXMLLoader.load(getClass().getResource("/view/"+fileName+".fxml"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		bp.setCenter(pane);
		switch (fileName) {
		case "AllRecipesPage":
			return new ShowRecipeBookPage();
		case "AddPage":
			return new AddRecipePage();
		case "SearchPage":
			return new SearchPage();
		default:
			throw new IllegalArgumentException("Unexpected value: " + fileName); // MIGHT NEED TO BE CHANGED
		}
		
	}

}
