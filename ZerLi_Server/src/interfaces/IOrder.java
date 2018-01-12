package interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import entities.*;
import entities.Order.*;

public interface IOrder  {
	/**
	 * sending new order to server and asks to insert it to DataBase
	 * @param order - the Order which will be add
	 * @return 
	 * @throws Exception 
	 */
	//ArrayList<Object> add(Object order) throws Exception;

	/**
	 * asks the server to update the <code>Order</code>'s <code>status</code> attribute
	 * to be <b>canceled</b>
	 * @param order - the object with the <code>orderID</code>
	 * @return 
	 */
	ArrayList<Object> cancelOrder(Order order) throws SQLException;

	/**
	 * asks the server to update the <code>Order</code> with <code>orderID</code>
	 * by the data in <code>order</code>
	 * @param order - the object with the new data and the <code>orderID</code>
	 * @return 
	 * @throws Exception 
	 */
	//ArrayList<Object> update(ArrayList<Object> arr) throws Exception;

	/**
	 * asks the server for all the <code>Order</code>s by <code>storeID</code>
	 * @param storeID - the parameter to find the <code>Order</code>s
	 * @return 
	 */
	public ArrayList<Object> getAllOrdersByStoreID(BigInteger storeID) throws SQLException;

	public ArrayList<Object> getOrdersWaitingForPaymentByCustomerID(ArrayList<Object> arr) throws Exception;
	
	/**
	 * adds the shipment's price to the final price of the order 
	 * (in cart there is the final price)
	 * @param order - the order which price needs to be updated
	 */
	//void updatePriceWithShipment(Order order) throws SQLException;
	
	/**
	 * asks from server an Order with orderid=<code>orderID</code>
	 * @param orderID - the id of the Order
	 * @return 
	 * @throws IOException
	 */
	ArrayList<Object> getProductsInOrder(BigInteger orderID) throws SQLException;
	
	//void addProductInOrderToOrder(ProductInOrder product);
	
	ArrayList<Object> getOrderInProcess(BigInteger customerID) throws SQLException;

	ArrayList<Object> getOrdersByCustomerID(BigInteger customerID) throws SQLException;
	
	/**
	 * parsing the data into new Order object
	 * @param orderID		-	the order's ID
	 * @param customerID	-	the customer who made the order's ID
	 * @param deliveryID	-	the delivery's details's ID
	 * @param transactionID	-	the transaction of the whole order's ID
	 * @param type			-	the type of order by the ENUM
	 * @param greeting		-	the greeting which can be attached to the order
	 * @param deliveryType	-	the delivery type by the ENUM
	 * @param orderStatus	-	the order's status by the ENUm
	 * @param date			-	the order's date
	 * @param price TODO
	 * @param cartID		-	the cart which contains all the products in order ID
	 * @return new object created by the data above
	 */
	Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethod, BigInteger shipmentID, String type,
			String greeting, String deliveryType, String orderStatus, Timestamp date, float price);
}