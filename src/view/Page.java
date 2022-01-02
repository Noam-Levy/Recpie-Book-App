package view;


import java.util.ArrayList;

import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import listeners.UIEventListener;


public abstract class Page extends Pane  {
	
	private static ArrayList<UIEventListener> listeners = new ArrayList<UIEventListener>();
	
	
	public final void addListener(UIEventListener listener) {
		listeners.add(listener);
	}
}
