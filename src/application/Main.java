package application;

import controller.Controller;
import model.Model;
import view.LoginRegisterPage;
import view.Page;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;


public class Main extends Application {

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
			// show error window with Exception message.
			e.printStackTrace();
			//System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);	
	}
}
