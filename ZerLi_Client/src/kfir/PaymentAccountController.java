package kfir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CreditCard;
import entities.Customer;
import entities.Order;
import entities.PaymentAccount;
import entities.Store;
import gui.controllers.ParentGUIController;
import interfaces.IParent;
import entities.CSMessage.MessageType;

public class PaymentAccountController extends ParentController{

	/**
	 * 
	 * @param customerID
	 * @throws IOException
	 */
	public void add(PaymentAccount pa, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(pa);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr, PaymentAccount.class));
	}

	public void handleGet(ArrayList<PaymentAccount> pa) {
		String methodName = "setPayment";
		Method m = null;
		try {
			// a controller asked data, not GUI
			if (Context.askingCtrl != null && Context.askingCtrl.size() != 0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName, ArrayList.class);
				Object ob = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(ob, pa);
			} else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName, ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, pa);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '" + methodName + "'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '" + methodName + "'");
			e2.printStackTrace();
		}
	}

	public void getPayAccount(BigInteger custID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(custID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,PaymentAccount.class));
	}

	public void update(PaymentAccount paymentAccount) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(paymentAccount);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,PaymentAccount.class));
	}
	
	/**
	 * looks for {@link PaymentAccount} in the {@link ArrayList}, where the {@link PaymentAccount}
	 * related to the specific {@link Store} and return it. If not exist, returns null.
	 * @param pas
	 * @param s
	 * @return
	 * @throws Exception 
	 */
	public PaymentAccount getPaymentAccountOfStore(ArrayList<PaymentAccount> pas, Store s) throws Exception {
		if(pas == null || s == null) throw new Exception();
		for (PaymentAccount pa : pas) {
			if(pa.getStore() == null || pa.getStore().getStoreID() == null || s.getStoreID() == null)
				throw new Exception();
			if(pa.getStore().getStoreID().intValue()==s.getStoreID().intValue())
				return pa;
		}
		return null;
	}
	public void handleInsert(BigInteger id) {
		String methodName = "setPAid";
		Method m = null;
		try {
			// a controller asked data, not GUI
			if (Context.askingCtrl != null && Context.askingCtrl.size() != 0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName, BigInteger.class);
				Object ob = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(ob, id);
			} else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName, BigInteger.class);
				m.invoke(ParentGUIController.currentGUI, id);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '" + methodName + "'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '" + methodName + "'");
			e2.printStackTrace();
		}
	}

}
