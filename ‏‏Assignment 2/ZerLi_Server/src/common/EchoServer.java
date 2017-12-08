package common;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import controllers.Factory;
import entities.CSMessage;
import entities.MessageType;
import ocsf.server.*;

public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**The default port to listen on.*/
	final public static int DEFAULT_PORT = 5555;
	
	private String dbUrl_default = "localhost",
			dbName_default="dbassignment2",
			dbUserName_default = "root", 
			dbPassword_default = "1234";
	
	private final String projectPath="C:\\Users\\izhar\\eclipse-github\\Zer-Li-Flower-Store\\þþAssignment 2\\ZerLi_Server\\src\\" ,
			dbTxtPath="common\\DataBaseAddress.txt";
	
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 * @param port	-	The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		connectToDB();
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
			if(msg instanceof CSMessage) {
				client.sendToClient(setMessageToClient((CSMessage)msg));
			}
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
					csMsg.setObjs(Factory.db.getQuery(query));
				
				/*-------UPDATE queries from DB-------*/
				else {
					if(objArr!=null)	objArr.clear();
					else				objArr = new ArrayList<>();
					objArr.add(Factory.db.setQuery(query));
					csMsg.setObjs(objArr);
				}
			}
		}
		else if(msgType.equals(MessageType.DBData)) {
			if(objArr!=null)	objArr.clear();
			else				objArr = new ArrayList<>();
			objArr.add(dbUrl_default);
			objArr.add(dbName_default);
			objArr.add(dbUserName_default);
			objArr.add(dbPassword_default);
			csMsg.setObjs(objArr);
		}
		else if(msgType.equals(MessageType.SetDB)) {
			String[] dbData = new String[4];
			int i = 0;
			for (Object object : objArr) {
				if(object instanceof String == false) {
					System.err.println("One of dbData isn't String!\n");
					objArr.clear();
					objArr.add(false);
					return csMsg;
				}
				else
					dbData[i++]=(String)object;
			}
			try {
				updateDB(dbData);
			} catch (SQLException e) {
				System.err.println("Incorrect data of message from client!\n");
				objArr.clear();
				objArr.add(false);
				return csMsg;
			}
			objArr.clear();
			objArr.add(true);
			csMsg.setObjs(objArr);
			dbUrl_default=dbData[0];
			dbName_default=dbData[1];
			dbUserName_default=dbData[2];
			dbPassword_default=dbData[3];
			try {
				writeNewDBDataIntoTxt();
			} catch (IOException e) {
				System.err.println("Can't write to txt file new data\n");
			}
		}
		return csMsg;
	}
	
	private void writeNewDBDataIntoTxt() throws IOException {
		File f = new File(projectPath+dbTxtPath);
		if (f.exists() == false) //Create a new file if doesn't exists yet
			f.createNewFile();
		PrintStream output = new PrintStream(projectPath+dbTxtPath);
		output.flush();//flush whole txt file
		output.println("Url: "+dbUrl_default);
		output.println("Name: "+dbName_default);
		output.println("UserName: "+dbUserName_default);
		output.println("Password: "+dbPassword_default);
		output.close();
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
	
	private void updateDB(String url, String name, String userName, String password) throws SQLException {
		Factory.setDataBase(new DataBase(url, name,userName,password));
	}
	
	private void updateDB(String[] args) throws SQLException {
		String url = args[0], name=args[1],userName=args[2],password=args[3];
		Factory.setDataBase(new DataBase(url, name,userName,password));
	}

	private void connectToDB() {
		int dbSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			Scanner scnr = null;
			scnr = new Scanner(new File(projectPath+dbTxtPath));
			scnr.useDelimiter("\\w");
			String[] args = new String[4];
			for(int i = 0;i<4 && scnr.hasNextLine();i++) {
				String[] tempSplit = scnr.nextLine().split("url|\\W+");
				args[i]= tempSplit[tempSplit.length-1];
			}
			scnr.close();
			updateDB(args);
			dbSuccessFlag = 1;
		} catch (SQLException e) {
			System.err.println("\nDataBaseAddress.txt data is corrupted, or the process is.\nGo to EchoServer for the process\n");
		} catch (FileNotFoundException e) {
			Factory.db=null;
			System.err.println("Can't find txt file at "+dbTxtPath+"\n");
		}
		
		if(dbSuccessFlag==0) {	//db data corrupted 
			try {
				updateDB(dbUrl_default, dbName_default,dbUserName_default,dbPassword_default);
			} catch (SQLException e) {
				Factory.db=null;
				System.err.println("\nDefault Data Base data is wrong!\nGo to EchoServer to fix it!\n");
			}
		}
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
