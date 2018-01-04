package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import entities.User;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainMenuGUIController extends ParentGUIController{
	
	private @FXML MenuButton menuProducts;
	private @FXML Button btnConfig;
	private @FXML ImageView imgCart;
	private @FXML MenuItem	miCatalog, miShowProduct, miAddSurvey,
							miReportSelector, miAssembleItem, miUpdateOrderStatus,
							miManualTransaction,miSurveyReport;
	
	private String cartLogoPath="/images/Shopping_Cart.png";
		
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
	
	public void showSurveyReport(ActionEvent event){
		if(Context.clientConsole.isConnected()==false)
			setServerUnavailable();
		else {
			try {
				loadGUI("SurveyReportGUI", false);
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
			loadGUI("CatalogGUI", "ProductsPresentationCSS");
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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		imgCart.setImage(new Image(getClass().getResourceAsStream(cartLogoPath)));
		Tooltip.install(imgCart, new Tooltip("Show my cart"));
		
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false)
			setServerUnavailable();
		else
			setServerAvailable();
		
		if(Context.order==null)
			Context.askOrder();
		/*User user = Context.getUser();
		if(user != null) {
			UserType perm = user.getPermissions();
			if(perm.equals(UserType.Customer) ==false) {
				miCatalog.setVisible(false);
			}
			if(perm.equals(UserType.Customer)==true) {
				miReportSelector.setVisible(false);
				miUpdateOrderStatus.setVisible(false);
				miAddSurvey.setVisible(false);
			}
		}*/
		
	}
	
	public void showCart(MouseEvent event) throws Exception{
		try {
			loadGUI("CartGUI", "ProductsPresentationCSS");
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

	@FXML public void loadUpdateOrder() {
		try {
			loadGUI("UpdateOrderStatusGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}

	@FXML public void loadManualTransaction() {
		try {
			loadGUI("ManualTransactionGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}
}
