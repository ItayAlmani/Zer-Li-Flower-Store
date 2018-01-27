package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import common.gui.controllers.ParentGUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The class which extends {@link Application} and contains:
 * <ul>
 	* <li>{@link #main(String[])}</li>
	* <li>{@link Application#launch(String...)}</li>
 * </ul>
 */
public class MainClient extends Application {
	private Stage stage;
	
	/**The name of the image of the logo of the app.<br>
	 *logoRelativePath = {@value}*/
	public final static String logoRelativePath = "/common/images/logos/logo3.gif";
	
	
	public static void main(String args[]) throws IOException {
		launch(args);
	} // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ParentGUIController main = new ParentGUIController();
        initApp(primaryStage, main);
		main.start(primaryStage);
	}

	/**
	 * Initializing the whole project before GUI loading up
	 * @param primaryStage the primary stage of the app
	 * @param mainCtrl the main controller which be start first
	 * @throws IOException {@link #getLogoAsImage()}
	 */
	private void initApp(Stage primaryStage, ParentGUIController mainCtrl) throws IOException {
		//Setting up listener when the primaryStage closing
		this.stage=primaryStage;
		primaryStage.setOnCloseRequest(confirmCloseEventHandler);
		
		//Setting up primaryStage dimensions
		primaryStage.setHeight(500);
		primaryStage.setWidth(500);
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY(0);
        primaryStage.setHeight(primScreenBounds.getMaxY());
        primaryStage.setMaxHeight(primScreenBounds.getMaxY());
        primaryStage.setMaximized(true);
        //primaryStage.resizableProperty().setValue(Boolean.FALSE);
		
		primaryStage.getIcons().add(getLogoAsImage());
		
        //primaryStage.show();
	}

	/**
	 * {@link EventHandler} which pop an {@link Dialog} when {@link #stage} being asked
	 * to close. The {@link Dialog} will confirm that the user want to exit the app.<br>
	 * If confirmed, the app will disconnect the {@link ClientConsole} and call {@link #deleteAllImages()}.
	 */
	private final EventHandler<WindowEvent> confirmCloseEventHandler = winEvent -> {
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
			Platform.exit();
			if (Context.clientConsole != null)
				Context.clientConsole.quit();
		}
	};
	
	/**
	 * Creating new {@link java.io.InputStream} with path {@link #logoRelativePath}
	 * @return new {@link Image} with content loaded from {@value #logoRelativePath}
	 * @throws IOException {@link java.io.InputStream#close()} failed
	 */
	public static Image getLogoAsImage() throws IOException {
		InputStream is = MainClient.class.getResourceAsStream(logoRelativePath);
		Image img = new Image(is);
		is.close();
		return img;
	}
}
