package kfir.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import common.MainClient;
import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
public class LogInGUIController implements Initializable{
	
	private @FXML JFXTextField txtUserName;
	private @FXML JFXPasswordField txtPassword;
	private @FXML Button btnLogIn;
	private @FXML ImageView imgLogo;
	
	/**
	 * Function get input from GUI TextField, check if the input is correct
	 * and get the relevant user from the DataBase
	 * if the user don't exist show ErrorMsg  
	 */
	public void logIn() {
		String uName = this.txtUserName.getText(),
				pass = this.txtPassword.getText();
		if(uName!=null && pass !=null) {
			try {
				Context.mainScene.setMenuPaneDisable(true);
				Context.fac.user.getUserForLogIn(new User(uName, pass));
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				System.err.println("LogInGUI getUser failed");
				e.printStackTrace();
			}
		}
		else
			Context.mainScene.setMessage("All fields must be entered");
	}
	
	/**
	 * Function get {@link ArrayList} of {@link User} 
	 * check if the {@link ArrayList} is empty, if yes show Error
	 * check {@link User} Connection and Active status and set {@link User} to {@link Context}
	 * @param users - {@link ArrayList} of {@link User} return from DB
	 */
	public void setUsers(ArrayList<User> users) {
		Context.mainScene.setMenuPaneDisable(false);
		Context.mainScene.clearMsg();
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
	
	/**
	 * Function set {@link User} Connection status
	 * In order to prevent double connection of same {@link User} 
	 * @param user - {@link User} to change his connection status
	 */
	public void setUserConnected(User user) {
		try {
			user.setConnected(true);
			Context.fac.user.update(user);
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
			return;
		}
		Context.mainScene.logInSuccess();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			imgLogo.setImage(MainClient.getLogoAsImage());
			setComponentSendOnEnter(Arrays.asList(new Node[] {txtUserName,txtPassword}));
		} catch (IOException e) {
			e.printStackTrace();
			Context.mainScene.ShowErrorMsg();
		}
	}
	
	/**
	 * Function allow user to LogIn with "Enter" key
	 * @param comp - Key that pressed
	 */
	private void setComponentSendOnEnter(List<Node> comp) {
		for (Node node : comp) {
			node.setOnKeyPressed((keyEvent)->{
				if (keyEvent.getCode() == KeyCode.ENTER)
		           logIn();
			});
		}
	}
}