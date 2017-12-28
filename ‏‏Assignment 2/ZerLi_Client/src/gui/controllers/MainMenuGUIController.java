package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import entities.User.UserType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainMenuGUIController extends ParentGUIController{
	
	private @FXML MenuButton menuProducts;
	private @FXML Button btnConfig;
	private @FXML ImageView imgCart;
	private @FXML MenuItem miCatalog;
		
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
	
	public void showSurvey(ActionEvent event){
		if(Context.clientConsole.isConnected()==false)
			setServerUnavailable();
		else {
			try {
				loadGUI("SurveyGUI", false);
			} catch (Exception e) {
				lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
		}
	}
	
	public void showConnectionGUI(ActionEvent event) throws Exception{
		try {
			Context.prevGUI=this;
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
			setServerUnavailable();
		}
		if(Context.clientConsole!=null && Context.clientConsole.isConnected()==true)
			setServerAvailable();
		
		Tooltip.install(imgCart, new Tooltip("Show my cart"));
		
		Context.askOrder();
		
		if(Context.getUser() != null) {
			if(Context.getUser().getPermissions().equals(UserType.Customer) ==false)
				miCatalog.setVisible(false);
		}
	}
	
	public void showCart(MouseEvent event) throws Exception{
		try {
			loadGUI("CartGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}

	public void showReportSelector(ActionEvent event) {
		try {
			loadGUI("ReportSelectorGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}
}
