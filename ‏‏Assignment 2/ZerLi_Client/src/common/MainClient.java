package common;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kfir.gui.controllers.LogInGUIController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import gui.controllers.*;

public class MainClient extends Application {
	private Stage stage;
	public static void main(String args[]) throws IOException {
		try {
			Context.connectToServer();
		} catch (IOException e) {
			System.err.println("Connecting to server failed");
		}
		if (Context.clientConsole != null && Context.clientConsole.isConnected() == true) {
			try {
				Context.fac.dataBase.getDBStatus();
			} catch (IOException e) {
				System.err.println("Can't get data base status");
			}
		}
		launch(args);

	} // end main

	@Override
	public void start(Stage primaryStage) throws Exception {
		ParentGUIController main = new ParentGUIController();
		this.stage=primaryStage;
		primaryStage.setOnCloseRequest(confirmCloseEventHandler);
		primaryStage.setHeight(500);
		primaryStage.setWidth(500);
		for (int i = 0; i <= 5; i++)
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logos/img/logo3-" + i + ".png")));
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY(0);
        primaryStage.setMaxHeight(primScreenBounds.getMaxY());
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
		main.start(primaryStage);
	}
	
	


	private EventHandler<WindowEvent> confirmCloseEventHandler = winEvent -> {
		Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
		Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("Exit");
		closeConfirmation.setHeaderText("Confirm Exit");
		closeConfirmation.initModality(Modality.APPLICATION_MODAL);
		closeConfirmation.initOwner(stage);

		closeConfirmation.setX(stage.getX());
		closeConfirmation.setY(stage.getY());

		Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get()))
			winEvent.consume();
		else {
			if (Context.mainScene != null)
				Context.mainScene.logOutUserInSystem();
			if (Context.clientConsole != null)
				Context.clientConsole.quit();
		}
	};
}
