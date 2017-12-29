package izhar.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

import entities.*;
import entities.Order.*;
import interfaces.IParent;

public interface IOrder extends IParent  {

	/**  */
	void noOrderIDErrMsg();

	/**
	 * update status to canceled
	 * @param complaint
	 */
	boolean updateCustomerComplaintRefund(Complaint complaint);

	/**
	 * sending new order to server and asks to insert it to DataBase
	 * @param order - the Order which will be add
	 */
	void addOrder(Order order) throws IOException;

	/**
	 * asks the server to update the <code>Order</code>'s <code>status</code> attribute
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
	void updateOrder(Order order) throws IOException;

	/**
	 * asks the server for all the <code>Order</code>s by <code>storeID</code>
	 * @param storeID - the parameter to find the <code>Order</code>s
	 */
	public void getAllOrdersByStoreID(int storeID) throws IOException;
	
	/**
	 * send the response from server to the correct GUI which asked the Orders
	 * @param orders - collection of orders
	 */
	public void sendOrders(ArrayList<Order> orders);

	public void getOrdersWaitingForPaymentByCustomerID(int customerID) throws IOException;
	
	
	/**
	 * adds the shipment's price to the final price of the order 
	 * (in cart there is the final price)
	 * @param order - the order which price needs to be updated
	 */
	void updatePriceWithShipment(Order order) throws IOException;
	
	/**
	 * parsing the data into new Order object
	 * @param orderID		-	the order's ID
	 * @param customerID	-	the customer who made the order's ID
	 * @param cartID		-	the cart which contains all the products in order ID
	 * @param deliveryID	-	the delivery's details's ID
	 * @param type			-	the type of order by the ENUM
	 * @param transactionID	-	the transaction of the whole order's ID
	 * @param greeting		-	the greeting which can be attached to the order
	 * @param deliveryType	-	the delivery type by the ENUM
	 * @param orderStatus	-	the order's status by the ENUm
	 * @param date			-	the order's date
	 * @return new object created by the data above
	 */
	Order parse(BigInteger orderID, int customerID, int cartID, int deliveryID, String type,
			int transactionID, String greeting, String deliveryType, 
			String orderStatus, Date date);
	
	/**
	 * asks from server an Order with orderid=<code>orderID</code>
	 * @param orderID - the id of the Order
	 * @throws IOException
	 */
	void getProductsInOrder(BigInteger orderID) throws IOException;
	
	void addProductInOrderToOrder(ProductInOrder product);
	
	
	void updateFinalPriceByPAT(PaymentAccount pa);
	
	void getOrderInProcess(int customerID) throws IOException;

	void getLastAutoIncrenment() throws IOException;

	void getOrdersByCustomerID(int customerID) throws IOException;
}