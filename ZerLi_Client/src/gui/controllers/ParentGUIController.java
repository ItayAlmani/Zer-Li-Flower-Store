package gui.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.ClientController;
import common.Context;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import entities.Customer;
import entities.PaymentAccount;
import entities.Store;
import entities.User.UserType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class ParentGUIController extends SetUpMainGUIController implements Initializable {
	/** The current JavaFX stage <=> the window of the GUI */
	public static Stage primaryStage;
	
	/** The current GUI. Used to deliver the answer from the <code>EchoServer</code>. */
	public static Object currentGUI = null;
	
	public void ShowErrorMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Context.mainScene.setMessage("Error");
			}
		});
	}

	public void ShowSuccessMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Context.mainScene.setMessage("Success");
			}
		});
	}

	public void logInSuccess() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				menu.setVisible(true);
				if(Context.getUser()!=null) {
					UserType perm = Context.getUser().getPermissions();
					switch (perm) {
					case Customer:
						setUpCustomerMenu();
						break;
					case StoreWorker:
						setUpStoreWorkerMenu();
						break;
					case StoreManager:
						setUpStoreManagerMenu();
						break;
					case ServiceExpert:
						setUpServiceExpertMenu();
						break;
					case CustomerServiceWorker:
						setUpCustomerServiceWorkerMenu();
						break;
					case ChainStoreWorker:
						setUpChainStoreWorkerMenu();
						break;
					case ChainStoreManager:
						setUpChainStoreManagerMenu();
						break;
					default:
						break;
					}
				}
			}
		});
		loadMainMenu();
	}

	protected void clearLblMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Context.mainScene.setMessage("");
			}
		});
	}

	public void initialize(URL location, ResourceBundle resources) {
		setUpInit();
		mainPane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		DropShadow ds = new DropShadow(30, Color.web("#ffbb2d"));
		menu.setEffect(ds);
		setUpToolTips();
		Context.mainScene = this;
		currentGUI=this;
	}
	
	public void setDBStatus(Boolean dbStatus) {
		Platform.runLater(()->{
			if (isServerConnected() == true)
				loadGUI("LogInGUI", false);
			else
				loadConnectionGUI();
		});
	}

	private boolean isServerConnected() {
		if (Context.clientConsole == null || Context.clientConsole.isConnected() == false
				|| ClientController.dbConnected == false)
			return false;
		return true;
	}

	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/MainScene.fxml"));
		ParentGUIController.primaryStage=stage;
		Scene scene = new Scene(loader.load());
		stage.setTitle("ZerLi Flower Store");
		stage.setScene(scene);
		//stage.show();

		try {
			ClientController.connectToServer();
			if (Context.clientConsole != null && Context.clientConsole.isConnected() == true) {
				try {
					Context.fac.dataBase.getDBStatus();
				} catch (IOException e) {
					System.err.println("Can't get data base status");
				}
			}
		} catch (IOException e) {
			System.err.println("Connecting to server failed!!");
			((ParentGUIController)loader.getController()).setServerUnavailable();
		}
	}

	public void logOut() {
		try {
			logOutUserInSystem();
			this.start(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logOutUserInSystem() {
		if (Context.getUser() != null) {
			Context.getUser().setConnected(false);
			// set user as logged out
			Context.fac.user.update(Context.getUser());
		}
	}

	public void getNewOrderByStore() {
		if(cbStores.getValue()!=null) {
			Context.askOrder(cbStores.getValue());
			loadMainMenu();
    	}
	}
	
	/* 
	private Thread th = null;
	protected Boolean lblMsgState = null, changed = false;
	private Task<Void> lblMsgThread() {
		return new Task<Void>() {
			@Override
			public Void call() throws InterruptedException {
				while (true) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							 will change when Server sends answer 
							if (changed == true && lblMsgState != null && lblMsg != null) {
								if (lblMsgState == true) {
									Context.mainScene.setMessage("Success");
								} else if (lblMsgState == false) {
									Context.mainScene.setMessage("Error");
								}
								changed = false;
							}
						}
					});
					Thread.sleep(1000);
				}
			}
		};
	}*/
}
