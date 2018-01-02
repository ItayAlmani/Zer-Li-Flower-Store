package itayNron;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import entities.CSMessage.MessageType;
import itayNron.interfaces.IStock;

public class StockController extends ParentController implements IStock {
	
	private Order order = null;
	
	public void getStockByStore(BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT sto.stockID,product.*,sto.quantity,sto.storeID" + 
				" FROM stock AS sto" + 
				" JOIN product ON sto.productID=product.productID" + 
				" WHERE sto.storeID='"+storeID+"'"
				);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Stock.class));
		
	}
	
	public void sendStocks(ArrayList<Stock> stocks) {
		String methodName = "setStocks";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), stocks);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, stocks);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
		
	}
	
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
	
	public void updateStock(Order order) throws IOException {
		this.order=order;
		Context.askingCtrl.add(this);
		Context.fac.prodInOrder.getPIOsByOrder(order.getOrderID());
	}
	
	public void setPIOs(ArrayList<ProductInOrder> prds) throws IOException  {
		for(ProductInOrder productInOrder : prds) {
			myMsgArr.clear();
			myMsgArr.add(String.format(
					" UPDATE stock" + 
					" SET quantity=quantity - '%d'" + 
					" WHERE storeID="
					+ "("
						+ " SELECT storeID"
						+ " FROM deliverydetails "
						+ " WHERE orderID='%d' AND productID='%d'"
					+ ");",
					productInOrder.getQuantity(),
					order.getOrderID(),
					productInOrder.getProduct().getPrdID()));
			Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
		}
		
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Stock> stocks = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 10) {
			try {
				stocks.add(parse(
						BigInteger.valueOf(Long.valueOf((int)obj.get(i))),
						BigInteger.valueOf(Long.valueOf((int)obj.get(i + 1))),
						(String) obj.get(i + 2), 
						(String) obj.get(i + 3),
						(float) obj.get(i + 4),
						(String) obj.get(i + 5),
						((int)obj.get(i + 6))!= 0,
						(String)obj.get(i+7),
						(int)obj.get(i + 8),
						BigInteger.valueOf(Long.valueOf((int)obj.get(i + 9)))
						));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		sendStocks(stocks);
		
	}
	
	public Stock parse(BigInteger stckID, 
			BigInteger prdID, String name, String type, float price, 
			String color, boolean inCatalog, String imageURL,
			int quantity, BigInteger storeID) throws FileNotFoundException {
		return new Stock(stckID, 
				Context.fac.product.parse(prdID, name, type, price, color, inCatalog, imageURL),
				quantity, storeID);
	}
	
}
