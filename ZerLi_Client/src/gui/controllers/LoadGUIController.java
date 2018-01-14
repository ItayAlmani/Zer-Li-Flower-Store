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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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

public class LoadGUIController {
	protected @FXML Label lblMsg;
	protected @FXML Pane paneOfScene;
	protected Region homePane;
	protected @FXML Label lblTitle;
	protected @FXML ImageView imgCart, imgLogOut;
	protected @FXML VBox menu;
	protected @FXML BorderPane mainPane;
	protected @FXML HBox scenePane;
	protected @FXML MenuButton menuCustomers;
	protected @FXML JFXComboBox<Store> cbStores;

	protected @FXML OctIconView icnCatalog, icnReportSelector;
	protected @FXML MaterialDesignIconView icnLogOut, icnCart, icnAssemble, icnSurvey, icnSurveyReport, icnComplaints,
			icnProductsForm;
	protected @FXML MaterialIconView icnUpdateOrder, icnManualTransaction;
	protected @FXML	FontAwesomeIconView icnUpdatePaymentAccount;
	protected @FXML HBox hbIcons, hbChangingIcons; 
	protected @FXML VBox hbCustServiceData, hbCustomer, hbOrders, hbCustomersInfo;

	protected @FXML ImageView imgLogo1, imgLogo2;
	
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
		loadGUI("AssembleProductGUI", false);
	}

	public void loadComplaints() {
		loadGUI("ComplaintGUI", false);
	}

	public void loadUpdatePaymentAccount() {
		loadGUI("ShowCustomersGUI", false);
	}

	private void createScene(String guiName, Stage primaryStage) {
		scenePane.getChildren().clear();
		scenePane.getChildren().add(homePane);
		if(guiName.equals("LogInGUI")) {
			this.lblTitle.setText("Zer Li Flower Store");
			return;
		}
		String[] splitSTR = guiName.split("GUI")[0].trim().split("(?=\\p{Upper})");
		String title = "";
		for (String string : splitSTR)
			title += string + " ";
		this.lblTitle.setText(title);
	}

	private void addMediaPlayer() {
		String musicFile = "Bana_Cut.mp3";
		Media sound = new Media(getClass().getResource("/sound/").getPath()+musicFile);
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
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

	private void changeScene(String guiName, String cssName) {
		setMessage("");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/" + guiName + ".fxml"));
			homePane = loader.load();
			homePane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
			if (cssName != null)
				homePane.getStylesheets().add(getClass().getResource("/gui/css/" + cssName + ".css").toExternalForm());
			createScene(guiName, ParentGUIController.primaryStage);
			ParentGUIController.primaryStage.show();
			ParentGUIController.currentGUI=loader.getController();	//put the current scene as currentGUI
			
			if (ParentGUIController.primaryStage.getScene() != null) {
				/*ParentGUIController.primaryStage.getScene().heightProperty().addListener((obs,oldHeight,newHeight)->{
				    System.out.println("Height: " + newHeight);
				});*/
				ParentGUIController.primaryStage.getScene().getWindow().sizeToScene();
				ParentGUIController.primaryStage.sizeToScene();
				//ParentGUIController.primaryStage.getScene().getWindow().setHeight(menu.getHeight()+scenePane.getHeight()+100);
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

	public void setMessage(String msg) {
		// Range for readable colors
		double[] color = new double[3];
		double rangeMin = 0.05f, rangeMax = 0.6f;
		for (int i = 0; i < color.length; i++)
			color[i] = rangeMin + (rangeMax - rangeMin) * Math.random();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblMsg.setTextFill(Color.color(color[0], color[1], color[2]));
				lblMsg.setText(msg);
			}
		});
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
		changeScene("ConnectionConfigGUI", null);
	}

	public void setServerAvailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				hbChangingIcons.setDisable(false);
				if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
					cbStores.setVisible(true);
				menu.setVisible(false);
				Context.mainScene.setMessage("");
			}
		});
	}
	
	public void setServerAvailable(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				hbChangingIcons.setDisable(false);
				if (Context.getUser() != null && Context.getUser().getPermissions().equals(UserType.Customer))
					cbStores.setVisible(true);
				menu.setVisible(false);
				Context.mainScene.setMessage(msg);
			}
		});
	}

}
