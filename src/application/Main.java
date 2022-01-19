package application;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import controller.Controller;
import model.Model;
import view.LoginRegisterPage;
import view.Page;


public class Main extends Application {

	@SuppressWarnings("unused") // controller
	@Override
	public void start(Stage primaryStage) {
		Page view = new LoginRegisterPage();
		Model model = new Model();
		Controller controller = new Controller(view, model);
		try {
			Parent root =FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			view.showErrorWindow(e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);	
	}
}
