package izhar.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import common.ClientConsole;
import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.Refund;
import entities.PaymentAccount;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import gui.controllers.ParentGUIController;

public interface IOrder  {
	/**
	 * sending new {@link Order} to server and asks to insert it to DataBase
	 * @param order - the {@link Order} which will be add
	 * @param getNextID - indicates if we want to get the id of the new {@link Order} (<code>true</code>) or not(<code>false</code>)
	 */
	void add(Order order, boolean getNextID) throws IOException;

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
	 * Updating {@link Order}'s price by {@link PaymentAccount#getRefundAmount()} if exists
	 * @param pa
	 * @param order
	 * @param customer
	 * @return TODO
	 * @throws IOException
	 */
	Float getFinalPriceByPAT(PaymentAccount pa, Order order, Customer customer) throws IOException;
	
	/**
	 * send the response from server to the correct GUI which asked the Orders
	 * @param orders - collection of orders
	 */
	void handleGet(ArrayList<Order> orders);
	
	/**
	 * Asks from the Server {@link Order}s where {@link Order#getOrderStatus()}='InProcess', by the parameters.
	 * @param customerID the {@link Order} belongs to the {@link Customer} with this ID
	 * @param store the {@link Order} is in {@link Store} with this ID
	 * @throws IOException {@link ClientConsole#handleMessageFromClientUI(entities.CSMessage)}
	 */
	void getOrAddOrderInProcess(BigInteger customerID, Store store) throws IOException;

	/**
	 * Asks from the Server {@link Order}s by customerID
	 * @param customerID the {@link Order} belongs to the {@link Customer} with this ID
	 * @throws IOException {@link ClientConsole#handleMessageFromClientUI(entities.CSMessage)}
	 */
	void getOrdersByCustomerID(BigInteger customerID) throws IOException;
	
	/**
	 * Updating the order with the {@link ProductInOrder}
	 * @param p the {@link Product} that need to be in the order
	 * @param price the price of the product (maybe after sales and discounts)
	 * @param pio the {@link ProductInOrder} to add to the cart with the new {@link Product}
	 * @param stock the {@link Stock} with the sales data of the {@link Product}
	 * @return the {@link ProductInOrder} with the new data
	 */
	ProductInOrder manageCart(Product p, Float price, ProductInOrder pio, Stock stock);
	
	/**
	 * Get the {@link Order} id after doing {@link #add(Order, boolean)} when <code>getNextID = true</code>
	 * and sends it to the asker (by {@link ParentGUIController#currentGUI} or {@link Context#askingCtrl}
	 * @param id the id of the new row in the DataBase
	 */
	void handleInsert(BigInteger id);
	
	/**
	 * Calculates the {@link Order} price by it's {@link Order#getProducts()} and updates it
	 * @param order the {@link Order} to update it's price
	 */
	void calcFinalPriceOfOrder(Order order);

	/**
	 * Asks from the Server {@link Order}s by customerID where {@link Order#getOrderStatus()} is 'Paid'
	 * and the {@link Order#getDelivery()} date hasn't pass
	 * @param customerID the {@link Order} belongs to the {@link Customer} with this ID
	 * @throws IOException {@link ClientConsole#handleMessageFromClientUI(entities.CSMessage)}
	 */
	void getCancelableOrdersByCustomerID(BigInteger customerID) throws IOException;
}