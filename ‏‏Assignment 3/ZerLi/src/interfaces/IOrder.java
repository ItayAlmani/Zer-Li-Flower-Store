package interfaces;

import java.util.ArrayList;

import entities.Complaint;
import entities.DeliveryDetails;
import entities.Order;
import entities.Refund;

public interface IOrder {

	/**
	 * 
	 * @param orderID
	 */
	Order getOrder(String orderID);

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
	 * 
	 * @param order
	 */
	void insertOrderToDB(Order order);

	/**
	 * 
	 * @param order
	 */
	String cancelOrder(Order order);

	/**
	 * 
	 * @param deliveryID
	 */
	DeliveryDetails getDeliveryByID(String deliveryID);

	/**
	 * 
	 * @param delivery
	 */
	Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery);

	/**
	 * 
	 * @param order
	 */
	void updateOrderInDB(Order order);

	/**
	 * 
	 * @param storeid
	 */
	public ArrayList<Order> getOrdersByStore(String storeid);

	/**
	 * 
	 * @param order
	 */
	void updatePriceWithShipment(Order order);

}