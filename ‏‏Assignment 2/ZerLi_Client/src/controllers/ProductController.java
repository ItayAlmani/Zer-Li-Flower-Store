package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import common.Context;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Product;
import entities.Product.ProductType;
import gui.controllers.ProductsFormGUIController;

public class ProductController extends ParentController {	
	public static void askProductsFromServer() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	public static void askUpdateProductFromServer(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getId()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 */
	public static void handleGetProducts(ArrayList<Object> obj) {
		ArrayList<Product> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3)
			prds.add(parsingToProduct((int) obj.get(i), (String) obj.get(i + 1), (String) obj.get(i + 2)));
		sendProductsToClient(prds);
	}
	
	private static Product parsingToProduct(int id, String name, String type) {
		Product p = new Product(id, name);
		switch (type.toLowerCase()) {
		case "bouqute":
			p.setType(ProductType.Bouqute);
			break;
		default:
			p.setType(ProductType.Empty);
			break;
		}
		return p;
	}
	
	private static void sendProductsToClient(ArrayList<Product> prds) {
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
