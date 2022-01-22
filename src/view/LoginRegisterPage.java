package view;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import exceptions.UserRegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import listeners.UIEventListener;

public class LoginRegisterPage extends Page implements Initializable {


	@FXML private Button btnLogin, btnRegister;
	@FXML private HBox hbAPI;
	@FXML private VBox vbLogin, vbRegister;
	@FXML private TextField registerUserName, loginUserName;
	@FXML private PasswordField registerPassword, loginPassword;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showLoginOpt(null);
	}

	@FXML
	private void showLoginOpt(ActionEvent event) {
		vbRegister.setVisible(false);
		btnRegister.setVisible(false);
		vbLogin.setVisible(true);
		btnLogin.setVisible(true);
	}

	@FXML
	private void showRegisterOpt(ActionEvent event) {
		vbLogin.setVisible(false);
		btnLogin.setVisible(false);
		vbRegister.setVisible(true);
		btnRegister.setVisible(true);
	}

	@FXML
	private void userLogin(ActionEvent event) {
		if(loginPassword.getText().isBlank() || loginUserName.getText().isBlank()) {
			showErrorWindow("Username or password fields cannot be empty");
			return;
		}
		try {
			for (UIEventListener l : listeners)
				if(!l.userLogin(loginUserName.getText(), loginPassword.getText())) 
					showErrorWindow("Wrong Username or password");
		} catch (NoSuchAlgorithmException | IOException | SQLException e) {
			showErrorWindow("Error: " + e.getMessage());
		}
	}

	@FXML
	void registerNewUser(ActionEvent event) {
		for (UIEventListener l : listeners) {
			try {
				l.registerUser(registerUserName.getText(), registerPassword.getText());
				// enable menu buttons
			} catch (UserRegistrationException e) {
				showErrorWindow(e.getMessage());
			} catch (SQLException sqe) {
				//ADD SQLException handling cases
				showErrorWindow("Faild logging into database");
			} catch (NoSuchAlgorithmException e) {
				showErrorWindow("Cannot create properties file: " + e.getMessage());
			} catch (IOException e) {
				showErrorWindow(e.getMessage());
			}
		}
	}



}
