package izhar.gui.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Product;
import entities.CSMessage.MessageType;
import izhar.interfaces.ICatalog;

public class CatalogController extends ParentController implements ICatalog {

	@Override
	public void getProductsInCatalog() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product WHERE inCatalog='1';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	@Override
	public void sendProductsInCatalog(ArrayList<Product> products) {
		Method m = null;
		try {
			m = Context.currentGUI.getClass().getMethod("productsToGUI",ArrayList.class);
			m.invoke(Context.currentGUI, products);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method 'productsToComboBox'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called 'productsToComboBox'");
			e2.printStackTrace();
		}
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		
	}
}