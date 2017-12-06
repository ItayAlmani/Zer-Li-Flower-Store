package Server;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import Controllers.Factory;
import Entity.CSMessage;
import Entity.MessageType;
import Entity.Product;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**The default port to listen on.*/
	final public static int DEFAULT_PORT = 5555;
	
	public static String dbUrl = "localhost",dbName="dbassignment2",
			dbUserName = "root", 
			dbPassword = "1234";
	
	public static DataBase db;
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 * @param port	-	The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		Factory.db=db;
		updateDB();
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
				client.sendToClient(setMessageToClient((CSMessage)msg));
		} catch (IOException e) {
			if(msg instanceof CSMessage) {
				CSMessage csMsg = (CSMessage)msg;
				csMsg.setObjs(new ArrayList<>());
				csMsg.setType(MessageType.Exception);
				client.sendToClient(csMsg);
			}
		}
	}
	
	private CSMessage setMessageToClient(CSMessage csMsg) {
		MessageType msgType = csMsg.getType();
		ArrayList<Object> objArr = csMsg.getObjs();
		
		if(msgType.equals(MessageType.SELECT) || msgType.equals(MessageType.UPDATE)) {
			if(csMsg.getObjs().size() == 1 && objArr.get(0) instanceof String) {
				String query = (String) objArr.get(0);
				
				/*-------SELECT queries from DB-------*/
				if(msgType.equals(MessageType.SELECT))
					csMsg.setObjs(db.getQuery(query));
				
				/*-------UPDATE queries from DB-------*/
				else {
					if(objArr!=null)	objArr.clear();
					else				objArr = new ArrayList<>();
					objArr.add(db.setQuery(query));
					csMsg.setObjs(objArr);
				}
			}
		}
		else if(msgType.equals(MessageType.DBData)) {
			if(objArr!=null)	objArr.clear();
			else				objArr = new ArrayList<>();
			objArr.add(dbUrl);
			objArr.add(dbName);
			objArr.add(dbUserName);
			objArr.add(dbPassword);
			csMsg.setObjs(objArr);
		}
		return csMsg;
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
	
	public static void updateDB() {
		db = new DataBase(dbUrl, dbName,dbUserName,dbPassword);
		Factory.setDataBase(db);
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 * @param args[0]	-	The port number to listen on. Defaults to 5555 if no argument is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
// End of EchoServer class
