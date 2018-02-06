package common.gui.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.Context;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import orderNproducts.entities.Store;
import usersInfo.entities.User.UserType;

public class LoadGUIController {
	public @FXML Label lblMsg;
	protected @FXML Pane paneOfScene;
	protected Region homePane;
	protected @FXML Label lblTitle, lblUserWelcome;
	protected @FXML ImageView imgCart, imgLogOut;
	protected @FXML VBox menu;
	protected @FXML BorderPane mainPane;
	protected @FXML HBox scenePane, panePA, paneNoPA, paneStore, paneStrManager, hbCustomer, hbIcons;
	public @FXML HBox hbChangingIcons;
	protected @FXML MenuButton menuCustomers;
	protected @FXML JFXComboBox<Store> cbStores;

	protected @FXML OctIconView icnCatalog, icnReportSelector;
	protected @FXML MaterialDesignIconView icnLogOut, icnCart, icnAssemble, icnSurvey, icnSurveyReport, icnComplaints,
			icnProductsForm;
	protected @FXML MaterialIconView icnUpdateOrder, icnManualTransaction, icnCancelOrder, icnUpdatePaymentAccount,
									icnSub;
	protected @FXML	FontAwesomeIconView icnAccounts;
	protected @FXML VBox paneCustServiceData, paneOrders, paneCustomersInfo;
	
	protected @FXML JFXButton btnReportSelector, btnSurveyReport, btnComplaints, btnUpdateCatalog, btnUpdateUsers;

	protected @FXML ImageView imgLogo1, imgLogo2;
	
	/** The string which will be use to show error message to the user.<br>
 	{@code errMsg = }{@value #errMsg}
	 */
	protected final static String errMsg =  "Error";
	
	/** The string which will be use to show success message to the user.<br>
		{@code sucMsg = }{@value #sucMsg}
	 */
	protected final static String sucMsg =  "Success";
	
	/** The path of the shared css files<br>
	 * css_path={@value}*/ 
	public final static String css_path = "/common/gui/css/";
	
