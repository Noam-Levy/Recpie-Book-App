package application;

import controller.Controller;

//import java.net.ConnectException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Model;

import view.MenuPage;
import view.Page;
//import model.RecipeFetcher;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		Page view = new MenuPage();
		Model model = new Model();
		Controller controller = new Controller(view, model);
		try {
			Parent root =FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			// show error window with Exception message.
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);	
	}
}

//		try {
		//			RecipeFetcher.getInstance("").getRandomRecipe();
		//			//RecipeFetcher.getInstance().convertMeasurementToGrams(null);
		//		} catch (ConnectException e) {
		//			System.err.println("Cannot connect to API. Check internet connection");
		//		}
		//		catch (Exception e) {
		//			System.err.println(e.getMessage());