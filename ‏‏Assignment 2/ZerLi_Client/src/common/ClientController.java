package common;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import gui.controllers.*;
import izhar.gui.controllers.PaymentGUIController;

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
		
		Class<?> c = null;
		Method m = null;
		if(csMsg.getClasz()!=null) {
			String className = csMsg.getClasz().getName();
			className=className.substring(className.lastIndexOf("."));
			if((c=findHandleGetFunc(className, "controllers"))==null) {
				if((c=findHandleGetFunc(className, "itayNron"))==null)
					if((c=findHandleGetFunc(className, "izhar"))==null)
						if((c=findHandleGetFunc(className, "lior"))==null)
							if((c=findHandleGetFunc(className, "kfir"))==null)
								return;
			}
		}
		/*------------------SELECT queries from DB------------------*/
		if (msgType.equals(MessageType.SELECT) && c!=null) {
			try {
				m = c.getMethod("handleGet",ArrayList.class);
				m.invoke(c.newInstance(), csMsg.getObjs());
			} catch (NoSuchMethodException | SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
		}
		else if (msgType.equals(MessageType.UPDATE)) {
			if (csMsg.getObjs().get(0)!= null &&
					csMsg.getObjs().get(0) instanceof Boolean &&
					csMsg.getClasz() !=null)
				ParentController.sendResultToClient((Boolean)csMsg.getObjs().get(0));
		}
		else if (msgType.equals(MessageType.INSERT) && c!=null) {
			if (csMsg.getObjs().get(0)!= null &&
					csMsg.getObjs().get(0) instanceof BigInteger &&
					csMsg.getClasz() !=null) {
				try {
					m = c.getMethod("handleInsert",BigInteger.class);
					m.invoke(c.newInstance(), (BigInteger)csMsg.getObjs().get(0));
				} catch (NoSuchMethodException | SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
					e.printStackTrace();
				}
					
				if(Context.currentGUI instanceof PaymentGUIController)
					((PaymentGUIController)Context.currentGUI).loadNextWindow();
				ParentController.sendResultToClient(true);
			}
			else
				ParentController.sendResultToClient(false);
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
	
	private static void sendNewID(BigInteger id, Class<?> clasz) {
		String methodName = "setUpdateRespond";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,Integer.class, Class.class);
				m.invoke(Context.askingCtrl.get(0), id, clasz);
				Context.askingCtrl.remove(0);
			}
			//Send success message
			ParentController.sendResultToClient(true);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
	
	private static Class<?> findHandleGetFunc(String className, String classPath) {
		try {
			//System.err.println("Class found in "+classPath+className);
			return Class.forName(classPath+className+"Controller");
		} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
			//System.err.println("No class found in "+classPath+className);
		}
		return null;
	}

	public static void getLastAutoIncrenment(Class<?> clasz) throws IOException {
		myMsgArr.clear();
		String tbName = null;
		if(clasz.equals(Order.class))
			tbName="orders";
		else
			tbName=clasz.getName().toLowerCase().substring(clasz.getName().lastIndexOf("."+1));
		myMsgArr.add("SHOW TABLE STATUS WHERE `Name` = '"+tbName+"'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.GetAI, myMsgArr, clasz));
	}

}
