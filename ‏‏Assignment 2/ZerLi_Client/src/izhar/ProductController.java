package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
						BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
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
	public Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL) throws FileNotFoundException {
		return new Product(prdID, name, type,price,color,inCatalog,
				Context.projectPath+"\\src\\images\\"+imageURL);
	}
	
	@Override
	public void sendProducts(ArrayList<Product> prds) {
		String methodName = "setProducts";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), prds);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, prds);
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
		String query = "INSERT INTO orders (productName, productType, price, color, inCatalog)"
				+ "VALUES ('" + p.getName() + "', '" 
				+ p.getType().toString() + "', '"
				+ p.getPrice() + "', '"
				+ p.getColor().toString() + "', '"
				+ res + "');";
		query += "SELECT Max(productID) from product;";
		myMsgArr.add(query);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Product.class));
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
