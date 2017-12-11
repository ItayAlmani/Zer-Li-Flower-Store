package common;


import java.io.*;
import java.util.ArrayList;

import controllers.ClientController;
import controllers.ParentController;
import entities.CSMessage;
import entities.MessageType;
import entities.Product;
import gui.controllers.ProductsFormGUIController;

import ocsf.client.*;

/**
 *The handler of the sending and receiving data to/from Server
 *(LAB6).
 */
public class ClientConsole extends AbstractClient {
	/**
	 * Constructs an instance of the <code>ClientConsole</code>.
	 * @param host	-	The host to connect to.
	 * @param port	-	The port to connect on.
	 */
	public ClientConsole(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {System.err.println("Can't close Client connection!!\n");}
		System.exit(0);
	}
	
	/**
	 * Handles a message sent from the server to this client.
	 * @param msg - the message sent. Usually instance of CS Message
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof CSMessage)
			ClientController.parseMessage((CSMessage) msg);
	}

	/**
	 * Will handle the message from the GUI in client side
	 * @param message	-	CSMessage object which includes the message to the server
	 * @param csc		-	the controller which sent the message (will be sent by *this*)
	 * @throws IOException
	 */
	public void handleMessageFromClientUI(CSMessage message) throws IOException {
		sendToServer(message);
	}

}
// End of ConsoleChat class
