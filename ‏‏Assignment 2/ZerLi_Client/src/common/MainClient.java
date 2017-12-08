package common;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

import gui.controllers.*;

public class MainClient extends Application {
	public static ClientConsole cc;
	
	final public static int DEFAULT_PORT = 5555;
	final public static String DEFAULT_HOST="localhost";

	public static void main(String args[]) throws IOException {
		launch(args);
	} // end main

	@Override
	public void start(Stage arg0) throws Exception {

		MainMenuGUIController main = new MainMenuGUIController();
		main.host=DEFAULT_HOST;
		main.port=DEFAULT_PORT;
		arg0.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override public void handle(WindowEvent t) {
		        if(cc!=null)
		        	cc.quit();
		    }
		});
		main.start(arg0);
	}
}
