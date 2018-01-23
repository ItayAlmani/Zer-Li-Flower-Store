package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import common.ClientController;
import common.Context;
import entities.Order;
import entities.Store;
import entities.User;
import entities.User.UserType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The Controller of {@code MainScene.fxml}. This controller handles the fxml itself.<br>
 * The controller extends {@linkplain SetUpMainGUIController}
 * @see {@linkplain SetUpMainGUIController}
 */
public class ParentGUIController extends SetUpMainGUIController implements Initializable {
	/** The current JavaFX stage <=> the window of the GUI */
	public static Stage primaryStage;
	
	/** The current GUI. Used to deliver the response from {@code EchoServer}. */
	public static Object currentGUI = null;
	
	/** Will show {@link #errMsg} ({@value #errMsg}) to the user in {@link LoadGUIController#lblMsg} */
	public void ShowErrorMsg() {
		Context.mainScene.setMessage(errMsg);
	}

	/** Will show {@link #sucMsg} ({@value #sucMsg}) to the user in {@link LoadGUIController#lblMsg}*/
	public void ShowSuccessMsg() {
		Context.mainScene.setMessage(sucMsg);
	}

	/**
	 * After the log in will occur successfully, the menu will be adjusted appropriately.<br>
	 * At the end, {@link #loadMainMenu()} will occur.
	 */
	public void logInSuccess() {
		if(Platform.isFxApplicationThread())
			setUpMenus();
		Platform.runLater(()->setUpMenus());
		loadMainMenu();
	}
	
	private void setUpMenus() {
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
			menu.setVisible(true);
		}
	}

	/** Will clear the message in {@link LoadGUIController#lblMsg}*/
	public void clearMsg() {
		Context.mainScene.setMessage("");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			setUpInit();
			mainPane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
			DropShadow ds = new DropShadow(30, Color.web("#ffbb2d"));
			menu.setEffect(ds);
			setUpToolTips();
			Context.mainScene = this;
			currentGUI=this;
		} catch (IOException e) {
			e.printStackTrace();
			ShowErrorMsg();
		}
	}
	
	/**
	 * Invoked by {@link ClientController} when the {@link ParentGUIController} asked for
	 * the status of the data base.
	 * @param dbStatus indicates if the data base connected at the server side {@code (true)} or not {@code (false)}
	 */
	public void setDBStatus(Boolean dbStatus) {
		if (isServerConnected() == true)
			loadGUI("LogInGUI", false);
		else
			loadConnectionGUI();
	}

	/**
	 * Checks if the {@code client} is connected to the {@code server}, and the {@code data base} connected too.
	 * @return {@code true} if the {@code client} is connected to the {@code server}, and the {@code data base} connected
	 * too, else {@code false} 
	 */
	private boolean isServerConnected() {
		return Context.clientConsole != null && Context.clientConsole.isConnected() && ClientController.dbConnected;
	}

	/**
	 * The function which will load the {@link Application}.<br>
	 * @see {@link Application#start(Stage)}
	 * @param stage the {@link Stage} of the {@link Application}
	 * @throws IOException when can't invoke {@link FXMLLoader#load()}
	 */
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/MainScene.fxml"));
		ParentGUIController.primaryStage=stage;
		ScrollPane root = loader.load();
		root.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		stage.setTitle("ZerLi Flower Store");
		stage.setScene(scene);

		try {
			ClientController.connectToServer();
			if (Context.clientConsole != null && Context.clientConsole.isConnected() == true) {
				try {
					//will return the answer to ParentGUIController.setDBStatus()
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

	/**
	 * The {@link User} will disconnected from the {@link Application}, and the {@link Application}
	 * will restart.
	 */
	public void logOut() {
		try {
			logOutUserInSystem();
			primaryStage.hide();
			this.start(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Marks the {@link User} as connected, to prevent more Log Ins in other devices.
	 */
	public void logOutUserInSystem() {
		if (Context.getUser() != null) {
			Context.getUser().setConnected(false);
			// set user as logged out
			/*try {
				Context.fac.user.update(Context.getUser());
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				e.printStackTrace();
			}*/
		}
	}

	/**
	 * the function asks for existing {@link Order} by {@code cbStores value}.<br>
	 * Additionally, the function will invoke {@link LoadGUIController#loadMainMenu()}
	 * by {@code loadHomePage}.
	 * @param loadHomePage if true, {@link MainMenuGUIController} will be loaded.
	 */
	public void getNewOrderByStore(boolean loadHomePage) {
		if(cbStores.getValue()!=null) {
			Context.askOrder(cbStores.getValue());
			if(loadHomePage)
				loadMainMenu();
    	}
	}
	
	/**
	 * When {@link Store} in the {@link LoadGUIController#cbStores} is changed, the function will
	 * invoke {@link #getNewOrderByStore(boolean)}
	 */
	public void storeChanged() {
		getNewOrderByStore(true);
	}
}
