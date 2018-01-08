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
//------------------------------------------------IN CLIENT--------------------------------------------------------------------	
	public void handleInsert(BigInteger id) {
		String methodName = "setOrderID";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,BigInteger.class);
				m.invoke(Context.askingCtrl.get(0), id);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,BigInteger.class);
				m.invoke(Context.currentGUI, id);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	public boolean isCartEmpty(ArrayList<ProductInOrder> products) {
		for (ProductInOrder pio : products)
			if(pio.getQuantity()!=0)
				return false;
		return true;
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

	public void calcFinalPriceOfOrder(Order order) {
		float price = 0f;
		for (ProductInOrder p : order.getProducts())
			price+=p.getFinalPrice();
		order.setFinalPrice(price);
	}

	@Override
	public void updateFinalPriceByPAT(PaymentAccount pa) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addProductInOrderToOrder(ProductInOrder product) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updatePriceWithShipment(Order order) throws IOException {
		order.setFinalPrice(order.getFinalPrice()+ShipmentDetails.shipmentPrice);
	}

//------------------------------------------------IN SERVER--------------------------------------------------------------------
	
	public void handleGet(ArrayList<Order> orders) {
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
	public void update(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(order);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Order.class));
	}
	
	@Override
	public void cancelOrder(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("UPDATE orders SET status='" + OrderStatus.Canceled.toString() + "' WHERE orderID='"
				+ order.getOrderID() + "'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Order.class));
	}
	
	public void add(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(order);
		arr.add(true);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,Order.class));
	}
	
	@Override
	public void getProductsInOrder(BigInteger orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(orderID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Product.class));
	}

	@Override
	public void getOrderInProcess(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(customerID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

	@Override
	public void getOrdersWaitingForPaymentByCustomerID(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(customerID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}
	
	@Override
	public void getOrdersByCustomerID(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(customerID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

	@Override
	public void getAllOrdersByStoreID(BigInteger storeID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(storeID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}
	
	
//--------------------------------------------------------------------------------------------------------------------
}