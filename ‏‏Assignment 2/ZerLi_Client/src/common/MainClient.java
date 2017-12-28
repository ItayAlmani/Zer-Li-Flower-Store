package common;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import kfir.gui.controllers.LogInGUIController;

import java.io.IOException;
import java.util.ArrayList;

import gui.controllers.*;

public class MainClient extends Application {	 
	private static String sargs[];
	public static void main(String args[]) throws IOException {
		sargs=args;
		
		/*try {
			Context.askingCtrl.add(MainClient.class.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			Context.connectToServer();
		} catch (IOException e) {
			
		}
		
		Context.fac.dataBase.getDBStatus();
		launch(sargs);
		
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
		main.start(arg0);
	}
}
