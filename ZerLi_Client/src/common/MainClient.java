package common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import gui.controllers.ParentGUIController;
import javafx.application.Application;
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

public class MainClient extends Application {
	private Stage stage;
	public final static String logoName = "logo3.gif";
	
	/**The path to the project/JAR + /temp/.<br>
	 *tempDir = {@value}*/
	public final static String tempDir = System.getProperty("user.dir") + "\\temp\\";
	
	/**The path to {@link #tempDir} + */
	public final static String imagesPath = tempDir+"images\\";
	
	public static void main(String args[]) throws IOException {
		File fTmpDir = new File(MainClient.tempDir);
		if(fTmpDir.exists()==false) {
			fTmpDir.mkdir();
			fTmpDir.deleteOnExit();
		}
		launch(args);
	} // end main

	public static Image getLogoAsImage() throws IOException {
		InputStream is = MainClient.class.getResourceAsStream("/images/logos/"+logoName);
		Image img = new Image(is);
		is.close();
		return img;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.show();
		ParentGUIController main = new ParentGUIController();
		this.stage=primaryStage;
		primaryStage.setOnCloseRequest(confirmCloseEventHandler);
		primaryStage.setHeight(500);
		primaryStage.setWidth(500);
		/*for (int i = 0; i <= 5; i++)
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logos/img/logo3-" + i + ".png")));*/
		primaryStage.getIcons().add(getLogoAsImage());
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY(0);
        primaryStage.setMaxHeight(primScreenBounds.getMaxY());
        //primaryStage.resizableProperty().setValue(Boolean.FALSE);
        
		main.start(primaryStage);
	}
	
	private void deleteAllImages(){	
		File directory = new File(MainClient.imagesPath);
		if(directory.exists()) {
			for(File f: directory.listFiles()) {
				  if(f.isDirectory()==false)
					  if(f.delete()==false)
						  System.err.println("Can't delete "+ f.getName());
			}
		}
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
			deleteAllImages();
			if (Context.clientConsole != null)
				Context.clientConsole.quit();
		}
	};
}
