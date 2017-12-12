package common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import gui.controllers.MainMenuGUIController;
import javafx.stage.Stage;

/** The class which holds the data of each client. 
 * Static class which will help to hold the common data that
 * more than one class will use
*/
public class Context {
	
	/** The default port of the server */
	public static int DEFAULT_PORT = 5555;
	/** The default host of the server */
	public static String DEFAULT_HOST="localhost1";
	
	/** The path of the project: "C:.../ZerLi_Client" */
	private final static String projectPath=System.getProperty("user.dir");
	/** The path of the ServerAddress.txt file - the file
	 * that contains the server's details: host and port */
	private final static String serTxtPath="\\src\\common\\ServerAddress.txt";
	
	/** Each client has <b>one</b> <code>ClientConsole</code> */
	public static ClientConsole clientConsole = null;
	/** The current GUI. Used to deliver the answer from the <code>EchoServer</code>. */
	public static Object currentGUI = null;
	/** The current JavaFX stage <=> the window of the GUI */
	public static Stage stage = null;
	
	/**
	 * Looking for the .txt file at <code>projectPath</code>+<code>serTxtPath</code> path,
	 * and will take the data from there.
	 * If not found or invalid data, the system will try to connect by
	 * <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * @throws IOException - when the data in the .txt file is wrong, or the file couldn't be found
	 */
	public static void connectToServer() throws IOException{
		int serSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			Scanner scnr = null;
			scnr = new Scanner(new File(projectPath+serTxtPath));
			scnr.useDelimiter("\\w");
			String[] args = new String[2];
			for(int i = 0;i<2 && scnr.hasNextLine();i++) {
				String[] tempSplit = scnr.nextLine().split("\\W+");
				args[i]= tempSplit[tempSplit.length-1];
			}
			scnr.close();
			clientConsole = new ClientConsole(args[0],Integer.parseInt(args[1]));
			
			/*connection succeeded, default data will be changed in .txt file and in default attributes*/
			serSuccessFlag = 1;
			DEFAULT_HOST = args[0];
			DEFAULT_PORT=Integer.parseInt(args[1]);
			writeNewServerDataIntoTxt();
		} catch (IOException e) {
			System.err.println("ServerAddress.txt data is corrupted or Can't find txt file at "+projectPath+serTxtPath+".");
			System.err.println("Go to Context for the process");
		}
		if(serSuccessFlag==0) {	//db data corrupted 
			try {
				clientConsole = new ClientConsole(DEFAULT_HOST,DEFAULT_PORT);
			} catch (IOException e) {
				System.err.println("Default Server data is wrong!\nGo to Context to fix it!");
				throw e;
			}
		}
	}

	/**
	 * Trying to connect into server at (<code>host</code>,<code>port</code>).
	 * If succeeded will change the .txt file at <code>projectPath</code>+<code>serTxtPath</code> path.
	 * If not, the system will try to connect by <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * @param host - new <code>DEFAULT_HOST</code> of the Server trying to connect to
	 * @param port - new <code>DEFAULT_PORT</code> of the Server trying to connect to
	 * @throws IOException - when the data in the .txt file is wrong, or the file couldn't be found
	 */
	public static void connectToServer(String host, int port) throws IOException{
		int serSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			clientConsole = new ClientConsole(host,port);
			DEFAULT_HOST = host;
			DEFAULT_PORT=port;
			writeNewServerDataIntoTxt();
			serSuccessFlag = 1;
		} catch (IOException e) {
			System.err.println("\nServerAddress.txt data is corrupted or Can't find txt file at "+projectPath+serTxtPath+".");
			System.err.println("Go to Context for the process\n");
		}
		
		if(serSuccessFlag==0) {	//db data corrupted 
			try {
				clientConsole = new ClientConsole(DEFAULT_HOST,DEFAULT_PORT);
				throw new IOException();
			} catch (IOException e) {
				System.err.println("\nDefault Server data is wrong!\nGo to Context to fix it!\n");
				throw e;
			}
		}
	}

	/**
	 * Will flush all data in .txt file at <code>projectPath</code>+<code>serTxtPath</code>,
	 * and will write the new Server data: <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * @throws IOException - will be thrown when the .txt file not found
	 */
	private static void writeNewServerDataIntoTxt() throws IOException {
		File f = new File(projectPath+serTxtPath);
		if (f.exists() == false) //Create a new file if doesn't exists yet
			f.createNewFile();
		PrintStream output = new PrintStream(projectPath+serTxtPath);
		output.flush();//flush whole txt file
		output.println("Host: "+DEFAULT_HOST);
		output.println("Port: "+DEFAULT_PORT);
		output.close();
	}
}
