package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainMenuGUIController extends ParentGUIController{
	
	private @FXML MenuButton menuProducts;
	private @FXML Button btnConfig;
		
	public void showProducts(ActionEvent event){
		if(Context.clientConsole.isConnected()==false)
			setServerUnavailable();
		else {
			try {
				loadGUI("ProductsFormGUI", false);
			} catch (Exception e) {
				lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
		}
	}
	
	public void showConnectionGUI(ActionEvent event) throws Exception{
		try {
			loadGUI("ConnectionConfigGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}
	
	public void showCatalog(ActionEvent event) throws Exception{
		try {
			loadGUI("CatalogGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}
	
	public void setServerUnavailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				menuProducts.setDisable(true);
				btnConfig.setVisible(true);
				lblMsg.setText("Connection failed");
			}
		});
	}
	
	public void setServerAvailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				menuProducts.setDisable(false);
				btnConfig.setVisible(false);
				lblMsg.setText("");
			}
		});
	}
		
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/MainMenuGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Main Menu");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false) {
			try {
				Context.connectToServer();
			} catch (IOException e) {
				setServerUnavailable();
			}
		}
		if(Context.clientConsole!=null && Context.clientConsole.isConnected()==true)
			setServerAvailable();
	}
}
