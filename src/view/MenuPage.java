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
import listeners.UIEventListener;

public class MenuPage extends Page implements Initializable {

	private Pane pane;
	@FXML private BorderPane bp;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		switchPane("LoginRegisterPage");
		// disable menu buttons
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
	private void showRecipesPage(ActionEvent event) {
		switchPane("AllRecipesPage");
	}
	
	//private void showLoginRegisetPage(ActionEvent event) - for logout use-case
	
	
	public final void switchPane(String fileName)  {
		try {
			pane = FXMLLoader.load(getClass().getResource("/view/"+fileName+".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		bp.setCenter(pane);
		/* 
		 * set MVC controller current view to actual view.
		 * this is effectively implements the command design pattern between the MVC controller and the view controllers 
		 * such that the MVC controller functionality regarding each view changes during runtime.
		 */
		for (UIEventListener l : listeners) {
			switch (fileName) {
			case "AllRecipesPage":
				 l.setCurrentView(new ShowRecipeBookPage());
				 break;
			case "AddPage":
				l.setCurrentView(new AddRecipePage());
				break;
			case "SearchPage":
				l.setCurrentView(new SearchPage());
				break;
			case "LoginRegisterPage":
				l.setCurrentView(new LoginRegisterPage());
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + fileName); // MIGHT NEED TO BE CHANGED
			}
		}		
		
	}
}
