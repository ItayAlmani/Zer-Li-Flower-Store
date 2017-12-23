package izhar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.CSMessage.MessageType;
import entities.Product;
import entities.ShoppingCart;
import entities.Transaction;
import entities.Product.Color;
import entities.Product.ProductType;
import enums.DeliveryType;
import enums.OrderStatus;
import enums.OrderType;
import enums.UserType;
import izhar.interfaces.IProduct;

public class ProductController extends ParentController implements IProduct {	
	@Override
	public void getProduct() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	@Override
	public void updateProduct(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getPrdId()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Product> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 6)
			prds.add(parse(
					(int) obj.get(i), 
					(String) obj.get(i + 1), 
					(String) obj.get(i + 2),
					(float) obj.get(i + 3),
					(String) obj.get(i + 4),
					((int)obj.get(i + 5))!= 0));
		sendProducts(prds);
	}
	
	@Override
	public Product parse(int prdID, String name, String type, float price, String color, boolean inCatalog) {
		return new Product(prdID, name, type);
	}
	
	@Override
	public void sendProducts(ArrayList<Product> prds) {
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

	@Override
	public void assembleItemFromDB(ProductType type, float priceStart, float priceEnd, Color color) {
		
	}
}
