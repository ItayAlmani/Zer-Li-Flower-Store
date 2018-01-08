package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;

public class ProductController extends ParentController {	
	
	public Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL) throws FileNotFoundException {
		return new Product(prdID, name, type,price,color,inCatalog, "/images/"+imageURL);
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> add(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
