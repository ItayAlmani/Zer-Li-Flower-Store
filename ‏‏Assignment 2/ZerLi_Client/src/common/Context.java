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

public class Context {
	public static int DEFAULT_PORT = 5555;
	public static String DEFAULT_HOST="localhost";
	
	private final static String projectPath="C:\\Users\\izhar\\eclipse-github\\Zer-Li-Flower-Store\\þþAssignment 2\\ZerLi_Client\\src\\";
	private final static String serTxtPath="common\\ServerAddress.txt";
	
	public static ClientConsole cc = null;
	public static Object CurrentGUI = null;
	public static Stage stage = null;
	
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
			cc = new ClientConsole(args[0],Integer.parseInt(args[1]));
			DEFAULT_HOST = args[0];
			DEFAULT_PORT=Integer.parseInt(args[1]);
			writeNewServerDataIntoTxt();
			serSuccessFlag = 1;
		} catch (IOException e) {
			System.err.println("\nServerAddress.txt data is corrupted or Can't find txt file at "+projectPath+serTxtPath+".");
			System.err.println("Go to Context for the process\n");
		}
		
		if(serSuccessFlag==0) {	//db data corrupted 
			try {
				cc = new ClientConsole(DEFAULT_HOST,DEFAULT_PORT);
			} catch (IOException e) {
				System.err.println("\nDefault Server data is wrong!\nGo to Context to fix it!\n");
				throw e;
			}
		}
	}
	
	public static void connectToServer(String host, int port) throws IOException{
		int serSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			cc = new ClientConsole(host,port);
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
				cc = new ClientConsole(DEFAULT_HOST,DEFAULT_PORT);
				throw new IOException();
			} catch (IOException e) {
				System.err.println("\nDefault Server data is wrong!\nGo to Context to fix it!\n");
				throw e;
			}
		}
	}
	
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

	/*public static void addGUI(Class<? extends Object> clss, Stage s) {
		AllGUIs.put(clss, s);
	}
	
	public static Stage getStageByGUIController(Class<? extends Object> clss) {
		for (Entry<Class<? extends Object>, Stage> entry : AllGUIs.entrySet())
			if(entry.getKey()==clss)
				return entry.getValue();
		return null;
	}
	
	public static void quitAllGUIs() {
		for (Entry<Class<? extends Object>, Stage> entry : AllGUIs.entrySet()) {
			entry.getValue().close();
		}
	}*/
}
