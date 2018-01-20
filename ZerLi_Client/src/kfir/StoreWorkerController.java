package kfir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Customer;
import entities.PaymentAccount;
import entities.CSMessage.MessageType;
import gui.controllers.ParentGUIController;
import entities.StoreWorker;

public class StoreWorkerController extends ParentController{
	
	
	public void getStoreWorkerByUser(BigInteger userID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(userID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,StoreWorker.class));
	}
	
	public void handleGet(ArrayList<StoreWorker> storeWorkers) {
		String methodName = "setStoreWorkers";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, storeWorkers);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, storeWorkers);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
	
	public void update(StoreWorker sw) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(sw);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,StoreWorker.class));
	}
	
	public void add(StoreWorker sw, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(sw);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr, StoreWorker.class));
	}

	public void delete(BigInteger userID) throws Exception {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(userID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr, StoreWorker.class));
	}
	
	public void handleInsert(BigInteger id) {
		String methodName = "setSWid";
		Method m = null;
		try {
			// a controller asked data, not GUI
			if (Context.askingCtrl != null && Context.askingCtrl.size() != 0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName, BigInteger.class);
				Object ob = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(ob, id);
			} else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName, BigInteger.class);
				m.invoke(ParentGUIController.currentGUI, id);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '" + methodName + "'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '" + methodName + "'");
			e2.printStackTrace();
		}
	}
}