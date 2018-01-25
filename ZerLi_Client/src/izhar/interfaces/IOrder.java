package izhar.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;

import entities.*;
import entities.Order.*;
import interfaces.IParent;

public interface IOrder  {
	/**
	 * sending new order to server and asks to insert it to DataBase
	 * @param order - the Order which will be add
	 */
	void add(Order order, boolean getNextID) throws IOException;

	/**
	 * asks the server to update the {@link Order}'s <code>status</code> attribute
	 * to be <b>canceled</b>
	 * @param order - the object with the <code>orderID</code>
	 */
	void cancelOrder(Order order) throws IOException;

	/**
	 * Calculate refund by difference of current time and delivery time
	 * @param delivery - the delivery object which it's time will be checked
	 */
	Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery);

	/**
	 * asks the server to update the <code>Order</code> with <code>orderID</code>
	 * by the data in <code>order</code>
	 * @param order - the object with the new data and the <code>orderID</code>
	 */
	void update(Order order) throws IOException;

	/**
	 * asks the server for all the <code>Order</code>s by <code>storeID</code>
	 * @param storeID - the parameter to find the <code>Order</code>s
	 */
	public void getAllOrdersByStoreID(BigInteger storeID) throws IOException;

	public void getOrdersWaitingForPaymentByCustomerID(BigInteger customerID) throws IOException;
	
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
/*	Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethodID, BigInteger shipmentID, String type,
			String greeting, String deliveryType, String orderStatus, Timestamp date, float price);*/
	/*Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethod,
			BigInteger shipmentID, String type, String greeting, String deliveryType, String orderStatus,
			Timestamp date, float price);*/
	
	/**
	 * asks from server an Order with orderid=<code>orderID</code>
	 * @param orderID - the id of the Order
	 * @throws IOException
	 */
	void getProductsInOrder(BigInteger orderID) throws IOException;
	
	/**
	 * Updating {@link Order}'s price by {@link PaymentAccount#getRefundAmount()} if exists
	 * @param pa
	 * @param order
	 * @param customer
	 * @return TODO
	 * @throws IOException
	 */
	Float getFinalPriceByPAT(PaymentAccount pa, Order order, Customer customer) throws IOException;
	
	void getOrAddOrderInProcess(BigInteger customerID, Store store) throws IOException;

	void getOrdersByCustomerID(BigInteger customerID) throws IOException;
	
	/**
	 * send the response from server to the correct GUI which asked the Orders
	 * @param orders - collection of orders
	 */
	void handleGet(ArrayList<Order> orders);
}