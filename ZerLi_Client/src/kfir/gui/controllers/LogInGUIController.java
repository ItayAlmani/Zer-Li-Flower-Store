package kfir.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import common.MainClient;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
public class LogInGUIController implements Initializable{
	
	private @FXML JFXTextField txtUserName;
	private @FXML JFXPasswordField txtPassword;
	private @FXML Button btnLogIn;
	private @FXML ImageView imgLogo;
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
				Context.fac.user.getUserForLogIn(new User(uName, pass));
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				System.err.println("LogInGUI getUser failed");
				e.printStackTrace();
			}
		}
	}
	
	public void setUsers(ArrayList<User> users) {
		Context.mainScene.clearMsg();
		if(getAllUsers==true) {
			if(Platform.isFxApplicationThread())
				TextFields.bindAutoCompletion(txtUserName, users);
			else Platform.runLater(()->TextFields.bindAutoCompletion(txtUserName, users));
			getAllUsers=false;
			return;
		}
		//User Name or Password are incorrect
		if(users.isEmpty())
			Context.mainScene.setMessage("User name or password are incorrect!");
		else if(users.size()==1){
			User user = users.get(0);
			if(user.isConnected()==true) {
				Context.mainScene.setMessage("You are already connected at another device\n"
						+ "Please disconnect before next login");
				return;
			}
			else if(user.isActive()==false) {
				Context.mainScene.setMessage("Your account isn't active.\n"
						+ "Please approach to the store for details");
				return;
			}
			Context.setUser(user);
			//Will load next gui if Context will call to setUserConnected()
		}
	}
	
	public void setUserConnected(User user) {
		try {
			user.setConnected(true);
			Context.fac.user.update(user);
			Context.mainScene.logInSuccess();
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ParentGUIController.currentGUI=this;
			Context.fac.user.getAllUsers();
			imgLogo.setImage(MainClient.getLogoAsImage());
			setComponentSendOnEnter(Arrays.asList(new Node[] {txtUserName,txtPassword}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setComponentSendOnEnter(List<Node> comp) {
		for (Node node : comp) {
			node.setOnKeyPressed((keyEvent)->{
				if (keyEvent.getCode() == KeyCode.ENTER)
		           logIn();
			});
		}
	}
}