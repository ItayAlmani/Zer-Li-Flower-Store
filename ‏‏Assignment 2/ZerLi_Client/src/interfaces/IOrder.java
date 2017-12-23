package interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Complaint;
import entities.DeliveryDetails;
import entities.Order;
import enums.Refund;

public interface IOrder {

	/**
	 * asks from server an Order with orderid=<code>orderID</code>
	 * @param orderID - the id of the Order
	 * @throws IOException
	 */
	void getOrderWithProducts(String orderID) throws IOException;

	/**  */
	void noOrderIDErrMsg();

	/**
	 * 
	 * @param order
	 */
	Refund checkOrderRefund(Order order);

	/**
	 * 
	 * @param complaint
	 */
	boolean updateCustomerComplaintRefund(Complaint complaint);

	/**
	 * sending new order to server and asks to insert it to DataBase
	 * @param order - the Order which will be add
	 */
	void addNewOrder(Order order) throws IOException;

	/**
	 * 
	 * @param order
	 */
	String cancelOrder(Order order) throws IOException;

	/**
	 * 
	 * @param deliveryID
	 */
	DeliveryDetails getDelivery(String deliveryID) throws IOException;

	/**
	 * 
	 * @param delivery
	 */
	Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery);

	/**
	 * 
	 * @param order
	 */
	void updateOrder(Order order) throws IOException;

	/**
	 * 
	 * @param storeid
	 */
	public ArrayList<Order> getAllOrders(String storeid) throws IOException;

	/**
	 * 
	 * @param order
	 */
	void updatePriceWithShipment(Order order) throws IOException;

	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}
}