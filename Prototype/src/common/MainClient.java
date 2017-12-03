package common;

import javafx.application.Application;
import javafx.stage.Stage;
import CS.ClientConsole;
import gui.*;

public class MainClient extends Application {
	private static ClientConsole cc;
	
	final public static int DEFAULT_PORT = 5555;

	public static void main(String args[]) throws Exception {
		String host = "";
		int port = 0; // The port number

		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		cc = new ClientConsole(host, DEFAULT_PORT);
		launch(args);
	} // end main

	@Override
	public void start(Stage arg0) throws Exception {

		MainMenuGUIController main = new MainMenuGUIController();
		main.start(arg0);
	}
}
