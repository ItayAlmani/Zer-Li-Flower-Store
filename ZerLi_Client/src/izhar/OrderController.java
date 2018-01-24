package izhar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.OrderStatus;
import entities.Order.Refund;
import entities.PaymentAccount;
import entities.Product;
import entities.ProductInOrder;
import entities.ShipmentDetails;
import entities.Stock;
import entities.Store;
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import gui.controllers.ParentGUIController;
import izhar.interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {
//------------------------------------------------IN CLIENT--------------------------------------------------------------------	
	@SuppressWarnings("unused")
	public ProductInOrder manageCart(Product p, Float price, ProductInOrder pio, Stock stock) {
		Product prd = new Product(p);	//create new copy
		Order ord = Context.order;
		pio = Context.order.containsProduct(prd);
		
		if(pio==null) {
			pio = new ProductInOrder(prd, 1, ord.getOrderID());
			pio.getProduct().setPrice(price);
			pio.setFinalPrice();
			if(ord.getProducts()==null)
				ord.setProducts(new ArrayList<>());
			ord.getProducts().add(pio);
			try {
				Context.mainScene.setMenuPaneDisable(true);
				Context.fac.prodInOrder.add(pio, true);
				//Auto update of stock
				stock.setQuantity(stock.getQuantity()-1);
				Context.fac.stock.update(stock);
			} catch (IOException e) {
				System.err.println("Can't add product\n");
				Context.mainScene.ShowErrorMsg();
				return null;
			}
		}
		else {
			try {
				pio.getProduct().setPrice(price);
				pio.setFinalPrice();
				pio.addOneToQuantity();
				Context.fac.prodInOrder.update(pio);
			} catch (Exception e) {
				System.err.println("Can't update product\n");
				Context.mainScene.ShowErrorMsg();
				return null;
			}
		}
		Context.fac.order.calcFinalPriceOfOrder(ord);
		return pio;
	}
	
	public void handleInsert(BigInteger id) {
		String methodName = "setOrderID";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,BigInteger.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, id);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,BigInteger.class);
				m.invoke(ParentGUIController.currentGUI, id);
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

	@Override
	public Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery) {
		Duration duration = Duration.between(delivery.getDate(), LocalDateTime.now());
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
	public Float getFinalPriceByPAT(PaymentAccount pa, Order order, Customer customer) throws IOException {
		if(pa != null && pa.getRefundAmount() > 0 && order != null) {
			if(pa.getRefundAmount() > order.getFinalPrice()) {
				//pa.setRefundAmount(pa.getRefundAmount()-order.getFinalPrice());
				//order.setFinalPrice(0f);
				return 0f;
			}
			else {
				//order.setFinalPrice(order.getFinalPrice()-pa.getRefundAmount());
				//pa.setRefundAmount(0f);
				return order.getFinalPrice()-pa.getRefundAmount();
			}
		}
		return null;
	}

	public boolean isCancelable(Order order) {
		return order.getOrderStatus().equals(OrderStatus.Paid) && 
				order.getDelivery()!=null &&
				order.getDelivery().getStore() != null &&
				LocalDateTime.now().isBefore(order.getDelivery().getDate());
	}
//------------------------------------------------IN SERVER--------------------------------------------------------------------
	
	public void handleGet(ArrayList<Order> orders) {
		String methodName = "setOrders";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, orders);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, orders);
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
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(order);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Order.class));
	}
	
	public void add(Order order, boolean getNextID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(order);
		arr.add(getNextID);
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
	public void getOrAddOrderInProcess(BigInteger customerID, Store store) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(customerID);
		arr.add(store);
		myMsgArr.add(arr);
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
	
	public void getCancelableOrdersByCustomerID(BigInteger customerID) throws IOException {
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