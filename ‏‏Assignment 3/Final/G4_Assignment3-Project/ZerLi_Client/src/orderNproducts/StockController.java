package orderNproducts;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.CSMessage;
import common.Context;
import common.ParentController;
import common.CSMessage.MessageType;
import common.gui.controllers.ParentGUIController;
import orderNproducts.entities.Order;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Store;
import orderNproducts.entities.Product.ProductType;
import orderNproducts.interfaces.IStock;

public class StockController extends ParentController implements IStock {
	
	public ArrayList<Stock> getInCatalogOnlyStock(ArrayList<Stock> stocks){
		ArrayList<Stock> newStocks = new ArrayList<>();
		for (Stock stk : stocks) {
			if(stk.getProduct().isInCatalog() && stk.getQuantity()>0)
				newStocks.add(stk);
		}
		return newStocks;
	}
	
	public ArrayList<Stock> getNotInCatalogOnlyStock(ArrayList<Stock> stocks){
		ArrayList<Stock> newStocks = new ArrayList<>();
		for (Stock stk : stocks) {
			if(stk.getProduct().isInCatalog()==false)
				newStocks.add(stk);
		}
		return newStocks;
	}
	
	public ArrayList<Stock> getStocksByProductType(ArrayList<Stock> stocks, ProductType pt){
		ArrayList<Stock> newStocks = new ArrayList<>();
		for (Stock stk : stocks) {
			if(stk.getProduct().getType().equals(pt))
				newStocks.add(stk);
		}
		return newStocks;
	}
	
	public Product checkStockByOrder(Order order, Store store) 
	{
		if(store.getStock()==null || store.getStock().size()==0) 
		{
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
	
	public void update(Stock stock) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(stock);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,Stock.class));
	}
	
	public void getStockByStore(BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(storeID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Stock.class));
		
	}
	
	private void saveProductsImages(ArrayList<Stock> stocks) {
		ArrayList<Product> prds = new ArrayList<>();
		for (Stock stk : stocks)
			prds.add(stk.getProduct());
	}
	
	public void handleGet(ArrayList<Stock> stocks) {
		saveProductsImages(stocks);
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
	
	public Stock getStockByProductFromStore(Store s, Product p) {
		if(s!=null && s.getStock()!=null) {
			for (Stock stc : s.getStock()) {
				if(stc.getProduct() != null && stc.getProduct().getPrdID().equals(p.getPrdID()))
					return stc;
			}
		}
		return null;
	}
	
	public Stock getStockByProductByStocks(ArrayList<Stock> stocks, Product p) {
		for (Stock stc : stocks) {
			if(stc.getProduct() != null && stc.getProduct().getPrdID().equals(p.getPrdID()))
				return stc;
		}
		return null;
	}
	
	/**
	 * The function will ask from the server to add {@code p} to all the stores
	 * with {@code quantity = 0}
	 * @param p the product to add to all the stores
	 * @throws IOException 
	 */
	public void addProductToAllStores(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(null,myMsgArr,Stock.class));
	}
}