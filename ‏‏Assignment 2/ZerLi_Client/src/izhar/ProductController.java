package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import entities.Product.Color;
import entities.Product.ProductType;
import izhar.interfaces.IProduct;

public class ProductController extends ParentController implements IProduct {	
	@Override
	public void getProductByID(int prdID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product WHERE productID = '"+prdID+"';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	@Override
	public void updateProduct(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getPrdID()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Product> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 7)
			try {
				prds.add(parse(
						(int) obj.get(i), 
						(String) obj.get(i + 1), 
						(String) obj.get(i + 2),
						(float) obj.get(i + 3),
						(String) obj.get(i + 4),
						((int)obj.get(i + 5))!= 0,
						(String)obj.get(i+6))
						);
			} catch (FileNotFoundException e) {
				System.err.println("Couldn't find Image named "+ (String)obj.get(i+6) +".");
				e.printStackTrace();
			}
		sendProducts(prds);
	}
	
	@Override
	public Product parse(int prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL) throws FileNotFoundException {
		return new Product(prdID, name, type,price,color,inCatalog,
				Context.projectPath+"\\src\\images\\"+imageURL);
	}
	
	@Override
	public void sendProducts(ArrayList<Product> prds) {
		String productsToGUI_MethodName = "productsToGUI";
		Method m = null;
		try {
			m = Context.currentGUI.getClass().getMethod(productsToGUI_MethodName,ArrayList.class);
			m.invoke(Context.currentGUI, prds);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+productsToGUI_MethodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+productsToGUI_MethodName+"'");
			e2.printStackTrace();
		}
	}	

	@Override
	public void assembleItemFromDB(ProductType type, float priceStart, float priceEnd, Color color) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT *" + 
				"FROM product" + 
				"WHERE inCatalog=0 AND"
				+ " productType='"+type.toString()+"' AND"
				+ " price>="+priceStart+" AND"
				+ " price<="+priceEnd+" AND"
				+ " color='"+color.toString()+"'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr));
	}

	@Override
	public void addProduct(Product p) throws IOException {
		myMsgArr.clear();
		String res = "0";
		if(p.isInCatalog())
			res="1";
		myMsgArr.add(
				"INSERT INTO orders (productName, productType, price, color, inCatalog)"
						+ "VALUES ('" + p.getName() + "', '" 
						+ p.getType().toString() + "', '"
						+ p.getPrice() + "', '"
						+ p.getColor().toString() + "', '"
						+ res + "');"
								);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public void getAllProducts() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}

	@Override
	public void getProductsInCatalog() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product WHERE inCatalog='1';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
}
