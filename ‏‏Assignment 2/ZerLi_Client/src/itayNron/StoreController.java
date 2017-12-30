package itayNron;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import entities.Store.StoreType;
import entities.StoreWorker;
import entities.CSMessage.MessageType;
import itayNron.interfaces.IStore;

public class StoreController extends ParentController implements IStore {
	private Order order = null; 

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

	@Override
	public Product checkStockByOrder(Order order, Store store) {
		ArrayList<Stock> stock = (ArrayList<Stock>) store.getStock().clone();
		for (ProductInOrder ordProd : order.getProducts()) {
			Product prod = ordProd.getProduct();
			Stock stk = store.getProductFromStock(prod);
			if(	stk==null //not in stock
					|| stk.getQuantity()-ordProd.getQuantity()<0 //Not enough products in stock
					)	
				return prod;
		}
		return null;
	}
	
	public ArrayList<Store> getOrdersOnlyStoresFromArrayList(ArrayList<Store> allStores){
		ArrayList<Store> ordersOnly = new ArrayList<>();
		for (Store store : allStores)
			if(store.getType().equals(StoreType.OrdersOnly))
				ordersOnly.add(store);
		return ordersOnly;
	}

	@Override
	public void updateStock(Order order) throws IOException {
		this.order=order;
		Context.askingCtrl.add(this);
		Context.fac.prodInOrder.getPIOsByOrder(order.getOrderID());
		
	}
	
	public void setPIOs(ArrayList<ProductInOrder> prds) throws IOException  {
		myMsgArr.clear();
		for(ProductInOrder productInOrder : prds) 
		{
			myMsgArr.add(String.format(
					"update stock" + 
					"set quantity=quantity - '%d'" + 
					"where storeID=(select storeID from deliverydetails where orderID='%d');",prds.get(3).getQuantity(),order.getOrderID()));
			Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
			
		}
		
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
	public void getStockByStore(int storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT sto.quantity,product.*" + 
				" FROM stock AS sto" + 
				" JOIN product ON sto.orderID=product.orderID" + 
				" WHERE sto.storeID='"+storeID+"'"
				);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Store.class));
		
	}
	
	@Override
	public void sendStock(Stock stock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllPhysicalStores() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT store.*" + "FROM store" + "WHERE store.type='Physical'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Store.class));
		
	}

	@Override
	public Store parse(BigInteger storeID, String type, BigInteger managerID, String name)  {
		return new Store(storeID, name, StoreType.valueOf(type), new StoreWorker(managerID));
		
	}

}