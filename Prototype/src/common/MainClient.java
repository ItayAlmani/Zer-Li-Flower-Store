package common;

import javafx.application.Application;
import javafx.stage.Stage;
import CS.ClientConsole;
import gui.*;

public class MainClient extends Application {
	public static ClientConsole cc;
	
	final public static int DEFAULT_PORT = 5555;
	final public static String DEFAULT_HOST="localhost";

	public static void main(String args[]) throws Exception {
		cc = new ClientConsole(DEFAULT_HOST, DEFAULT_PORT);
		launch(args);
	} // end main

	@Override
	public void start(Stage arg0) throws Exception {

		MainMenuGUIController main = new MainMenuGUIController();
		main.start(arg0);
	}
}
