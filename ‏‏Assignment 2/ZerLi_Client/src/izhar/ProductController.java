package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import gui.controllers.ParentGUIController;
import izhar.interfaces.IProduct;

public class ProductController extends ParentController implements IProduct {	
//------------------------------------------------IN CLIENT--------------------------------------------------------------------
	@Override
	public ArrayList<Product> assembleProduct(ProductType type, Float priceStart, Float priceEnd, Color color, ArrayList<Product> products) {
		ArrayList<Product> inConditionProds = new ArrayList<>();
		if(type!=null && priceStart!=null && priceEnd!=null) {
			if(color==null) {
				for (Product p : products) {
					if(p.getType().equals(type)
							&& p.getPrice()>=priceStart 
							&& p.getPrice()<=priceEnd) {
						inConditionProds.add(p);
					}
				}
			}
			else {
				for (Product p : products) {
					if(p.getType().equals(type)
							&& p.getPrice()>=priceStart 
							&& p.getPrice()<=priceEnd) {
						if(p.getColor().equals(color))
							inConditionProds.add(p);
					}
				}
			}
		}
		return inConditionProds;
	}
	
	
	
	
	
//------------------------------------------------IN SERVER--------------------------------------------------------------------
	@Override
	public void getProductByID(BigInteger prdID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product WHERE productID = '"+prdID+"';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	@Override
	public void update(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getPrdID()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
	public void handleGet(ArrayList<Product> prds) {
		String methodName = "setProducts";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, prds);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, prds);
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
	public void add(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(p);
		arr.add(true);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,Product.class));
	}
	
	@Override
	public void getAllProducts() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	@Override
	public void getProductsInCatalog() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	@Override
	public void getAllProductsNotInCatalog() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));		
	}
//-------------------------------------------------------------------------------------------------------------------------
}
