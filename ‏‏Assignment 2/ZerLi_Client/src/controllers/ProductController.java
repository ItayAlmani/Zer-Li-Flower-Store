package controllers;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import entities.CSMessage;
import entities.MessageType;
import entities.Product;
import entities.ProductType;
import gui.controllers.ProductsFormGUIController;

public class ProductController extends ParentController {
	private static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	public static void askProductsFromServer() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
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
		if(Context.CurrentGUI instanceof ProductsFormGUIController)
			((ProductsFormGUIController)Context.CurrentGUI).updateCB(prds);
	}
	
	public static void askUpdateProductFromServer(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productID = '%d',productName='%s',productType='%s'"
				+ "WHERE productID=%d;",p.getId(),p.getName(),p.getType(),p.getId()));
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
}
