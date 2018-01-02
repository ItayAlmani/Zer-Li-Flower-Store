package izhar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.PayMethod;
import entities.Order.Refund;
import entities.Subscription.SubscriptionType;
import izhar.interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {
	
	public void setLastAutoIncrenment(ArrayList<Object> obj) throws IOException {
		Context.order = new Order((BigInteger)obj.get(10));
		Order.setIdInc((BigInteger)obj.get(10));
	}
	
	/*public void setCreatedRow(Integer id){
		Order.setIdInc(BigInteger.valueOf(id));
		
	}*/

	public boolean isCartEmpty(ArrayList<ProductInOrder> products) {
		for (ProductInOrder pio : products)
			if(pio.getQuantity()!=0)
				return false;
		return true;
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
		String delID ="NULL", shipID="NULL", payMeth = "NULL", delType="NULL", greeting = "NULL";
		if(order.getDeliveryType() != null) {
			if(order.getDeliveryType().equals(DeliveryType.Pickup) && order.getDelivery() != null)
				delID="'"+order.getDelivery().getDeliveryID().toString()+"'";
			else if(order.getDeliveryType().equals(DeliveryType.Shipment) && order.getDelivery() != null)
				shipID="'"+((ShipmentDetails)order.getDelivery()).getOrderID().toString()+"'";
			delType="'"+order.getDeliveryType().toString()+"'";
		}
		if(order.getPaymentMethod()!=null)
			payMeth="'"+order.getPaymentMethod().toString()+"'";
		if(order.getGreeting()!=null)
			greeting = "'"+order.getGreeting()+"'";
		String query = "INSERT INTO orders (customerID, deliveryID, type, paymentMethod, shipmentID, greeting, deliveryType, status, date, price)"
				+ "VALUES ('" + order.getCustomerID() + "'"
				+ ", " + delID + ", '"
				+ order.getType().toString() + "', "
				+ payMeth + ", " 
				+ shipID + ", "
				+  greeting + ", "
				+ delType + ", '" 
				+ order.getOrderStatus().toString() + "', '"
				+ (Timestamp.valueOf(order.getDate())).toString() + "','"+
				 order.getFinalPrice()+"');";
		query += "SELECT Max(orderID) from orders;";
		myMsgArr.add(query);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Order.class));
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Order> ords = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 11) {
			BigInteger deliveryID = obj.get(i + 2)==null?null:BigInteger.valueOf(Long.valueOf((int)obj.get(i + 2))),
					shipmentID=obj.get(i + 4)==null?null:BigInteger.valueOf(Long.valueOf((int) obj.get(i + 4)));
			
			
			ords.add(parse(
					BigInteger.valueOf(Long.valueOf((int)obj.get(i))), 
					BigInteger.valueOf(Long.valueOf((int) obj.get(i + 1))),
					deliveryID, 
					obj.get(i + 3)==null?null:(String)obj.get(i + 3),
					shipmentID,
					obj.get(i + 5)==null?null:(String)obj.get(i + 5),
					(String) obj.get(i + 6),
					(String) obj.get(i + 7),
					(String) obj.get(i + 8),
					(Timestamp)obj.get(i + 9),
					(float)obj.get(i+10)
					));
		}
		sendOrders(ords);
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
		String delID ="NULL", shipID="NULL", payMeth = "NULL", delType="NULL", greeting = "NULL";
		if(order.getDeliveryType() != null) {
			if(order.getDeliveryType().equals(DeliveryType.Pickup) && order.getDelivery() != null)
				delID="'"+order.getDelivery().getDeliveryID().toString()+"'";
			else if(order.getDeliveryType().equals(DeliveryType.Shipment) && order.getDelivery() != null)
				shipID="'"+((ShipmentDetails)order.getDelivery()).getOrderID().toString()+"'";
			delType="'"+order.getDeliveryType().toString()+"'";
		}
		if(order.getPaymentMethod()!=null)
			payMeth="'"+order.getPaymentMethod().toString()+"'";
		if(order.getGreeting()!=null)
			greeting = "'"+order.getGreeting()+"'";
		myMsgArr.add("UPDATE orders SET customerID = '" + order.getCustomerID() 
		+ "',deliveryID = " + delID 
		+ ",type = '" + order.getType().toString() 
		+ "',paymentMethod = " + payMeth
		+ ",shipmentID = " + shipID
		+ ",greeting = " + greeting 
		+ ",deliveryType = " + delType
		+ ",status='" + order.getOrderStatus().toString() 
		+ "',date = '" + (Timestamp.valueOf(order.getDate())).toString()
		+ "',price = '"+ order.getFinalPrice()
		+ "' WHERE orderID='" + order.getOrderID() + "'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public void getAllOrdersByStoreID(BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"SELECT ord.*" + 
				" FROM orders AS ord" + 
				" JOIN deliverydetails ON ord.orderID=deliverydetails.orderID" + 
				" WHERE deliverydetails.storeID='"+storeID+"'"
				);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}
	
	public void calcFinalPriceOfOrder(Order order) {
		float price = 0f;
		for (ProductInOrder p : order.getProducts())
			price+=p.getFinalPrice();
		order.setFinalPrice(price);
	}

	@Override
	public void sendOrders(ArrayList<Order> orders) {
		String methodName = "setOrders";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), orders);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, orders);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	@Override
	public void updatePriceWithShipment(Order order) throws IOException {
		order.setFinalPrice(order.getFinalPrice()+ShipmentDetails.shipmentPrice);
		//updateOrder(order);
	}

	@Override
	public Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethod, BigInteger shipmentID, String type,
			String greeting, String deliveryType, String orderStatus, Timestamp date, float price) {
		LocalDateTime ldtDate = date.toLocalDateTime();
		if(deliveryType==null)
			return new Order(orderID,
					customerID,
					OrderType.valueOf(type),
					payMethod==null?null:PayMethod.valueOf(payMethod), 
					greeting,
					OrderStatus.valueOf(orderStatus), 
					ldtDate,
					price);
		else if(DeliveryType.valueOf(deliveryType).equals(DeliveryType.Pickup))
			return new Order(orderID,
					customerID,
					new DeliveryDetails(deliveryID),
					OrderType.valueOf(type),
					PayMethod.valueOf(payMethod), 
					greeting,
					DeliveryType.valueOf(deliveryType), 
					OrderStatus.valueOf(orderStatus), 
					ldtDate, 
					price);
		else if(DeliveryType.valueOf(deliveryType).equals(DeliveryType.Shipment))
			return new Order(orderID,
					customerID,
					new ShipmentDetails(shipmentID),
					OrderType.valueOf(type),
					PayMethod.valueOf(payMethod), 
					greeting,
					DeliveryType.valueOf(deliveryType), 
					OrderStatus.valueOf(orderStatus), 
					ldtDate,
					price);
		else return null;
	}

	@Override
	public void getProductsInOrder(BigInteger orderID) throws IOException {
		myMsgArr.add("SELECT productID, quantity, totalprice FROM" + 
				"(" + 
				"	SELECT ordCart.* FROM" + 
				"	(" + 
				"		SELECT crt.*" + 
				"		FROM cart AS crt" + 
				"		JOIN orders ON crt.orderID=orders.orderID" + 
				"		where crt.orderID = '"+orderID+"'" + 
				"	) AS ordCart" + 
				"	JOIN product ON ordCart.productID=product.productID" + 
				") AS prodInOrd");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Product.class));
		
	}

	@Override
	public void addProductInOrderToOrder(ProductInOrder product) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateFinalPriceByPAT(PaymentAccount pa) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getOrderInProcess(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM orders WHERE customerID='"+	customerID+"' AND status='InProcess';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

	@Override
	public void getOrdersWaitingForPaymentByCustomerID(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM orders WHERE customerID='"+	customerID+"' AND status='WaitingForCashPayment'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}
	
	@Override
	public void getOrdersByCustomerID(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM orders WHERE customerID='"+	customerID+"'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

	public void updatePriceWithSubscription(Order order, Customer customer) {
		if(customer.getPaymentAccount()!= null && customer.getPaymentAccount().getSub() != null) {
			LocalDate date = customer.getPaymentAccount().getSub().getSubDate();
			SubscriptionType type = customer.getPaymentAccount().getSub().getSubType();
			if(type.equals(SubscriptionType.Monthly)) {
				if(date.plusMonths(1).isBefore(LocalDate.now()))
					order.setFinalPrice(order.getFinalPrice()*Subscription.getDiscountInPercent());
			}
			else if(type.equals(SubscriptionType.Yearly)) {
				if(date.plusYears(1).isBefore(LocalDate.now()))
					order.setFinalPrice(order.getFinalPrice()*Subscription.getDiscountInPercent());
			}
		}
	}
}