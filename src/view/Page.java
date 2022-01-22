package view;

import listeners.UIEventListener;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public abstract class Page {
	
	protected static ArrayList<UIEventListener> listeners = new ArrayList<UIEventListener>();
	
		public final void addListener(UIEventListener listener) {
		listeners.add(listener);
	}
	
	public static final void showErrorWindow(String error) {
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.setTitle("Something went wrong");
		StackPane root = new StackPane();
		Label label = new Label(error);
		label.setTextFill(Color.RED);
		root.getChildren().add(label);
		stage.setScene(new Scene(root, Region.USE_PREF_SIZE, Region.USE_PREF_SIZE));
		stage.show();
	}
}
