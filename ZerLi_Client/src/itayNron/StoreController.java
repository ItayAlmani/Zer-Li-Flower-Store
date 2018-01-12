package itayNron;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Store;
import entities.Store.StoreType;
import entities.StoreWorker;
import gui.controllers.ParentGUIController;
import itayNron.interfaces.IStore;

public class StoreController extends ParentController implements IStore {
	
	public void handleGet(ArrayList<Store> stores) {
		String methodName = "setStores";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, stores);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, stores);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
	
	@Override
	public void getAllStores() throws IOException   {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Store.class));
	}
	
	public void getAllStoresWithStock() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Store.class));
	}
	
	public ArrayList<Store> getOrdersOnlyStoresFromArrayList(ArrayList<Store> allStores){
		ArrayList<Store> ordersOnly = new ArrayList<>();
		for (Store store : allStores)
			if(store.getType().equals(StoreType.OrdersOnly))
				ordersOnly.add(store);
		return ordersOnly;
	}
	
	public ArrayList<Store> getPhysicalStoresFromArrayList(ArrayList<Store> allStores){
		ArrayList<Store> physicals = new ArrayList<>();
		for (Store store : allStores)
			if(store.getType().equals(StoreType.Physical))
				physicals.add(store);
		return physicals;
	}

	@Override
	public void update(Store store) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(store);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,Store.class));
	}

	@Override
	public Store parse(BigInteger storeID, String type, BigInteger managerID, String name)  {
		return new Store(storeID, name, StoreType.valueOf(type), new StoreWorker(managerID));
		
	}

	@Override
	public void getAllPhysicalStores() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Store.class));
		
	}

}