package application;


import javafx.application.Application;
import javafx.stage.Stage;

import controller.Controller;
import model.Model;
import view.LoginRegisterPage;
import view.Page;


public class Main extends Application {
 
	@Override
	public void start(Stage primaryStage) {
		Page view = new LoginRegisterPage();
		Model model = new Model();
		Controller controller = new Controller(view, model);
		try {
			controller.start(primaryStage);
		} catch(Exception e) {
			Page.showErrorWindow(e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);	
	}
}
