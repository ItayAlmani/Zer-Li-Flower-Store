package gui.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ParentGUIController implements Initializable {

	private @FXML Pane paneOfScene;
	private Region homePane;
	protected @FXML Label lblMsg;
	
	protected Boolean lblMsgState = null, changed = false;
	private Thread th = null;
	
	private @FXML MenuButton menuProducts;
	private @FXML ImageView imgCart,imgLogOut;
	private @FXML MenuItem	miCatalog, miShowProduct, miAddSurvey,
							miReportSelector, miAssembleItem, miUpdateOrderStatus,
							miManualTransaction,miSurveyReport;
	
	private String cartLogoPath="/images/Shopping_Cart.png", logOutLogoPath="/images/Log_Out.png";
	private @FXML VBox vbxMenu, mainVBox;
		
	public void showProducts(){		
		loadGUI("ProductsFormGUI", false);
	}
	public void showSurvey(){
		loadGUI("SurveyGUI", false);
	}
	public void showSurveyReport(){
		loadGUI("SurveyReportGUI", false);
	}
	public void showConnectionGUI(){
		loadGUI("ConnectionConfigGUI", false);
	}
	
	public void showCatalog(){
		loadGUI("CatalogGUI", "ProductsPresentationCSS");
	}
	
	public void showCart(){
		if(Context.order!=null)
			loadGUI("CartGUI", "ProductsPresentationCSS");
		else
			setMessage("Try again");
	}

	public void showReportSelector() {
		loadGUI("ReportSelectorGUI", false);
	}

	public void loadUpdateOrder() {
		loadGUI("UpdateOrderStatusGUI", false);
	}

	public void loadManualTransaction() {
		loadGUI("ManualTransactionGUI", false);
	}
	
	public void ShowErrorMsg() {
		/*lblMsgState=false;
		changed=true;*/
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Context.mainScene.setMessage("Error");
			}
		});
	}

	public void ShowSuccessMsg() {
		/*lblMsgState=true;
		changed=true;*/
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
				vbxMenu.setVisible(true);
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
		});
		if(Context.order==null)
			Context.askOrder();
		loadMainMenu();
	}
	
	protected void clearLblMsg() {
		/*lblMsgState=null;
		changed=true;*/
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Context.mainScene.setMessage("");
			}
		});
	}
	
	private void changeScene(String guiName, String cssName) {
		Stage primaryStage = Context.stage;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/"+guiName+".fxml"));
			homePane = loader.load();
			homePane.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
			if(cssName!=null)
				homePane.getStylesheets().add(getClass().getResource("/gui/css/"+cssName+".css").toExternalForm());
			paneOfScene.getChildren().clear();
			paneOfScene.getChildren().add(homePane);
			if(primaryStage.getScene()!=null)
				primaryStage.getScene().getWindow().sizeToScene();
			
			primaryStage.setTitle(guiName.split("GUI")[0].trim());
			if(lblMsgState!=null) {
				changed=true;
				lblMsgState=null;
			}
			primaryStage.show();
			
			//addMediaPlayer();
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
	
	public void loadGUI(String guiName, String cssName){		
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false) {
			setServerUnavailable();
			return;
		}
		changeScene(guiName, cssName);
	}

	public void loadGUI(String guiName, boolean withCSS){		
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false) {
			setServerUnavailable();
			return;
		}
		String cssName = null;
		if(withCSS==true)
			cssName=guiName.split("GUI")[0]+"CSS";
		
		changeScene(guiName, cssName);
	}
	
	public void loadMainMenu() {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	    		loadGUI("MainMenuGUI", false);
	        }
		});
	}
	
	public void setServerUnavailable() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbxMenu.setVisible(false);
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
		//Range for readable colors
		double[] color= new double[3];
		double rangeMin=0.05f,rangeMax=0.6f;
		for (int i = 0; i < color.length; i++)
			color[i]= rangeMin + (rangeMax - rangeMin) *Math.random();
		this.lblMsg.setTextFill(Color.color(color[0],color[1],color[2]));
		this.lblMsg.setText(msg);
	}
	
	public void initialize(URL location, ResourceBundle resources) {		
		imgLogOut.setImage(new Image(getClass().getResourceAsStream(logOutLogoPath)));
		Tooltip.install(imgCart, new Tooltip("Log Out"));
		imgCart.setImage(new Image(getClass().getResourceAsStream(cartLogoPath)));
		Tooltip.install(imgCart, new Tooltip("Show my cart"));
		Context.mainScene=this;
		if(isServerConnected()==true)
			loadGUI("LogInGUI", false);
		else
			showConnectionGUI();
		th = new Thread(lblMsgThread());
		th.setDaemon(true);
		th.start();
	}
	
	private boolean isServerConnected() {
		if(Context.clientConsole==null || 
				Context.clientConsole.isConnected()==false ||
				Context.dbConnected == false)
			return false;
		return true;
	}
	
	private Task<Void> lblMsgThread(){
		return new Task<Void>() {
			  @Override
			  public Void call() throws InterruptedException {
			    while (true) {
			      Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	/*will change when Server sends answer*/
			        	if(changed==true && lblMsgState!=null && lblMsg!=null) {
			        		if(lblMsgState==true) {
								Context.mainScene.setMessage("Success");
			        		}
							else if(lblMsgState==false) {
								Context.mainScene.setMessage("Error");
							}
							changed=false;
			        	}
			        }
			      });
			      Thread.sleep(1000);
			    }
			  }
			};
	}
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/MainScene.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("ZerLi Flower Store");
		stage.setScene(scene);
	}
	public void logOut() {
		try {
			logOutUserInSystem();
			this.start(Context.stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void logOutUserInSystem() {
		if(Context.getUser()!=null) {
			Context.getUser().setConnected(false);
			//set user as logged out
			Context.fac.user.updateUser(Context.getUser());
		}
	}
}
