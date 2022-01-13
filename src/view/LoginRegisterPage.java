package view;

import java.sql.SQLException;

import exceptions.UserRegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import listeners.UIEventListener;

public class LoginRegisterPage extends Page {


    @FXML private Button btnLogin, btnRegister;
    @FXML private HBox hbAPI;
    @FXML private VBox vbLogin, vbRegister;
    @FXML private TextField registerUserName;
    @FXML private PasswordField registerPassword;

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
    
    @FXML
    void registerNewUser(ActionEvent event) {
    	for (UIEventListener l : listeners) {
			try {
				l.registerUser(registerUserName.getText(), registerPassword.getText());
			} catch (UserRegistrationException e) {
				showErrorWindow(e.getMessage());
			} catch (SQLException sqe) {
				//ADD SQLException handling cases
			}
		}
    }
    
}