	/** The path of the shared fxml files<br>
	 *fxml_path={@value}*/ 
	protected final static String fxml_path = "/common/gui/fxmls/";
	/**
	 * Function that Loads The update catalog GUI
	 */
	public void loadProducts() {
		loadGUI("UpdateCatalogGUI", false);
	}
	/**
	 * Function that Loads the survey GUI
	 */
	public void loadSurvey() {
		loadGUI("SurveyGUI", false);
	}
	/**
	 * Function that Loads the Survey Report GUI
	 */
	public void loadSurveyReport() {
		loadGUI("SurveyReportGUI", false);
	}
	/**
	 * Function that Loads the Connection Configuration GUI
	 */
	public void loadConnectionGUI() {
		loadGUI("ConnectionConfigGUI", false);
	}
	/**
	 * Function that Loads the Cancel Order GUI
	 */
	public void loadCancelOrder() {
		loadGUI("CancelOrderGUI", false);
	}
	/**
	 * Function that Loads the Catalog GUI with specific CSS ProductsPresentationCSS
	 */
	public void loadCatalog() {
		loadGUI("CatalogGUI", "ProductsPresentationCSS");
	}
	/**
	 * Function that Loads the Cart GUI with specific CSS ProductsPresentationCSS
	 */
	public void loadCart() {
		if (Context.order != null)
			loadGUI("CartGUI", "ProductsPresentationCSS");
		else
			setMessage("Try again");
	}
	/**
	 * Function that Loads the Report Selector GUI
	 */
	public void loadReportSelector() {
		loadGUI("ReportSelectorGUI", false);
	}
	/**
	 * Function that Loads the Update Order Status GUI
	 */
	public void loadUpdateOrder() {
		loadGUI("UpdateOrderStatusGUI", false);
	}
	/**
	 * Function that Loads the Manual Transaction GUI
	 */
	public void loadManualTransaction() {
		loadGUI("ManualTransactionGUI", false);
	}
	/**
	 * Function that Loads the Assemble Product GUI with specific CSS ProductsPresentationCSS
	 */
	public void loadAssembleProduct() {
		loadGUI("AssembleProductGUI", "ProductsPresentationCSS");
	}
	/**
	 * Function that Loads the Complaint GUI
	 */
	public void loadComplaints() {
		loadGUI("ComplaintGUI", false);
	}
	/**
	 * Function that Loads the PaymentAccountManagmentGUI
	 */
	public void loadUpdatePaymentAccount() {
		loadGUI("PaymentAccountManagmentGUI", false);
	}
	/**
	 * Function that Loads the Subscription GUI
	 */
	public void loadSub() {
		loadGUI("SubscriptionGUI", false);
	}
	/**
	 * Function that Loads the Update User GUI
	 */
	public void loadAccountsManage() {
		loadGUI("UpdateUserGUI", false);
	}
	/**
	 * Function that Loads Order Time GUI
	 */
	public void loadOrderTime() {
		loadGUI("OrderTimeGUI", false);
	}
	/**
	 * Function that Loads Payment GUI
	 */
	public void loadPayment() {
		loadGUI("PaymentGUI", false);
	}
	/**
	 * Function that Loads Delivery GUI
	 */
	public void loadDelivery() {
		loadGUI("DeliveryGUI", false);
	}
	/**
	 * Function that Loads Order GUI
	 */
	public void loadOrderDetails() {
		loadGUI("OrderGUI", false);
	}
	/**
	 * Function that creates the scene of LogIN
	 * @param guiName - represent the Gui Name
	 * @param primaryStage - represent the current primary stage
	 */
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
	/**
	 * A "generic" function that processes the loading process according to the details it receives
	 *  and knows how to categorize by which screen to load now
	 * @param guiName - the name of the gui that needed to load now
	 * @param cssName - load this specific CSS
	 */
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
	/**
	 * Function that changes between scenes
	 * @param guiName - the scene that need to be loaded
	 * @param cssName - optional, load specific CSS
	 */
	private void changeScene(String guiName, String cssName) {
		setMessage("");
		setTitle("");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml_path + guiName + ".fxml"));
			homePane = loader.load();
			ParentGUIController.currentGUI=loader.getController();	//put the current scene as currentGUI
			homePane.getStylesheets().add(getClass().getResource(css_path+"ParentCSS.css").toExternalForm());
			if (cssName != null)
				homePane.getStylesheets().add(getClass().getResource(css_path + cssName + ".css").toExternalForm());
			createScene(guiName, ParentGUIController.primaryStage);
			if(ParentGUIController.primaryStage.isShowing()==false)
				ParentGUIController.primaryStage.show();
		} catch (IOException e1) {
			System.err.println("Loader failed");
			Context.mainScene.setMessage("Loader failed");
			e1.printStackTrace();
		}
	}
	/**
	 * A "generic" function that processes the loading process according to the details it receives
	 *  and knows how to categorize by which screen to load now
	 * @param guiName - the name of the gui that needed to load now
	 * @param cssName - if the user want or not to change the default CSS
	 */
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
	/**
	 * Function that loads the main menu
	 */
	public void loadMainMenu() {
		loadGUI("MainMenuGUI", false);
	}
	/**
	 * Function that loads the main menu
	 * @param msg - message that shown in the screen
	 */
	public void loadMainMenu(String msg) {
		loadGUI("MainMenuGUI", false);
		setMessage(msg);
	}
	/**
	 * Function that set a message in the screen
	 * @param msg - the string you want to display
	 */
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
	/**
	 * Function that sets the lblmsg with a requested string
	 * @param msg- the requested message
	 * @param color - the color for the lyrics
	 */
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
	/**
	 * Shows if the server is not connected and calls to the new connection GUI
	 */
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
	/**
	 * Function that tells if the server is available
	 */
	public void setServerAvailable() {
		Context.mainScene.setMessage("");
		if(Platform.isFxApplicationThread()) {
			menu.setVisible(true);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		}
		Platform.runLater(()->{
			menu.setVisible(true);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		});
	}
	/**
	 * Function that tells if the server is available
	 * @param msg - message to be shown in the screen
	 */
	public void setServerAvailable(String msg) {
		Context.mainScene.setMessage(msg);
		if(Platform.isFxApplicationThread()) {
			menu.setVisible(true);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		}
		Platform.runLater(()->{
			menu.setVisible(true);
			hbChangingIcons.setDisable(false);
			if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
				cbStores.setVisible(true);
		});
	}
	/**
	 * Function that set the title of the screen
	 * @param title - the requested title
	 */
	public void setTitle(String title) {
		if(Platform.isFxApplicationThread())
			lblTitle.setText(title);
		else
			Platform.runLater(()->lblTitle.setText(title));
	}
}
