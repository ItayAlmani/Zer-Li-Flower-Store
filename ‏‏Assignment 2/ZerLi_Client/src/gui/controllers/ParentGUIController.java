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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public abstract class ParentGUIController implements Initializable {

	protected @FXML Label lblMsg;
	
	protected Boolean lblMsgState = null, changed = false;
	private Thread th = null;
	private boolean serverConnected = true;
	
	public void ShowErrorMsg() {
		lblMsgState=false;
		changed=true;
	}

	public void ShowSuccessMsg() {
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
					loadGUI("MainMenuGUI", false);
				} catch (Exception e) {
					lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
			}
		});
	}
	
	private void changeScene(String guiName, String cssName) throws IOException {
		Stage primaryStage = Context.stage;
		Scene scene = new Scene(
				new FXMLLoader(getClass().getResource("/gui/fxmls/"+guiName+".fxml")).load()
				);
		
		if(cssName!=null)
			scene.getStylesheets().add(getClass().getResource("/gui/css/"+cssName+".css").toExternalForm());
		else
			scene.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(guiName.split("GUI")[0].trim());
		if(lblMsgState!=null) {
			changed=true;
			lblMsgState=null;
		}
		primaryStage.show();
		
		String musicFile = "/sound/Bana_Cut.mp3";
		Media sound;
		try {
			sound = new Media(getClass().getResource(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			//mediaPlayer.play();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void loadGUI(String guiName, String cssName) throws IOException{		
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false) {
			loadMainMenu();
			return;
		}
		changeScene(guiName, cssName);
	}

	protected void loadGUI(String guiName, boolean withCSS) throws IOException{		
		if(Context.clientConsole==null || Context.clientConsole.isConnected()==false) {
			loadMainMenu();
			return;
		}
		String cssName = null;
		if(withCSS==true)
			cssName=guiName.split("GUI")[0]+"CSS";
		
		changeScene(guiName, cssName);
	}
	
	public void loadMainMenu() {
		try {
			loadGUI("MainMenuGUI", false);
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
			        		if(lblMsgState==true) {
								lblMsg.setText("Success");
								lblMsg.setTextFill(javafx.scene.paint.Color.color(Math.random(), Math.random(), Math.random()));
			        		}
							else if(lblMsgState==false) {
								lblMsg.setText("Error");
								lblMsg.setTextFill(javafx.scene.paint.Color.color(Math.random(), Math.random(), Math.random()));
							}
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
