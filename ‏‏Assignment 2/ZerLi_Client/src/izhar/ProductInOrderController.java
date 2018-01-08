package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Customer;
import entities.Product;
import entities.ProductInOrder;
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import izhar.interfaces.IProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrder {	
//------------------------------------------------IN CLIENT--------------------------------------------------------------------	
	public void updatePriceWithSubscription(ProductInOrder pio, Customer customer) {
		if(customer.getPaymentAccount()!= null && customer.getPaymentAccount().getSub() != null) {
			LocalDate date = customer.getPaymentAccount().getSub().getSubDate();
			SubscriptionType type = customer.getPaymentAccount().getSub().getSubType();
			if(type.equals(SubscriptionType.Monthly)) {
				if(date.plusMonths(1).isBefore(LocalDate.now()))
					pio.setFinalPrice(pio.getFinalPrice()*Subscription.getDiscountInPercent());
			}
			else if(type.equals(SubscriptionType.Yearly)) {
				if(date.plusYears(1).isBefore(LocalDate.now()))
					pio.setFinalPrice(pio.getFinalPrice()*Subscription.getDiscountInPercent());
			}
		}
	}
	
	public float calcFinalPrice(ProductInOrder p) {
		return p.getQuantity()*p.getProduct().getPrice();
	}
	
	public boolean isAllPIOsFromSameOrder(ArrayList<ProductInOrder> products) {
		BigInteger ordID = products.get(0).getOrderID();
		for (ProductInOrder pio : products) {
			if(pio.getOrderID().equals(ordID)==false)
				return false;
		}
		return true;
	}
	
	public ProductInOrder getPIOFromArr(ArrayList<ProductInOrder> prods, Product prod) {
		if(prod == null) return null;
		for (ProductInOrder pio : prods) {
			if(pio.getProduct()!=null &&
					pio.getProduct().getPrdID().equals(prod.getPrdID()))
				return pio;
		}
		return null;
	}
	
	@Override
	public void handleGet(ArrayList<ProductInOrder> pios) {
		String methodName = "setPIOs";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), pios);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, pios);
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
	public void add(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(p);
		arr.add(true);
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
