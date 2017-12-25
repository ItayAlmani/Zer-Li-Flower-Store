package common;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import entities.Customer;
import enums.UserType;
import gui.controllers.*;

public class MainClient extends Application {	
	
	public static void main(String args[]) throws IOException {
		launch(args);
	} // end main

	@Override
	public void start(Stage arg0) throws Exception {
		MainMenuGUIController main = new MainMenuGUIController();
		arg0.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override public void handle(WindowEvent t) {
		        if(Context.clientConsole!=null)
		        	Context.clientConsole.quit();
		    }
		});		
		Context.stage=arg0;
		
		main.start(arg0);
	}
}
