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
import itayNron.interfaces.IStore;

public class StoreController extends ParentController implements IStore {
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Store> stores = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 4)
				stores.add(parse
						(
						BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
						(String) obj.get(i + 2), 
						BigInteger.valueOf((int)obj.get(i+1)),
						(String) obj.get(i + 3)));
			 
		sendStores(stores);
	}
	

	@Override
	public void getAllStores() throws IOException   {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM store;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Store.class));
	}
	
	@Override
	public void sendStores(ArrayList<Store> stores) 
	{
		String methodName = "setStores";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), stores);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, stores);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
		
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
	public void updateStore(Store store) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE store " + 
				" SET storeID=%d, managerID=%d,type=%s,name=%s" + 
				" WHERE storeID=%d",
				store.getStoreID(),store.getManager().getUserID(),store.getType().toString(),store.getName()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}

	@Override
	public Store parse(BigInteger storeID, String type, BigInteger managerID, String name)  {
		return new Store(storeID, name, StoreType.valueOf(type), new StoreWorker(managerID));
		
	}


	@Override
	public void getAllPhysicalStores() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT store.*" + "FROM store" + "WHERE store.type='Physical'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Store.class));
		
	}

}