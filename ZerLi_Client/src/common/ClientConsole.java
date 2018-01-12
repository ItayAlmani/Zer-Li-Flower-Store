package common;


import java.io.*;

import entities.CSMessage;

import ocsf.client.*;

/**
 *The handler of the sending and receiving data to/from Server
 *(LAB6).
 */
public class ClientConsole extends AbstractClient {
	
	/**
	 * By constructing new instance of the <code>ClientConsole</code>,
	 * the connection to the Server will be created.
	 * @param host	-	The host to connect to.
	 * @param port	-	The port to connect on.
	 */
	public ClientConsole(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	/** This method terminates the client. */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {System.err.println("Can't close Client connection!!\n");}
		System.exit(0);
	}
	
	/**
	 * Handles a message sent from the server to this client.
	 * @param msg - the message sent. <b>Should</b> be instance of <code>CSMessage</code>
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof CSMessage)
			ClientController.parseMessage((CSMessage) msg);
	}

	/**
	 * Will send the message from the GUI to the server
	 * @param message	-	CSMessage object which includes the message to the server
	 * @throws IOException
	 */
	public void handleMessageFromClientUI(CSMessage message) throws IOException {
		sendToServer(message);
	}

}
// End of ConsoleChat class
