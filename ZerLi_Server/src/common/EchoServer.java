package common;

import java.io.*;
import java.util.ArrayList;

import entities.CSMessage;
import entities.CSMessage.MessageType;
import ocsf.server.*;

/**
 * Port can be written in the command line as argument. Otherwise, the default port is 5555
 *
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**The default port to listen on.*/
	final public static int DEFAULT_PORT = 5555;
	
	final public static Factory fac = new Factory();
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 * @param port	-	The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		fac.dataBase.connectToDB();
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 * @param msg	-	The message received from the client.
	 * @param client	-	The connection from which the message originated.
	 * @throws IOException
	 */
	@Override
	public void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException {
		try {
			if(msg instanceof CSMessage)
				client.sendToClient(ServerController.setMessageToClient((CSMessage)msg));
		} catch (Exception e) {
			if(msg instanceof CSMessage) {
				CSMessage csMsg = (CSMessage)msg;
				ArrayList<Object> arr = csMsg.getObjs();
				arr.clear();
				if(e.getMessage()!=null)
					arr.add(e.getMessage());
				else if(e.getCause()!=null && e.getCause().getMessage()!=null)
					arr.add("Error - " + e.getCause().getMessage());
				csMsg.setType(MessageType.Exception);
				client.sendToClient(csMsg);
			}
		}
	}
	
	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
	
	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 * @param args[0]	-	The port number to listen on. Defaults to 5555 if no argument is entered.
	 */
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		if(args.length!=0) {
			try {
				port = Integer.parseInt(args[0]);
			}catch (NumberFormatException e) {
				System.err.println(args[0] + " is not a number - enter number for port!");
				System.exit(0);
			}
		}
		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
			fac.setComplaintController();
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			ex.printStackTrace();
		}
	}
}
// End of EchoServer class
