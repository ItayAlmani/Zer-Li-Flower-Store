package client;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Entity.CSMessage;
import Entity.MessageType;
import Entity.Product;
import client.*;
import gui.MainMenuGUIController;
import gui.ProductViewGUIController;
import gui.ProductsFormGUIController;
import gui.TemplateGUI;
import javafx.application.Application;
import javafx.stage.Stage;

import ocsf.client.*;
import java.io.*;
import java.util.ArrayList;

public class ClientConsole extends AbstractClient {
	// ********** Class variables *************************************************

	/** The default port to connect on. */
	final public static int DEFAULT_PORT = 5555;

	// ********** Instance variables **********************************************
	private ClientServerController csc;

	// ********** Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 * 
	 * @param host
	 *            - The host to connect to.
	 * @param port
	 *            - The port to connect on.
	 */
	public ClientConsole(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		openConnection();
	}

	// ********** Instance methods ************************************************
	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
	// ********** Class methods ***************************************************

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof CSMessage) {
			CSMessage csMsg = (CSMessage) msg;
			MessageType msgType = csMsg.getType();

			/*------------------SELECT queries from DB------------------*/
			if (msgType.equals(MessageType.SELECT)) {
				if (csc.getGui() instanceof ProductsFormGUIController) {
					handleGetProducts(csMsg.getObjs());
				}
			}

			/*------------------UPDATE queries from DB------------------*/
			else if (msgType.equals(MessageType.UPDATE)) {
				if (csMsg.getObjs().get(0) instanceof Boolean)
					csc.sendResultToClient((boolean) csMsg.getObjs().get(0));
			}

			/*------------------get DB data from Server------------------*/
			else if (msgType.equals(MessageType.DBData)) {
				handleDBData(csMsg.getObjs());
			}
			
			/*------------------exception caught------------------*/
			else if (msgType.equals(MessageType.Exception)) {
				csc.sendResultToClient(false);
			}
		}
	}

	private void handleGetProducts(ArrayList<Object> obj) {
		ArrayList<Product> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3)
			prds.add(this.csc.parsingTheData((int) obj.get(i), (String) obj.get(i + 1), (String) obj.get(i + 2)));
		this.csc.sendProductsToClient(prds);
	}

	private void handleDBData(ArrayList<?> objArr) {
		if (objArr.get(0) instanceof String)
			this.csc.sendDBDataToClient((ArrayList<String>) objArr);
	}

	/**
	 * Will handle the message from the GUI in client side
	 * 
	 * @param message
	 *            - the query to send to the server: SELECT,UPDATE,INSERT...
	 * @param cc
	 *            - the controller which sended the messege (will be sent by *this*)
	 * @param gui
	 *            - the GUI to send response to
	 * @throws IOException
	 */
	public void handleMessageFromClientUI(CSMessage message, ClientServerController csc) throws IOException {
		this.csc = csc;
		sendToServer(message);
	}

}
// End of ConsoleChat class
