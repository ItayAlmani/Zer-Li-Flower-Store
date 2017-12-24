package izhar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import enums.DeliveryType;
import enums.OrderStatus;
import enums.OrderType;
import enums.Refund;
import enums.UserType;
import izhar.interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {

	@Override
	public void getOrderWithProducts(int orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT prd.*, ord.*" + "FROM orders ord, productincart pic, product prd, shoppingcart sc"
				+ "join orders ON sc.orderID=orders.orderID" +
				"join productincart ON sc.cartID=productincart.cartID"
				+ "where pic.productID = prd.productID and ord.orderID = '" + orderID + "';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

	@Override
	public void noOrderIDErrMsg() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateCustomerComplaintRefund(Complaint complaint) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addOrder(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"INSERT INTO orders (customerID, cartID, deliveryID, type, transactionID, greeting, deliveryType, status, date)"
						+ "VALUES ('" + order.getCustomerID() + "', '" 
						+ order.getOrderID() + "', '"
						+ order.getDelivery().getDeliveryID() + "', '"
						+ order.getType().toString() + "', '"
						+ order.getTransaction().getTansID() + "', '" 
						+ order.getGreeting() + "', '"
						+ order.getDeliveryType().toString() + "', '" 
						+ order.getOrderStatus().toString() + "', '"
						+ new Date(order.getDate().getTime()) + "');");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Order> ords = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 6)
			prds.add(parse((int) obj.get(i), (String) obj.get(i + 1), (String) obj.get(i + 2), (float) obj.get(i + 3),
					(String) obj.get(i + 4), ((int) obj.get(i + 5)) != 0));
		sendProducts(prds);
	}

	@Override
	public void cancelOrder(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("UPDATE orders SET status='" + OrderStatus.Canceled.toString() + "' WHERE orderID='"
				+ order.getOrderID() + "'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery) {
		Duration duration = Duration.between((Temporal) delivery.getDate(), LocalDate.now());
		long diff_in_hours = Math.abs(duration.toHours());
		if (diff_in_hours <= 1)
			return Refund.No;
		else if (diff_in_hours > 1 && diff_in_hours < 3)
			return Refund.Partial;
		else
			return Refund.Full;
	}

	@Override
	public void updateOrder(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("UPDATE orders " + "SET " + "customerID = " + order.getCustomerID() + ",cartID = "
				+ order.getOrderID() + ",deliveryID = " + order.getDelivery().getDeliveryID() + ",type = "
				+ order.getType().toString() + ",transactionID = " + order.getTransaction().getTansID() + ",greeting = "
				+ order.getGreeting() + ",deliveryType = " + order.getDeliveryType().toString() + ",status='"
				+ order.getOrderStatus().toString() + ",date = " + new Date(order.getDate().getTime())
				+ "' WHERE orderID='" + order.getOrderID() + "'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public void getAllOrdersByStoreID(int storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT * FROM(" +  
				"	SELECT ordStr.* FROM(" + 
				"		SELECT ord.*" + 
				"        FROM orders AS ord" + 
				"        JOIN deliverydetails ON ord.orderID=deliverydetails.orderID" + 
				"        WHERE deliverydetails.storeID='"+storeID+"') AS ordStr" + 
				"    JOIN shoppingcart ON ordStr.orderID=shoppingcart.orderID) AS ordStrCart" + 
				"JOIN productincart ON ordStrCart.cartID=productincart.cartID"
				);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

	@Override
	public void sendOrders(ArrayList<Order> orders) {
		Method m = null;
		try {
			m = Context.currentGUI.getClass().getMethod("ordersToComboBox",ArrayList.class);
			m.invoke(Context.currentGUI, orders);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method 'productsToComboBox'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called 'productsToComboBox'");
			e2.printStackTrace();
		}

	}

	@Override
	public void updatePriceWithShipment(Order order) throws IOException {
		order.getCart().setFinalPrice(order.getCart().getFinalPrice()+ShipmentDetails.shipmentPrice);
		updateOrder(order);
	}

	@Override
	public Order parse(int orderID, int customerID, int cartID, int deliveryID, String type, int transactionID,
			String greeting, String deliveryType, String orderStatus, java.util.Date date) {
		return new Order(orderID,
				customerID,
				new ShoppingCart(cartID), 
				new DeliveryDetails(deliveryID),
				OrderType.valueOf(type),
				new Transaction(transactionID), 
				greeting,
				DeliveryType.valueOf(deliveryType), 
				OrderStatus.valueOf(orderStatus), 
				date);
	}

}