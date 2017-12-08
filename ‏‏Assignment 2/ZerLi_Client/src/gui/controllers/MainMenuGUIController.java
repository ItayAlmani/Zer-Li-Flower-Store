package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainMenuGUIController extends ParentGUIController implements Initializable{
	
	@FXML
	private Button btnProducts;
		
	public void showProducts(ActionEvent event){
		if(Context.cc.isConnected()==false)
			setServerUnavailable();
		else {
			/*Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/ProductsFormGUI.fxml"));
			Pane root;*/
			try {
				/*root = loader.load();
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);		
				primaryStage.show();*/
				loadWindows(event, "ProductsFormGUI", false);
			} catch (Exception e) {
				lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
		}
	}
	
	public void showConnectionGUI(ActionEvent event) throws Exception{
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/fxmls/ConnectionConfigGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/gui/fxmls/MainMenuGUI.fxml"));
				
		Scene scene = new Scene(root);
		primaryStage.setTitle("Prototype");
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.CurrentGUI = this;
		if(Context.cc==null || Context.cc.isConnected()==false) {
			try {
				
				Context.connectToServer();
			} catch (IOException e) {
				setServerUnavailable();
			}
		}
		if(Context.cc!=null && Context.cc.isConnected()==true)
			setServerAvailable();
	}
	
	public void setServerUnavailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnProducts.setDisable(true);
				lblMsg.setText("Connection failed");
			}
		});
	}
	
	public void setServerAvailable() {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnProducts.setDisable(false);
				lblMsg.setText("");
			}
		});
	}
	
	public void ShowErrorMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblMsg.setText("Error");
			}
		});
	}

	public void ShowSuccessMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblMsg.setText("Success");
			}
		});
	}
}
