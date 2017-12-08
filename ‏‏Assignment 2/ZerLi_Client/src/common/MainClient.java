package common;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

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
		        if(Context.cc!=null)
		        	Context.cc.quit();
		    }
		});
		main.start(arg0);
	}
}
