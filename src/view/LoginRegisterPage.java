package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginRegisterPage extends Page {


    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private HBox hbAPI;


    @FXML
    private VBox vbLogin;

    @FXML
    private VBox vbRegister;

    @FXML
    void showLoginOpt(ActionEvent event) {
    	vbRegister.setVisible(false);
    	btnRegister.setVisible(false);
    	vbLogin.setVisible(true);
    	btnLogin.setVisible(true);
    }

    @FXML
    void showRegisterOpt(ActionEvent event) {
    	vbLogin.setVisible(false);
    	btnLogin.setVisible(false);
    	vbRegister.setVisible(true);
    	btnRegister.setVisible(true);
    }

}
