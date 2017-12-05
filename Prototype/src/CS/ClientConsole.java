package CS;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Entity.Product;
import client.*;
import common.*;
import gui.MainMenuGUIController;
import gui.ProductViewGUIController;
import gui.ProductsFormGUIController;
import gui.TemplateGUI;
import javafx.application.Application;
import javafx.stage.Stage;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsol e
 *
 * @author Fran&ccedil;aois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole extends AbstractClient{
	// Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Instance variables **********************************************
	private ProductsFormGUIController gui;
	private ClientController cc;
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host
	 *            The host to connect to.
	 * @param port
	 *            The port to connect on.
	 */
	public ClientConsole(String host, int port) throws IOException {
		super(host, port); //Call the superclass constructor
	    openConnection();
	}

	// Instance methods ************************************************
	  /**
	   * This method terminates the client.
	   */
	  public void quit()
	  {
	    try
	    {
	      closeConnection();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	  }
	// Class methods ***************************************************

	@Override
	protected void handleMessageFromServer(Object msg) {
		if(msg instanceof ArrayList<?>) {
			ArrayList<Object> obj = (ArrayList<Object>)msg;
			ArrayList<Product> prds = new ArrayList<>();
			for(int i = 0; i<obj.size();i+=3)
				prds.add(this.cc.parsingTheData((int)obj.get(i),(String)obj.get(i+1),(String)obj.get(i+2)));
			this.cc.gui.updateCB(prds);
		}
	}
	
	public void handleMessageFromClientUI(String message, ClientController cc, ProductsFormGUIController gui) throws IOException {
	    this.cc=cc;
	    this.gui=gui;
		sendToServer(message);
	}

}
// End of ConsoleChat class
