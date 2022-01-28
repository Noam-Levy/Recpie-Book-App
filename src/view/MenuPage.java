package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import listeners.UIEventListener;

public class MenuPage extends Page implements Initializable {

	private FXMLLoader loader;
	private Pane pane; 
	@FXML private BorderPane bp;
	@FXML private Button searchRecipeButton, addRecipeButton, allRecipesButton, logOutButton;
	@FXML private Label loggedUserLabel;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		switchPane("LoginRegisterPage");
		disableMenuButtons();
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

	@FXML
	private void userLogout(ActionEvent event) { 
		for (UIEventListener l : listeners)
			l.userLogout();
		switchPane("LoginRegisterPage");
		disableMenuButtons();
		setUserLabel("");
	}

	@FXML
	private void setMainView() {
		bp.setCenter(pane);
	}

	public void enableMenuButtons() {
		searchRecipeButton.setDisable(false);
		addRecipeButton.setDisable(false); 
		allRecipesButton.setDisable(false); 
		logOutButton.setDisable(false);
	}

	public void disableMenuButtons() {
		searchRecipeButton.setDisable(true);
		addRecipeButton.setDisable(true); 
		allRecipesButton.setDisable(true); 
		logOutButton.setDisable(true);
	}

	public void setUserLabel(String userName) {
		loggedUserLabel.setText("Logged as: " + userName);
	}

	public final void switchPane(String fileName) throws IllegalArgumentException {
		URL location = getClass().getResource("/view/"+fileName+".fxml");
		this.loader = new FXMLLoader(location);
		try {
			pane =(Pane)loader.load();
		} catch (IOException e) {
			showErrorWindow(e.getMessage());
		}
		setMainView();
		/* 
		 * set MVC controller current view to actual view.
		 * this is effectively implements the command design pattern between the MVC controller and the view controllers 
		 * such that the MVC controller functionality regarding each view changes during runtime.
		 */
		for (UIEventListener l : listeners) {
			switch (fileName) {
			case "AllRecipesPage":
				l.setCurrentView((ShowRecipeBookPage)loader.getController());
				break;
			case "AddPage":
				l.setCurrentView((AddRecipePage)loader.getController());
				break;
			case "SearchPage":
				l.setCurrentView((SearchPage)loader.getController());
				break;
			case "LoginRegisterPage":
				l.setCurrentView((LoginRegisterPage)loader.getController());
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + fileName);
			}
		}				
	}
}
