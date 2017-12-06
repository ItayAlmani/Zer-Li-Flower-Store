package gui;

import java.io.IOException;
import java.io.Serializable;

import Server.EchoServer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class TemplateGUI implements Serializable {

	@FXML
	protected Button btnExit;

	@FXML
	protected Label lblMsg;
	
	private boolean serverConnected = true;

	public void ExitProg(ActionEvent event) throws Exception {
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
		System.exit(0);
	}

	public void ShowErrorMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblMsg.setText("Error with DB");
			}
		});
	}

	public void ShowSuccessMsg() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblMsg.setText("Success");
			}
		});
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
				Stage primaryStage = new Stage();
				Pane root;
				try {
					root = FXMLLoader.load(getClass().getResource("/gui/MainMenuGUI.fxml"));
					((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
					Scene scene = new Scene(root);
					primaryStage.setScene(scene);		
					primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
