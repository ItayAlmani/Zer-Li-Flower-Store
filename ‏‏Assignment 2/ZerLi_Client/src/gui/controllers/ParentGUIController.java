package gui.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSlider;

import common.Context;
import javafx.application.Platform;
import javafx.css.CssMetaData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;

public class ParentGUIController implements Initializable {
	/** The current JavaFX stage <=> the window of the GUI */
	public static Stage primaryStage;
	
	/** The current GUI. Used to deliver the answer from the <code>EchoServer</code>. */
	public static Object currentGUI = null;
	
	private @FXML Pane paneOfScene;
	private Region homePane;
	protected @FXML Label lblMsg, lblTitle;
	private @FXML MenuButton menuProducts,menuCustomersSat,menuOrders;
	private @FXML ImageView imgCart, imgLogOut;
	private @FXML MenuItem miCatalog, miShowProduct, miAddSurvey, miReportSelector, miAssembleProduct, miUpdateOrderStatus,
			miManualTransaction, miSurveyReport;
	private @FXML MenuItem miUpdatePaymentAccount;
	private @FXML MaterialDesignIconView icnLogOut, icnCart;
	private @FXML VBox menu;
	private @FXML BorderPane mainPane;
	private @FXML HBox scenePane;

	@FXML MenuButton menuCustomers;

	public void loadProducts() {
		loadGUI("ProductsFormGUI", false);
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
		loadGUI("AssembleProductGUI", false);
	}
	
	public void loadUpdatePaymentAccount() {
		loadGUI("ShowCustomersGUI", false);
	}

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
			}
		});
		if (Context.order == null)
			Context.askOrder();
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

	private void changeScene(String guiName, String cssName) {
		setMessage("");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/" + guiName + ".fxml"));
			homePane = loader.load();
			homePane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
			if (cssName != null)
				homePane.getStylesheets().add(getClass().getResource("/gui/css/" + cssName + ".css").toExternalForm());
			createScene(guiName, primaryStage);
			primaryStage.show();
			if (primaryStage.getScene() != null) {
				primaryStage.getScene().getWindow().sizeToScene();
				//primaryStage.getScene().getWindow().setHeight(menu.getHeight()+scenePane.getHeight()+100);
			}

			// addMediaPlayer();
		} catch (IOException e1) {
			System.err.println("Loader failed");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Context.mainScene.setMessage("Loader failed");
				}
			});
			e1.printStackTrace();
		}
	}

	private void createScene(String guiName, Stage primaryStage) {
		scenePane.getChildren().clear();
		scenePane.getChildren().add(homePane);
		String[] splitSTR = guiName.split("GUI")[0].trim().split("(?=\\p{Upper})");
		String title = "";
		for (String string : splitSTR)
			title += string + " ";
		this.lblTitle.setText(title);
	}

	private void addMediaPlayer() {
		String musicFile = "/sound/Bana_Cut.mp3";
		Media sound;
		try {
			sound = new Media(getClass().getResource(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void loadGUI(String guiName, String cssName) {
		if (Context.clientConsole == null || Context.clientConsole.isConnected() == false) {
			setServerUnavailable();
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				changeScene(guiName, cssName);
			}
		});
	}

	public void loadGUI(String guiName, boolean withCSS) {
		if (Context.clientConsole == null || Context.clientConsole.isConnected() == false) {
			setServerUnavailable();
			return;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String cssName = null;
				if (withCSS == true)
					cssName = guiName.split("GUI")[0] + "CSS";
				changeScene(guiName, cssName);
			}
		});
	}

	public void loadMainMenu() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loadGUI("MainMenuGUI", false);
			}
		});
	}
	
	public void loadMainMenu(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				loadGUI("MainMenuGUI", false);
				setMessage(msg);
			}
		});
	}

	public void setServerUnavailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				menu.setVisible(false);
				menuProducts.setDisable(true);
				Context.mainScene.setMessage("Connection failed");
			}
		});
		changeScene("ConnectionConfigGUI", null);
	}

	public void setServerAvailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				menuProducts.setDisable(false);
				Context.mainScene.setMessage("");
			}
		});
	}

	public void setMessage(String msg) {
		// Range for readable colors
		double[] color = new double[3];
		double rangeMin = 0.05f, rangeMax = 0.6f;
		for (int i = 0; i < color.length; i++)
			color[i] = rangeMin + (rangeMax - rangeMin) * Math.random();
		this.lblMsg.setTextFill(Color.color(color[0], color[1], color[2]));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblMsg.setText(msg);
			}
		});
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		/*String cartLogoPath = "/images/Shopping_Cart.png", 
				logOutLogoPath = "/images/Log_Out.png";
		imgLogOut.setImage(new Image(getClass().getResourceAsStream(logOutLogoPath)));
		imgCart.setImage(new Image(getClass().getResourceAsStream(cartLogoPath)));
		*/
		mainPane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		DropShadow ds = new DropShadow(30, Color.web("#ffbb2d"));
		menu.setEffect(ds);
		Tooltip.install(icnCart, new Tooltip("Show my cart"));
		Tooltip.install(icnLogOut, new Tooltip("Log Out"));
		Context.mainScene = this;
		if (isServerConnected() == true)
			loadGUI("LogInGUI", false);
		else
			loadConnectionGUI();
		/*th = new Thread(lblMsgThread());
		th.setDaemon(true);
		th.start();*/
	}

	private boolean isServerConnected() {
		if (Context.clientConsole == null || Context.clientConsole.isConnected() == false
				|| Context.dbConnected == false)
			return false;
		return true;
	}

	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/MainScene.fxml"));
		ParentGUIController.primaryStage=stage;
		Scene scene = new Scene(loader.load());
		stage.setTitle("ZerLi Flower Store");
		stage.setScene(scene);
	}

	public void logOut() {
		try {
			logOutUserInSystem();
			this.start(primaryStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
