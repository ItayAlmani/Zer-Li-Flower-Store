package gui.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXComboBox;

import common.Context;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import entities.Store;
import entities.User.UserType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoadGUIController {
	public @FXML Label lblMsg;
	protected @FXML Pane paneOfScene;
	protected Region homePane;
	protected @FXML Label lblTitle;
	protected @FXML ImageView imgCart, imgLogOut;
	protected @FXML VBox menu;
	protected @FXML BorderPane mainPane;
	protected @FXML HBox scenePane, panePA, paneNoPA;
	protected @FXML MenuButton menuCustomers;
	protected @FXML JFXComboBox<Store> cbStores;

	protected @FXML OctIconView icnCatalog, icnReportSelector;
	protected @FXML MaterialDesignIconView icnLogOut, icnCart, icnAssemble, icnSurvey, icnSurveyReport, icnComplaints,
			icnProductsForm;
	protected @FXML MaterialIconView icnUpdateOrder, icnManualTransaction, icnCancelOrder, icnUpdatePaymentAccount,
									icnSub;
	protected @FXML	FontAwesomeIconView icnAccounts;
	protected @FXML HBox hbIcons, hbChangingIcons, hbCustomer; 
	protected @FXML VBox paneCustServiceData, paneCustomer, paneOrders, paneCustomersInfo;

	protected @FXML ImageView imgLogo1, imgLogo2;
	
	/** The string which will be use to show error message to the user.<br>
 	{@code errMsg = }{@value #errMsg}
	 */
	protected final static String errMsg =  "Error";
	
	/** The string which will be use to show success message to the user.<br>
		{@code sucMsg = }{@value #sucMsg}
	 */
	protected final static String sucMsg =  "Success";
	
	public void loadProducts() {
		loadGUI("UpdateCatalogGUI", false);
	}

	public void loadSurvey() {
		loadGUI("SurveyGUI", false);
	}

	public void loadSurveyReport() {
		loadGUI("SurveyReportGUI", false);
	}

	public void loadConnectionGUI() {
		loadGUI("ConnectionConfigGUI", false);
	}
	
	public void loadCancelOrder() {
		loadGUI("CancelOrderGUI", false);
	}

	public void loadCatalog() {
		loadGUI("CatalogGUI", "ProductsPresentationCSS");
	}

	public void loadCart() {
		if (Context.order != null)
			loadGUI("CartGUI", "ProductsPresentationCSS");
		else
			setMessage("Try again");
	}

	public void loadReportSelector() {
		loadGUI("ReportSelectorGUI", false);
	}

	public void loadUpdateOrder() {
		loadGUI("UpdateOrderStatusGUI", false);
	}

	public void loadManualTransaction() {
		loadGUI("ManualTransactionGUI", false);
	}

	public void loadAssembleProduct() {
		loadGUI("AssembleProductGUI", "ProductsPresentationCSS");
	}

	public void loadComplaints() {
		loadGUI("ComplaintGUI", false);
	}

	public void loadUpdatePaymentAccount() {
		loadGUI("PaymentAccountManagmentGUI", false);
	}
	
	public void loadSub() {
		loadGUI("SubscriptionGUI", false);
	}
	
	public void loadAccountsManage() {
		loadGUI("UpdateUserGUI", false);
	}

	private void createScene(String guiName, Stage primaryStage) {
		scenePane.getChildren().clear();
		scenePane.getChildren().add(homePane);
		if(guiName.equals("LogInGUI")) {
			this.lblTitle.setText("Zer Li Flower Store");
			return;
		}
		if(!this.lblTitle.getText().isEmpty())
			return;
		String[] splitSTR = guiName.split("GUI")[0].trim().split("(?=\\p{Upper})");
		String title = "";
		for (String string : splitSTR)
			title += string + " ";
		this.lblTitle.setText(title);
	}

	private void addMediaPlayer() {
		String musicFile = "Bana_Cut.mp3";
		Media sound = new Media(getClass().getResource("/sound/"+musicFile).getPath());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	public void loadGUI(String guiName, String cssName) {
		if (Context.clientConsole == null || Context.clientConsole.isConnected() == false) {
			setServerUnavailable();
			return;
		}
		if(Platform.isFxApplicationThread())
			changeScene(guiName, cssName);
		else
			Platform.runLater(()->changeScene(guiName, cssName));
	}

	private void changeScene(String guiName, String cssName) {
		setMessage("");
		setTitle("");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/" + guiName + ".fxml"));
			homePane = loader.load();
			ParentGUIController.currentGUI=loader.getController();	//put the current scene as currentGUI
			homePane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
			if (cssName != null)
				homePane.getStylesheets().add(getClass().getResource("/gui/css/" + cssName + ".css").toExternalForm());
			createScene(guiName, ParentGUIController.primaryStage);
			if(ParentGUIController.primaryStage.isShowing()==false)
				ParentGUIController.primaryStage.show();
			//Scene sce = ParentGUIController.primaryStage.getScene();
			//if (sce != null) {
				/*sce.heightProperty().addListener((obs,oldHeight,newHeight)->{
				    System.out.println("Height: " + newHeight);
				});*/
				//ParentGUIController.primaryStage.sizeToScene();
				/*if(sce.getWindow() != null)
					sce.getWindow().sizeToScene();*/
				//sce.getWindow().setHeight(menu.getHeight()+scenePane.getHeight()+100);
			//}

			 //addMediaPlayer();
		} catch (IOException e1) {
			System.err.println("Loader failed");
			Context.mainScene.setMessage("Loader failed");
			e1.printStackTrace();
		}
	}

	public void loadGUI(String guiName, boolean withCSS) {
		if (guiName != null && guiName.equals("ConnectionConfigGUI")==false &&
				(Context.clientConsole == null || Context.clientConsole.isConnected() == false)) {
			setServerUnavailable();
			return;
		}
		String cssName = null;
		if (withCSS == true)
			cssName = guiName.split("GUI")[0] + "CSS";
		if(Platform.isFxApplicationThread())
			changeScene(guiName, cssName);
		else {
			final String cssFinalName = cssName;
			Platform.runLater(()->changeScene(guiName, cssFinalName));
		}
	}

	public void loadMainMenu() {
		loadGUI("MainMenuGUI", false);
	}

	public void loadMainMenu(String msg) {
		loadGUI("MainMenuGUI", false);
		setMessage(msg);
	}

	public void setMessage(String msg) {
		// Range for readable colors
		double[] color = new double[3];
		double rangeMin = 0.05f, rangeMax = 0.6f;
		for (int i = 0; i < color.length; i++)
			color[i] = rangeMin + (rangeMax - rangeMin) * Math.random();
		Color c = Color.color(color[0], color[1], color[2]);
		if(Platform.isFxApplicationThread())
			showTextInLblMsg(msg,c);
		else
			Platform.runLater(()->showTextInLblMsg(msg,c));
	}
	
	private void showTextInLblMsg(String msg, Color color) {
		lblMsg.setTextFill(color);
		String lmsg = lblMsg.getText();
		if(msg==null)	msg="";
		
		//lblMsg not empty
		if(!lmsg.isEmpty()) {
			//if we have generic message
			if(msg.equals(errMsg) || msg.equals(sucMsg)) {
				String opp = msg.equals(errMsg)?sucMsg:errMsg;	//the opposite message from msg
				//we will delete the opposite and then show the msg
				if(lmsg.contains(opp)) {	
					lblMsg.setText(lmsg.replaceAll("\\s*\\b"+opp+"\\b\\s*", msg));
					return;
				}
				if(!lmsg.contains(msg))
					lblMsg.setText(lmsg+".\t"+msg);
			}
			else if(!lmsg.toLowerCase().contains(msg.toLowerCase()))
				lblMsg.setText(lmsg+".\t"+msg);
		}
		if(lmsg.isEmpty() || msg == null || msg.equals(""))
			lblMsg.setText(msg);
	}

	public void setServerUnavailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				menu.setVisible(false);
				if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
					cbStores.setVisible(false);
				hbChangingIcons.setDisable(true);
				Context.mainScene.setMessage("Connection failed");
			}
		});
		//changeScene("ConnectionConfigGUI", null);
		loadConnectionGUI();
	}

	public void setServerAvailable() {
		Context.mainScene.setMessage("");
		if(Platform.isFxApplicationThread()) {
			menu.setVisible(false);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		}
		Platform.runLater(()->{
			menu.setVisible(false);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		});
	}
	
	public void setServerAvailable(String msg) {
		Context.mainScene.setMessage(msg);
		if(Platform.isFxApplicationThread()) {
			menu.setVisible(false);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		}
		Platform.runLater(()->{
			menu.setVisible(false);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		});
	}

	public void setTitle(String title) {
		if(Platform.isFxApplicationThread())
			lblTitle.setText(title);
		else
			Platform.runLater(()->lblTitle.setText(title));
	}
}
