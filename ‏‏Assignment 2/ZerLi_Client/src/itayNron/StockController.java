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
import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import gui.controllers.ParentGUIController;
import itayNron.interfaces.IStock;

public class StockController extends ParentController implements IStock {
	
	public Product checkStockByOrder(Order order, Store store) {
		if(store.getStock()==null || store.getStock().size()==0) {
			if(order.getProducts() != null && order.getProducts().size()>0)
				return order.getProducts().get(0).getProduct();
			return null;
		}
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
	
	public void update(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(order);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,Stock.class));
	}
	
	public void getStockByStore(BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(storeID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Stock.class));
		
	}
	
	public void handleGet(ArrayList<Stock> stocks) {
		String methodName = "setStocks";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, stocks);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, stocks);
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