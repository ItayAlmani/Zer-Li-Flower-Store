package kfir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.CreditCard;
import entities.Customer;
import entities.Order;
import entities.Order.Refund;
import entities.PaymentAccount;
import entities.Product;
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import entities.User;
import gui.controllers.ParentGUIController;
import kfir.interfaces.ICustomer;

public class CustomerController extends ParentController implements ICustomer {

	public void getCustomer(BigInteger customerID) {
		
	}
	
	public void getCustomerByUser(BigInteger userID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(userID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Customer.class));
	}
	
	public boolean billCreditCardOfCustomer(Customer customer, float amount) {
		//return billCard(customer.getPaymentAccount().getCreditCard(), amount);
		return new Random().nextBoolean();
	}

	/**
	 * Suppose to be external function - the billing is external
	 * @param cc
	 * @param amount
	 * @return
	 */
	public boolean billCard(CreditCard cc, float amount) {
		return new Random().nextBoolean();
	}

	public void handleGet(ArrayList<Customer> customers) {
		String methodName = "setCustomers";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, customers);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, customers);
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
	public void getCustomerByPrivateID(String privateID) throws IOException {
				
	}

	@Override
	public void getAllCustomers() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Customer.class));
	}
}