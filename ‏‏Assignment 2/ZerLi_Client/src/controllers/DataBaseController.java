package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import common.ClientController;
import common.Context;
import entities.CSMessage;
import entities.DataBase;
import gui.controllers.ParentGUIController;
import entities.CSMessage.MessageType;

public class DataBaseController extends ParentController {
	
	/**
	 * Requests from the Server the <code>DataBase</code> information.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public void getDB() throws IOException {
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.DBData,null));
	}
	
	/**
	 * Requests from the Server the <code>DataBase</code> information.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public void getDBStatus() throws IOException {
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.DBStatus,null));
	}
	
	/**
	 * Requests from the Server to connect to a <code>DataBase</code> by the <code>db</code> information.
	 * @param db - the information of the new <code>DataBase</code>.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public void setDB(DataBase db) throws IOException {
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
	 * @param objArr - the data base details: [0]-connected or not (boolean),
	 * 										  [1]-Url,[2]-Name,[3]-UserName,[4]-Password
	 */
	public void handleGet(ArrayList<Object> objArr) {
		if(objArr.size()==1) {
			if(objArr.get(0) instanceof Boolean) {
				ClientController.dbConnected=(boolean)objArr.get(0);
				sendDBStatus((Boolean)objArr.get(0));
			}
		}
		else {
			ArrayList<String> strArr = new ArrayList<>();
			for (Object object : objArr) {
				if(object instanceof String == false)
					ParentController.sendResultToClient(false);
				else
					strArr.add((String)object);
			}
			sendDBData(strArr);
		}
	}
	
	/**
	 * Sending the <code>DataBase</code> data (which is in ArrayList) to the correct GUI.
	 * @param db - the information of the new <code>DataBase</code>.
	 * @throws IOException - thrown when sending to Server failed.
	 */
	public void sendDBData(ArrayList<String> dbData) {
		String methodName = "setDBData";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, dbData);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, dbData);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
	
	public void sendDBStatus(Boolean dbStatus) {
		String methodName = "setDBStatus";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,Boolean.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, dbStatus);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,Boolean.class);
				m.invoke(ParentGUIController.currentGUI, dbStatus);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
}
