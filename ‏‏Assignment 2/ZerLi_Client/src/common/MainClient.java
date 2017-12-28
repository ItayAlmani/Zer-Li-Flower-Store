package common;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kfir.gui.controllers.LogInGUIController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import gui.controllers.*;

public class MainClient extends Application {	 
	public static void main(String args[]) throws IOException {
		try {
			Context.connectToServer();
		} catch (IOException e) {
			
		}
		
		Context.fac.dataBase.getDBStatus();
		launch(args);
		
	} // end main

	@Override
	public void start(Stage arg0) throws Exception {
		LogInGUIController main = new LogInGUIController();
		arg0.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override public void handle(WindowEvent t) {
		        if(Context.clientConsole!=null)
		        	Context.clientConsole.quit();
		    }
		});		
		Context.stage=arg0;
		InputStream x =getClass().getResourceAsStream("/images/logos/logo1.png");
		for (int i = 0; i <= 5; i++)
			arg0.getIcons().add(new Image(getClass().getResourceAsStream("/images/logos/img/logo3-"+i+".png")));
		main.start(arg0);
	}
}
