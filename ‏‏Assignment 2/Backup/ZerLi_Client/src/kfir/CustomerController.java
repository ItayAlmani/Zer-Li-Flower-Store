package kfir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import common.Context;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.CreditCard;
import entities.Customer;
import entities.Order;
import entities.Order.Refund;
import entities.PaymentAccount;
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import entities.User;
import kfir.interfaces.ICustomer;

public class CustomerController extends UserController implements ICustomer {

	public PaymentAccount getPaymentAccount(Customer customer) {
		// TODO - implement CustomerController.getPaymentAccount
		throw new UnsupportedOperationException();
	}

	public CreditCard getCreditCard(String customerID) {
		// TODO - implement CustomerController.getCreditCard
		throw new UnsupportedOperationException();
	}

	public boolean updateRefundedOrderPrice(Order order, Refund refund) {
		// TODO - implement CustomerController.updateRefundedOrderPrice
		throw new UnsupportedOperationException();
	}

	public void updatePaymentAccountInDB(Customer customer) {
		// TODO - implement CustomerController.updatePaymentAccountInDB
		throw new UnsupportedOperationException();
	}


	public void getCustomer(BigInteger customerID) {
		
	}
	
	public void getCustomerByUser(BigInteger userID) {
		myMsgArr.clear();
		myMsgArr.add("SELECT *" + 
				" FROM customer" + 
				" WHERE userID='"+userID+"'");
		try {
			Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Customer.class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void SetPaymentAccount(String customerID, PaymentAccount account) {
		// TODO - implement CustomerController.SetPaymentAccount
		throw new UnsupportedOperationException();
	}

	public Subscription getSubscription(PaymentAccount account) {
		// TODO - implement CustomerController.getSubscription
		throw new UnsupportedOperationException();
	}

	public Subscription CreateSubscription(PaymentAccount account, SubscriptionType type) {
		// TODO - implement CustomerController.CreateSubscription
		throw new UnsupportedOperationException();
	}

	public void setSubscription(PaymentAccount account) {
		// TODO - implement CustomerController.setSubscription
		throw new UnsupportedOperationException();
	}

	public boolean billCreditCardOfCustomer(Customer customer, float amount) {
		//return billCard(customer.getPaymentAccount().getCreditCard(), amount);
		return new Random().nextBoolean();
	}

	//Suppose to be external function - the billing is external
	public boolean billCard(CreditCard cc, float amount) {
		return new Random().nextBoolean();
	}
	public void updateRefundAccount(String customerID, String refundAmount) {
		// TODO - implement CustomerController.updateRefundAccount
		throw new UnsupportedOperationException();
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<Customer> customers = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3)
			customers.add(parse(
					BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
					new User(BigInteger.valueOf(Long.valueOf((int) obj.get(i+1)))),
					new PaymentAccount(BigInteger.valueOf(Long.valueOf((int) obj.get(i+2))))
					));
		sendCustomers(customers);
	}

	@Override
	public Customer parse(BigInteger customerID, User user, PaymentAccount pa) {
		return new Customer(user, customerID, pa);
	}

	@Override
	public void sendCustomers(ArrayList<Customer> customers) {
		String methodName = "setCustomers";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl, customers);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, customers);
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
	public void getAllCustomers() {
		// TODO Auto-generated method stub
		
	}


}