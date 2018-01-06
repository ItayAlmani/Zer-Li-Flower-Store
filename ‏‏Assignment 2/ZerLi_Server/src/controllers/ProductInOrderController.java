package controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import common.ServerController;
import entities.ProductInOrder;
import entities.ShipmentDetails;
import entities.Order.DeliveryType;

public class ProductInOrderController {	
	
	public static BigInteger addPIO(ProductInOrder p) throws Exception {
		String query = "INSERT INTO cart (orderID, productID, quantity, totalprice) " + 
				" VALUES ('" 
				+ p.getOrderID()+ "', '"
				+ p.getProduct().getPrdID() + "', '"
				+ p.getQuantity() + "', '"
				+ p.getFinalPrice() + "');";
		ServerController.db.updateQuery(query);
		query="SELECT Max(productInOrderID) from cart";
		ArrayList<Object> arr =  ServerController.db.getQuery(query);
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)arr.get(0)+1);
		throw new Exception();
	}
}
