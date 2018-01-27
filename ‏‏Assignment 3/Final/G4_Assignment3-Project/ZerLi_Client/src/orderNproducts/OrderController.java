package orderNproducts;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.CSMessage;
import common.CSMessage.MessageType;
import common.Context;
import common.ParentController;
import common.gui.controllers.ParentGUIController;
import orderNproducts.entities.DeliveryDetails;
import orderNproducts.entities.Order;
import orderNproducts.entities.Order.Refund;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Store;
import orderNproducts.interfaces.IOrder;
import usersInfo.entities.Customer;
import usersInfo.entities.PaymentAccount;

public class OrderController extends ParentController implements IOrder {
//------------------------------------------------IN CLIENT--------------------------------------------------------------------	
	@Override
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
				pio.addOneToQuantity();
				pio.setFinalPrice();
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
	
	@Override
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

	@Override
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
//------------------------------------------------IN SERVER--------------------------------------------------------------------
	@Override
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
	public void getOrdersByCustomerID(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(customerID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}
	
	@Override
	public void getCancelableOrdersByCustomerID(BigInteger customerID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(customerID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, Order.class));
	}

//--------------------------------------------------------------------------------------------------------------------
}