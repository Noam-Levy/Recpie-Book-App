package view;


import java.util.ArrayList;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import listeners.UIEventListener;


public abstract class Page extends Pane  {
	
	protected static ArrayList<UIEventListener> listeners = new ArrayList<UIEventListener>();
	
	
	public final void addListener(UIEventListener listener) {
		listeners.add(listener);
	}
	
	public final void showErrorWindow(String error) {
		Stage stage = new Stage();
		stage.setTitle("Something went wrong");
		StackPane root = new StackPane();
		Label label = new Label(error);
		label.setTextFill(Color.RED);
		root.getChildren().add(label);
		stage.setScene(new Scene(root, 300, 50));
		stage.show();
	}
}
