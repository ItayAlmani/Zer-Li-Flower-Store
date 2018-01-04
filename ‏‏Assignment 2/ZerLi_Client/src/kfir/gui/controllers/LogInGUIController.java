package kfir.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import common.Context;
import entities.Customer;
import entities.User;
import entities.User.UserType;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class LogInGUIController implements Initializable{
	
	private @FXML TextField txtUserName, txtPassword;
	private @FXML Button btnLogIn;
	private @FXML ImageView imgLogo;
	private String logoPath="/images/logos/img/logo3-0.png";
	private boolean getAllUsers=true;
	
	public void logIn() {
		String uName = this.txtUserName.getText(),
				pass = this.txtPassword.getText();
		if(uName == null || uName.isEmpty() || pass == null || pass.isEmpty()) {
			uName ="izharAn";
			pass="1234";
		}
		System.err.println("Go to LogInGUIController to enable data in login");
		if(uName!=null && pass !=null) {
			try {
				Context.fac.user.getUserByUNameAndPass(new User(uName, pass));
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				System.err.println("LogInGUI getUser failed");
				e.printStackTrace();
			}
		}
	}
	
	public void setUsers(ArrayList<User> users) {
		if(getAllUsers==true) {
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	TextFields.bindAutoCompletion(txtUserName, users);
		        }
			});
			getAllUsers=false;
			return;
		}
		//User Name or Password are incorrect
		if(users.size()==0)
			Context.mainScene.setMessage("User name or password are incorrect!");
		else {
			User user = users.get(0);
			if(user.isConnected()==true) {
				Context.mainScene.setMessage("You are already connected at another device\n"
						+ "Please disconnect before next login");
				return;
			}
			Context.setUser(user);
			//Will load next gui if Context will call to setUserConnected()
		}
	}
	
	public void setUserConnected(User user) {
		user.setConnected(true);
		Context.fac.user.updateUser(user);
		Context.mainScene.logInSuccess();
	}
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/LogInGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Login");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
		
		try {
			Context.fac.user.getAllUsers();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ValidationSupport validationSupport = new ValidationSupport();
		validationSupport.registerValidator(txtUserName, Validator.createEmptyValidator("Text is required"));
		/*validationSupport.validationResultProperty().addListener( (o, oldValue, newValue) ->
        messageList.getItems().setAll(newValue.getMessages()));*/
		imgLogo.setImage(new Image(getClass().getResourceAsStream(logoPath)));
		setComponentSendOnEnter(Arrays.asList(new Node[] {txtUserName,txtPassword}));
	}
	
	private void setComponentSendOnEnter(List<Node> comp) {
		for (Node node : comp) {
			node.setOnKeyPressed(new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent keyEvent) {
			        if (keyEvent.getCode() == KeyCode.ENTER)  {
			           logIn();
			        }
			    }
			});
		}
	}
}