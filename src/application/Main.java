package application;
	
import java.net.ConnectException;

import javafx.application.Application;
import javafx.stage.Stage;
import model.RecipeFetcher;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			RecipeFetcher.getInstance("").getRandomRecipe();
			//RecipeFetcher.getInstance().convertMeasurementToGrams(null);
		} catch (ConnectException e) {
			System.err.println("Cannot connect to API. Check internet connection");
		}
		catch (Exception e) {
			System.err.println(e.getMessage());;
		}
		//launch(args);
	}
}
