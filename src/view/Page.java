package view;

import listeners.UIEventListener;

import java.util.ArrayList;

import javafx.geometry.Pos;
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
	
	private static void showWindow(String message, boolean error) {
		StackPane root = new StackPane();
		Stage stage = new Stage();
		root.setAlignment(Pos.CENTER);
		stage.setTitle("Something went wrong");
		if(error)
			stage.setTitle("Something went wrong");	
		root.getChildren().add(createLabel(message,error));
		stage.setAlwaysOnTop(true);
		stage.centerOnScreen();
		stage.setScene(new Scene(root, Region.USE_PREF_SIZE, Region.USE_PREF_SIZE));
		stage.show();
	}
	
	private static Label createLabel(String message, boolean error) {
		Label label = new Label(message);
		label.setMinSize(300, 50);
		label.setWrapText(true);
		if(error)
			label.setTextFill(Color.RED);
		return label;
	}
		
	public static final void showErrorWindow(String error) {
		showWindow(error, true);
	}
	
	public static final void showSuccessWindow(String message) {
		showWindow(message,false);
	}

}
