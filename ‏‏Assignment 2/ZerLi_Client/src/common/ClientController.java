package common;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import gui.controllers.*;

/** Will parse/decode the <code>CSMessage</code> which present the answer of the GUI request,
 * and will send the answer to the correct GUI.
 * Also, will be controller of the System data: DataBase information, Server information etc.
 */
public class ClientController {
	private static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	/**
	 * Analyzes the <code>csMsg</code> and calling to the suitable function which parse
	 * and send the respond to the correct GUI
	 * @param csMsg - the respond of the Server to the Client's request
	 */
	public static void parseMessage(CSMessage csMsg) {
		MessageType msgType = csMsg.getType();

		/*------------------SELECT queries from DB------------------*/
		if (msgType.equals(MessageType.SELECT)) {
			String className = csMsg.getClasz().getName();
			className=className.substring(className.lastIndexOf("."));
			Class c = null;
			Method m;
			if((c=findHandleGetFunc(className, "controllers"))==null) {
				if((c=findHandleGetFunc(className, "itayNron"))==null)
					if((c=findHandleGetFunc(className, "izhar"))==null)
						if((c=findHandleGetFunc(className, "lior"))==null)
							if((c=findHandleGetFunc(className, "kfir"))==null)
								return;
			}
			try {
				m = c.getMethod("handleGet",ArrayList.class);
				m.invoke(c.newInstance(), csMsg.getObjs());
			} catch (NoSuchMethodException | SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}

		/*------------------UPDATE queries from DB------------------*/
		else if (msgType.equals(MessageType.UPDATE)) {
			if (csMsg.getObjs().get(0) instanceof Boolean)
				ParentController.sendResultToClient((boolean) csMsg.getObjs().get(0));
		}

		/*------------------get DB data from Server------------------*/
		else if (msgType.equals(MessageType.DBData)) {
			handleDBData(csMsg.getObjs());
		}
		
		/*--------------------exception caught----------------------*/
		else if (msgType.equals(MessageType.Exception)) {
			ParentController.sendResultToClient(false);
		}
		
		else if(msgType.equals(MessageType.SetDB)) {
			if (csMsg.getObjs().get(0) instanceof Boolean)
				ParentController.sendResultToClient((boolean) csMsg.getObjs().get(0));
		}
	}
	
	/**
	 * Requests from the Server the <code>DataBase</code> information.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public static void askDBDataFromServer() throws IOException {
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.DBData,null));
	}
	
	/**
	 * Requests from the Server to connect to a <code>DataBase</code> by the <code>db</code> information.
	 * @param db - the information of the new <code>DataBase</code>.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public static void askSetDBData(DataBase db) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(db.getDbUrl());
		myMsgArr.add(db.getDbName());
		myMsgArr.add(db.getDbUserName());
		myMsgArr.add(db.getDbPassword());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SetDB,myMsgArr));
	}
	
	/**
	 * Sending the data base details to the client,
	 * after checking that the whole ArrayList has only String object.
	 * @param objArr - the data base details: [0]-Url,[1]-Name,[2]-UserName,[3]-Password
	 */
	private static void handleDBData(ArrayList<?> objArr) {
		for (Object object : objArr) {
			if(object instanceof String == false)
				ParentController.sendResultToClient(false);
		}
		sendDBDataToClient((ArrayList<String>) objArr);
	}
	
	/**
	 * Sending the <code>DataBase</code> data (which is in ArrayList) to the correct GUI.
	 * @param db - the information of the new <code>DataBase</code>.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public static void sendDBDataToClient(ArrayList<String> dbData) {
		if(Context.currentGUI instanceof ConnectionConfigGUIController) {
			((ConnectionConfigGUIController)Context.currentGUI).setDBDataInGUI(dbData);
		}
	}
	private static Class findHandleGetFunc(String className, String classPath) {
		try {
			Class c =	Class.forName(classPath+className+"Controller");
			System.err.println("Class found in "+classPath+className);
			return c;
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			System.err.println("No class found in "+classPath+className);
		}
		return null;
	}
}
