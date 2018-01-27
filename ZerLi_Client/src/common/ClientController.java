package common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import common.CSMessage.MessageType;

/**
 * Will parse/decode the <code>CSMessage</code> which present the answer of the
 * GUI request, and will send the answer to the correct GUI. Also, will be
 * controller of the System data: DataBase information, Server information etc.
 */
public class ClientController {
	/** The default port of the server */
	public static int DEFAULT_PORT = 5555;

	/** The default host of the server */
	public static String DEFAULT_HOST = "localhost";

	public static boolean dbConnected = false;

	/** The text file name which contains the server's details: host and port */
	private final static String serverTxtFileName = "ServerAddress.txt";
	private final static String projectPath = System.getProperty("user.dir") + "//";
	private final static String tempPath = projectPath + "temp//";
	private final static String txtLocalPath = tempPath + serverTxtFileName;

	/**
	 * Analyzes the <code>csMsg</code> and calling to the suitable function which
	 * parse and send the respond to the correct GUI
	 * @param csMsg the respond of the Server to the Client's request
	 */
	public static void parseMessage(CSMessage csMsg) {
		MessageType msgType = csMsg.getType();

		if (csMsg.getObjs() == null) {
			ParentController.sendResultToClient(false);
			return;
		}

		Class<?> c = null;
		Method m = null;

		if (csMsg.getClasz() != null) {
			c = getObjectClass(csMsg);
			if (c == null) {
				ParentController.sendResultToClient(false);
				return;
			}
		}
		if (msgType == null)
			return;
		/*------------------SELECT queries from DB------------------*/
		if (msgType.equals(MessageType.SELECT) && c != null) {
			try {
				m = c.getMethod("handleGet", ArrayList.class);
				m.invoke(c.newInstance(), csMsg.getObjs());
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		} else if (msgType.equals(MessageType.UPDATE)) {
			if (csMsg.getObjs().get(0) != null && csMsg.getObjs().get(0) instanceof Boolean)
				ParentController.sendResultToClient((Boolean) csMsg.getObjs().get(0));
		} else if (msgType.equals(MessageType.INSERT) && c != null) {
			if (csMsg.getObjs().get(0) != null) {
				if (csMsg.getObjs().get(0) instanceof BigInteger && csMsg.getClasz() != null) {
					try {
						m = c.getMethod("handleInsert", BigInteger.class);
						m.invoke(c.newInstance(), (BigInteger) csMsg.getObjs().get(0));
						ParentController.sendResultToClient(true);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException | InstantiationException e) {
						e.printStackTrace();
						ParentController.sendResultToClient(false);
					}
				} else if (csMsg.getObjs().get(0) instanceof Boolean)
					ParentController.sendResultToClient((boolean) csMsg.getObjs().get(0));
				else
					ParentController.sendResultToClient(false);
			} else
				ParentController.sendResultToClient(false);
		}

		/*------------------get DB data from Server------------------*/
		else if (msgType.equals(MessageType.DBData) || msgType.equals(MessageType.DBStatus)) {
			Context.fac.dataBase.handleGet(csMsg.getObjs());
		}
		/*--------------------exception caught----------------------*/
		else if (msgType.equals(MessageType.Exception)) {
			if (csMsg.getObjs() != null && csMsg.getObjs().isEmpty() == false
					&& csMsg.getObjs().get(0) instanceof String)
				Context.mainScene.setMessage((String) csMsg.getObjs().get(0));
			else
				ParentController.sendResultToClient(false);
		}

		else if (msgType.equals(MessageType.SetDB)) {
			if (csMsg.getObjs().get(0) instanceof Boolean)
				ParentController.sendResultToClient((boolean) csMsg.getObjs().get(0));
		}
	}

	/**
	 * Finds {@link Class} between all the {@code controllers packages: orderNproducts, customersSatisfaction etc.} 
	 *@param csMsg the request {@link Object}
	 *@return the {@link Class} object associated with the class or 
	 * interface with the given string name {@code classPath+className+"Controller"}.
	 */
	private static Class<?> getObjectClass(CSMessage csMsg){
		Class<?> c = null;
		if(csMsg.getClasz()!=null) {
			String className = csMsg.getClasz().getName();
			className=className.substring(className.lastIndexOf("."));
			if((c=findHandleGetFunc(className, "common"))==null)
				if((c=findHandleGetFunc(className, "orderNproducts"))==null)
					if((c=findHandleGetFunc(className, "customersSatisfaction"))==null)
						if((c=findHandleGetFunc(className, "quarterlyReports"))==null)
							c=findHandleGetFunc(className, "usersInfo");
		}
		return c;
	}
	
	private static Class<?> findHandleGetFunc(String className, String classPath) {
		try {
			return Class.forName(classPath + className + "Controller");
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Looking for the .txt file at <code>projectPath</code>+<code>serTxtPath</code>
	 * path, and will take the data from there. If not found or invalid data, the
	 * system will try to connect by <code>DEFAULT_HOST</code> and
	 * <code>DEFAULT_PORT</code>.
	 * 
	 * @throws IOException
	 *             - when the data in the .txt file is wrong, or the file couldn't
	 *             be found
	 */
	public static void connectToServer() throws IOException {
		int serSuccessFlag = 0; // will be 1 if updateDB(args) succeeded
		try {
			File fInput = new File(txtLocalPath);
			if (fInput.exists() == false) {
				try {
					Context.clientConsole = new ClientConsole(DEFAULT_HOST, DEFAULT_PORT);
				} catch (IOException e) {
					System.err.println("Context->Default Server data is wrong!\n");
					throw e;
				}
				return;
			}
			Scanner scnr = new Scanner(fInput);
			scnr.useDelimiter("\\w");
			String[] args = new String[2];
			for (int i = 0; i < 2 && scnr.hasNextLine(); i++) {
				String[] tempSplit = scnr.nextLine().split("\\W+");
				args[i] = tempSplit[tempSplit.length - 1];
			}
			scnr.close();
			Context.clientConsole = new ClientConsole(args[0], Integer.parseInt(args[1]));
			System.out.println("Client connected to server!\n");

			/*
			 * connection succeeded, default data will be changed in .txt file and in
			 * default attributes
			 */
			serSuccessFlag = 1;
			DEFAULT_HOST = args[0];
			DEFAULT_PORT = Integer.parseInt(args[1]);
			writeNewServerDataIntoTxt();
		} catch (IOException e) {
			System.err.println("Context->ServerAddress.txt data is corrupted or Can't find txt file at "
					+ serverTxtFileName + ".\n");
		}
		if (serSuccessFlag == 0) { // db data corrupted
			try {
				Context.clientConsole = new ClientConsole(DEFAULT_HOST, DEFAULT_PORT);
			} catch (IOException e) {
				System.err.println("Context->Default Server data is wrong!\n");
				throw e;
			}
		}
	}

	/**
	 * Trying to connect into server at (<code>host</code>,<code>port</code>). If
	 * succeeded will change the .txt file at
	 * <code>projectPath</code>+<code>serTxtPath</code> path. If not, the system
	 * will try to connect by <code>DEFAULT_HOST</code> and
	 * <code>DEFAULT_PORT</code>.
	 * 
	 * @param host
	 *            - new <code>DEFAULT_HOST</code> of the Server trying to connect to
	 * @param port
	 *            - new <code>DEFAULT_PORT</code> of the Server trying to connect to
	 * @throws IOException
	 *             - when the data in the .txt file is wrong, or the file couldn't
	 *             be found
	 */
	public static void connectToServer(String host, int port) throws IOException {
		int serSuccessFlag = 0; // will be 1 if updateDB(args) succeeded
		try {
			Context.clientConsole = new ClientConsole(host, port);
			DEFAULT_HOST = host;
			DEFAULT_PORT = port;
			writeNewServerDataIntoTxt();
			serSuccessFlag = 1;
		} catch (IOException e) {
			System.err
					.println("\nServerAddress.txt data is corrupted or Can't find txt file " + serverTxtFileName + ".");
			System.err.println("Go to Context for the process\n");
		}

		if (serSuccessFlag == 0) { // db data corrupted
			try {
				Context.clientConsole = new ClientConsole(DEFAULT_HOST, DEFAULT_PORT);
				throw new IOException();
			} catch (IOException e) {
				System.err.println("\nDefault Server data is wrong!\nGo to Context to fix it!\n");
				throw e;
			}
		}
	}

	/**
	 * Will flush all data in .txt file at
	 * <code>projectPath</code>+<code>serTxtPath</code>, and will write the new
	 * Server data: <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * 
	 * @throws IOException
	 *             - will be thrown when the .txt file not found
	 */
	private static void writeNewServerDataIntoTxt() throws IOException {
		File fTempDir = new File(tempPath);
		if (fTempDir.exists() == false)
			fTempDir.mkdir();
		File fInput = new File(txtLocalPath);
		if (fInput.exists() == false) // Create a new file if doesn't exists yet
			fInput.createNewFile();
		PrintStream output = new PrintStream(fInput);
		output.flush();// flush whole txt file
		output.println("Host: " + DEFAULT_HOST);
		output.println("Port: " + DEFAULT_PORT);
		output.close();
	}

}
