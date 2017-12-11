package controllers;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import common.MainClient;
import entities.*;
import gui.controllers.*;

/**
 * The connector between the GUI to the <code>ClientConsole</code>
 */
public class ClientServerController {
	private static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	public static void parseMessage(CSMessage csMsg) {
		MessageType msgType = csMsg.getType();

		/*------------------SELECT queries from DB------------------*/
		if (msgType.equals(MessageType.SELECT)) {
			if (Context.CurrentGUI instanceof ProductsFormGUIController) {
				if(csMsg.getClasz().equals(Product.class)) {
					ProductController.handleGetProducts(csMsg.getObjs());
				}
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
	
	public static void askDBDataFromServer() throws IOException {
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.DBData,null));
	}
	
	public static void askSetDBData(DataBase db) throws IOException {
		myMsgArr.add(db.getDbUrl());
		myMsgArr.add(db.getDbName());
		myMsgArr.add(db.getDbUserName());
		myMsgArr.add(db.getDbPassword());
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.SetDB,myMsgArr));
	}
	
	public static void sendDBDataToClient(ArrayList<String> dbData) {
		if(Context.CurrentGUI instanceof ConnectionConfigGUIController) {
			((ConnectionConfigGUIController)Context.CurrentGUI).setDBDataInGUI(dbData);
		}
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
}
