package interfaces;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.Customer;
import orderNproducts.entities.Order;

public interface IOrder {

	/**
	 * Gets all the {@link Order}s by customerID
	 * @param customerID the {@link Order} belongs to the {@link Customer} with this ID
	 * @return all the {@link Order}s by customerID
	 * @throws Exception
	 */
	ArrayList<Object> getOrdersByCustomerID(BigInteger customerID) throws Exception;

	/**
	 * Looking for {@link Order}s by customerID where {@link Order#getOrderStatus()} is 'Paid'
	 * and the {@link Order#getDelivery()} date hasn't pass
	 * @param customerID the {@link Order} belongs to the {@link Customer} with this ID
	 * @return {@link Order}s by customerID where {@link Order#getOrderStatus()} is 'Paid'
	 * and the {@link Order#getDelivery()} date hasn't pass
	 * @throws Exception
	 */
	ArrayList<Object> getCancelableOrdersByCustomerID(BigInteger customerID) throws Exception;

	/**
	 * Gets all {@link Order}s where {@link Order#getOrderStatus()}='InProcess', by the parameters from DB.
	 * @param arr contains 2 parameters: customerID and store
	 * @return all {@link Order}s (by the description)
	 * @throws Exception
	 */
	ArrayList<Object> getOrAddOrderInProcess(ArrayList<Object> arr) throws Exception;

	/**
	 * Gets all the {@link Order}s by <code>storeID</code> and <code>dates</code>
	 * @param storeID	- the parameter to find the <code>Order</code>s
	 * @param startDate	- the first day of the quarter	(01.MM.YY)
	 * @param endDate 	- the last day of the quarter	(31.MM+3.YY)
	 * @return all {@link Order}s (by the description)
	 * @throws Exception
	 */
	ArrayList<Object> getOrdersForReportByStoreID(BigInteger storeID, LocalDate startDate, LocalDate endDate)
			throws Exception;

	/**
	 * 
	 * @param orderID
	 * @return
	 * @throws Exception
	 */
	ArrayList<Object> getProductsInOrder(BigInteger orderID) throws Exception;

	/**
	 * parsing the data into new Order object
	 * @param orderID		-	the order's ID
	 * @param customerID	-	the customer who made the order's ID
	 * @param deliveryID	-	the delivery's details's ID (can be NULL)
	 * @param payMethod		-	the payment method
	 * @param shipmentID	-	the shipment ID (can be NULL)
	 * @param type			-	the type of order by the ENUM
	 * @param greeting		-	the greeting which can be attached to the order
	 * @param deliveryType	-	the delivery type by the ENUM
	 * @param orderStatus	-	the order's status by the ENUm
	 * @param date			-	the order's date
	 * @param price			-	the price of the order
	 * @return new {@link Order} created by the data above
	 * @throws Exception
	 */
	Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethod,
			BigInteger shipmentID, String type, String greeting, String deliveryType, String orderStatus,
			Timestamp date, float price) throws Exception;

}