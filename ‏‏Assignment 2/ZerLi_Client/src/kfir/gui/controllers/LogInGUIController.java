package kfir.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import entities.User;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
public class LogInGUIController implements Initializable{
	
	private @FXML JFXTextField txtUserName;
	private @FXML JFXPasswordField txtPassword;
	private @FXML Button btnLogIn;
	private @FXML ImageView imgLogo, imgLogIn;
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