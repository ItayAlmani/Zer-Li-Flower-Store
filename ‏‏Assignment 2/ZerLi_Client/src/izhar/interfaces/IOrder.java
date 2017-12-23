package izhar.interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Complaint;
import entities.DeliveryDetails;
import entities.Order;
import enums.Refund;
import interfaces.IParent;

public interface IOrder extends IParent  {

	/**
	 * asks from server an Order with orderid=<code>orderID</code>
	 * @param orderID - the id of the Order
	 * @throws IOException
	 */
	void getOrderWithProducts(int orderID) throws IOException;

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
	DeliveryDetails getDelivery(int deliveryID) throws IOException;

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
	public ArrayList<Order> getAllOrders(int storeid) throws IOException;

	/**
	 * 
	 * @param order
	 */
	void updatePriceWithShipment(Order order) throws IOException;
}