package gui.controllers;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import common.MainClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class ParentGUIController implements Initializable {

	@FXML
	protected Button btnExit;

	@FXML
	protected Label lblMsg;
	
	protected Boolean lblMsgState = null, changed = false;
	
	private Thread th = null;
	
	private boolean serverConnected = true;

	public void ExitProg() {
		Context.quitAllGUIs();
		Context.cc.quit();
	}
	
	public void ShowErrorMsg() {
		lblMsgState=false;
		changed=true;
	}

	public synchronized void ShowSuccessMsg() {
		lblMsgState=true;
		changed=true;
	}
	
	protected void clearLblMsg() {
		lblMsgState=null;
		changed=true;
	}

	public boolean isServerConnected() {
		return serverConnected;
	}

	public void setServerConnected(boolean serverConnected) {
		this.serverConnected = serverConnected;
	}
	
	public void serverDown(ActionEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					loadGUI(event, "MainMenuGUI", false, new MainMenuGUIController().getClass());
				} catch (Exception e) {
					lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
			}
		});
	}

	public void backToMainMenu(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/fxmls/MainMenuGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}

	protected void loadGUI(ActionEvent event, String name, 
			boolean withCSS, Class<? extends Object> clss) throws Exception{
		if(event!=null)
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		
		Stage primaryStage = null;
		FXMLLoader loader = null;
		primaryStage = Context.getStageByGUIController(clss);
		
		if(primaryStage!=null)		{//if null, this is the first call for %name%.fxml
			loader = (FXMLLoader) primaryStage.getScene().getUserData();
			primaryStage.show();
		}
		else {
			primaryStage = new Stage();
			loader = new FXMLLoader(getClass().getResource("/gui/fxmls/"+name+".fxml"));
			Pane root = loader.load();
			loader.setRoot(root);
			
			Scene scene = new Scene(root);
			
			if(withCSS==true)
				scene.getStylesheets().add(getClass().getResource("/gui/css/"+name+".css").toExternalForm());
			scene.setUserData(loader);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(name.split("GUI")[0].trim());
			Context.addGUI(clss, primaryStage);
		}
		if(lblMsgState!=null) {
			changed=true;
			lblMsgState=null;
		}
		primaryStage.show();
	}
	
	protected void loadMainMenu(ActionEvent event) {
		try {
			loadGUI(event, "MainMenuuGUI", false, new MainMenuGUIController().getClass());
		} catch (Exception e) {
			System.err.println("Loader failed");
			e.printStackTrace();
		}
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		Task<Void> task = new Task<Void>() {
			  @Override
			  public Void call() throws InterruptedException {
			    while (true) {
			      Platform.runLater(new Runnable() {
			        @Override
			        public void run() {
			        	/*will change when Server sends answer*/
			        	if(changed==true && lblMsgState!=null && lblMsg!=null) {
			        		if(lblMsgState==true)
								lblMsg.setText("Success");
							else if(lblMsgState==true)
								lblMsg.setText("Error");
							changed=false;
			        	}
			        }
			      });
			      Thread.sleep(1000);
			    }
			  }
			};
		th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
}
