package izhar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import entities.Subscription.SubscriptionType;
import gui.controllers.ParentGUIController;
import izhar.interfaces.IProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrder {	
//------------------------------------------------IN CLIENT--------------------------------------------------------------------	
	public void updatePriceWithSubscription(Order order, ProductInOrder pio, Customer customer) {
		PaymentAccount pa = null;
		if(order.getDelivery() != null && order.getDelivery().getStore() != null)
			pa = Context.fac.paymentAccount.getPaymentAccountOfStore(
					customer.getPaymentAccounts(), order.getDelivery().getStore());
		if(pa!= null && pa.getSub() != null && pa.getSub().getSubType() != null) {
			LocalDate date = pa.getSub().getSubDate();
			SubscriptionType type = pa.getSub().getSubType();
			if(type.equals(SubscriptionType.Monthly)) {
				if(date.plusMonths(1).isBefore(LocalDate.now()))
					pio.setFinalPrice(pio.getFinalPrice()*(1-Subscription.getDiscountInPercent()));
			}
			else if(type.equals(SubscriptionType.Yearly)) {
				if(date.plusYears(1).isBefore(LocalDate.now()))
					pio.setFinalPrice(pio.getFinalPrice()*(1-Subscription.getDiscountInPercent()));
			}
		}
	}
	
	public float calcFinalPrice(ProductInOrder p) {
		return p.getQuantity()*p.getProduct().getPrice();
	}
	
	public ProductInOrder getPIOFromArr(ArrayList<ProductInOrder> prods, Product prod) {
		if(prod == null || prods == null) return null;
		for (ProductInOrder pio : prods) {
			if(pio.getProduct()!=null &&
					pio.getProduct().getPrdID().equals(prod.getPrdID()))
				return pio;
		}
		return null;
	}
	
	private void saveProductsImages(ArrayList<ProductInOrder> pios) {
		ArrayList<Product> prds = new ArrayList<>();
		for (ProductInOrder pio : pios)
			prds.add(pio.getProduct());
	}
	
	@Override
	public void handleGet(ArrayList<ProductInOrder> pios) {
		saveProductsImages(pios);
		String methodName = "setPIOs";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, pios);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, pios);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	public void handleInsert(BigInteger id) {
		String methodName = "setPIOID";
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
//------------------------------------------------IN SERVER--------------------------------------------------------------------
	@Override
	public void getPIOsByOrder(BigInteger orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(orderID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, ProductInOrder.class));
	}

	@Override
	public void update(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,ProductInOrder.class));
	}
	
	@Override
	public void add(ProductInOrder p, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(p);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,ProductInOrder.class));
	}
	
	public void updatePriceOfPIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr, ProductInOrder.class));
	}
//-------------------------------------------------------------------------------------------------------------------------
	
}
