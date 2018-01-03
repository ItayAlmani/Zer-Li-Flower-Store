package kfir.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

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
public class LogInGUIController extends ParentGUIController{
	
	private @FXML TextField txtUserName, txtPassword;
	private @FXML Button btnConfig, btnLogIn;
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
				Context.fac.user.getUser(new User(uName, pass));
			} catch (IOException e) {
				ShowErrorMsg();
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
		if(users.size()==0)
			ShowErrorMsg();
		else {
			User user = users.get(0);
			Context.setUser(user);
			users.get(0).setConnected(true);
			Context.fac.user.updateUser(user);
			
			Platform.runLater(new Runnable() {
		        @Override
		        public void run() {
		        	loadMainMenu();
		        }
			});
		}
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
		super.initialize(location, resources);
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
		
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false) {
			try {
				Context.connectToServer();
			} catch (IOException e) {
				setServerUnavailable();
			}
		}
		if(Context.clientConsole!=null && Context.clientConsole.isConnected()==true &&
				Context.dbConnected == true) {
			try {
				Context.fac.dataBase.getDBStatus();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setServerAvailable();
		}
		if(Context.dbConnected == false)
			setServerUnavailable();
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

	public void showConnectionGUI(ActionEvent event){
		try {
			Context.prevGUI=this;
			loadGUI("ConnectionConfigGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}
	
	public void setServerUnavailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnLogIn.setDisable(true);
				btnConfig.setVisible(true);
				lblMsg.setText("Connection failed");
			}
		});
	}
	
	public void setServerAvailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnLogIn.setDisable(false);
				btnConfig.setVisible(false);
				lblMsg.setText("");
			}
		});
	}
}