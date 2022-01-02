package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MenuPage extends Page implements Initializable {

	private Pane pane;
	@FXML
	private BorderPane bp;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		switchPane("LoginRegisterPage")	;
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
		switchPane("AllRecpiesPage");
	}
	
	
	public final Page switchPane(String fileName)  {
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
