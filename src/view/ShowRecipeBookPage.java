package view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.DBManager;
import model.Recipe;

public class ShowRecipeBookPage extends Page {



	
    @FXML
    private GridPane GPingredient,GPinstruction;

    @FXML
    private ImageView blackStar,yellowStar;

    @FXML
    private CheckBox checkBoxFavorite;

    @FXML
    private Label lbCookTime,lbCuisine,lbRecipeName,lbServing;


    @FXML
    void blackOrYellowStar(ActionEvent event) {
    	
    	if(checkBoxFavorite.isSelected()) {
    		yellowStar.setVisible(true);
    		blackStar.setVisible(false);
    	}
    	else {
    		yellowStar.setVisible(false);
    		blackStar.setVisible(true);
    	}
    		

    }
    
    
    
    
    
    
    
    
    
    
    
	public void showRecipes(ArrayList<Recipe> foundRecipes) {
//		if (foundRecipes.size() == 0)
//		{
//			showErrorWindow("No recipes found");
//			return;
//		}
		// fill recipe pages...
	}



}
