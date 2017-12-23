package izhar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import izhar.interfaces.IProduct;

public class ProductController extends ParentController implements IProduct {	
	public void getProduct() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	public void updateProduct(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getPrdId()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
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

	public void createNewProduct(ProductType type, float priceStart, float priceEnd) {
		// TODO - implement ItemController.createNewItem
		throw new UnsupportedOperationException();
	}

	public void createNewProduct(ProductType type, float priceStart, float priceEnd, Color color) {
		// TODO - implement ItemController.createNewItem
		throw new UnsupportedOperationException();
	}

	public String[] assembleItemFromDB(ProductType type, float priceStart, float priceEnd, Color color) {
		// TODO - implement ItemController.assembleItemFromDB
		throw new UnsupportedOperationException();
	}
}
