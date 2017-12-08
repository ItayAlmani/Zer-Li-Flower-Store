package common;


import java.io.*;
import java.util.ArrayList;

import controllers.ClientServerController;
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
	 * The connector between the GUI to the <code>ClientConsole</code>
	 */
	private ClientServerController csc;

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
		} catch (IOException e) {System.err.println("Can't quit Client!!\n");}
		System.exit(0);
	}
	
	/**
	 * Handles a message sent from the server to this client.
	 * @param msg - the message sent. Usually instance of CS Message
	 */
	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg instanceof CSMessage) {
			CSMessage csMsg = (CSMessage) msg;
			MessageType msgType = csMsg.getType();

			/*------------------SELECT queries from DB------------------*/
			if (msgType.equals(MessageType.SELECT)) {
				if (Context.CurrentGUI instanceof ProductsFormGUIController) {
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
			
			/*--------------------exception caught----------------------*/
			else if (msgType.equals(MessageType.Exception)) {
				csc.sendResultToClient(false);
			}
			
			else if(msgType.equals(MessageType.SetDB)) {
				if (csMsg.getObjs().get(0) instanceof Boolean)
					csc.sendResultToClient((boolean) csMsg.getObjs().get(0));
			}
		}
	}

	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 */
	private void handleGetProducts(ArrayList<Object> obj) {
		ArrayList<Product> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3)
			prds.add(this.csc.parsingTheData((int) obj.get(i), (String) obj.get(i + 1), (String) obj.get(i + 2)));
		this.csc.sendProductsToClient(prds);
	}

	/**
	 * Sending the data base details to the client,
	 * after checking that the whole ArrayList has only String object.
	 * @param objArr - the data base details: [0]-Url,[1]-Name,[2]-UserName,[3]-Password
	 */
	private void handleDBData(ArrayList<?> objArr) {
		for (Object object : objArr) {
			if(object instanceof String == false)
				csc.sendResultToClient(false);
		}
		this.csc.sendDBDataToClient((ArrayList<String>) objArr);
	}

	/**
	 * Will handle the message from the GUI in client side
	 * @param message	-	CSMessage object which includes the message to the server
	 * @param csc		-	the controller which sent the message (will be sent by *this*)
	 * @throws IOException
	 */
	public void handleMessageFromClientUI(CSMessage message, ClientServerController csc) throws IOException {
		this.csc = csc;
		sendToServer(message);
	}

}
// End of ConsoleChat class
