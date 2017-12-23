package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import common.Context;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Product;

public class ProductController extends ParentController {	
	public void askProductsFromServer() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	public void askUpdateProductFromServer(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getPrdId()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 */
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Product> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 6)
			prds.add(parsingToProduct(
					(int) obj.get(i), 
					(String) obj.get(i + 1), 
					(String) obj.get(i + 2),
					(float) obj.get(i + 3),
					(String) obj.get(i + 4),
					((int)obj.get(i + 5))!= 0));
		sendProductsToClient(prds);
	}
	
	private Product parsingToProduct(int prdID, String name, String type, float price, String color, boolean inCatalog) {
		return new Product(prdID, name, type);
	}
	
	private void sendProductsToClient(ArrayList<Product> prds) {
		Method m = null;
		try {
			m = Context.currentGUI.getClass().getMethod("productsToComboBox",ArrayList.class);
			m.invoke(Context.currentGUI, prds);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method 'productsToComboBox'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called 'productsToComboBox'");
			e2.printStackTrace();
		}
	}	
}
