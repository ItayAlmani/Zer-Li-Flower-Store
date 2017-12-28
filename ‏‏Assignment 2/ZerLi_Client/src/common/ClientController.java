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
		if (msgType.equals(MessageType.SELECT) || msgType.equals(MessageType.GetAI)) {
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
				if(msgType.equals(MessageType.SELECT)) 
					m = c.getMethod("handleGet",ArrayList.class);
				else
					m = c.getMethod("setLastAutoIncrenment",ArrayList.class);
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
		else if (msgType.equals(MessageType.DBData) || msgType.equals(MessageType.DBStatus)) {
			Context.fac.dataBase.handleGet(csMsg.getObjs());
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
	
	private static Class findHandleGetFunc(String className, String classPath) {
		try {
			Class c =	Class.forName(classPath+className+"Controller");
			//System.err.println("Class found in "+classPath+className);
			return c;
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			//System.err.println("No class found in "+classPath+className);
		}
		return null;
	}
}
