package kfir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.PaymentAccount;
import entities.Store;
import gui.controllers.ParentGUIController;
import kfir.interfaces.IPaymentAccount;
import entities.CSMessage.MessageType;

public class PaymentAccountController extends ParentController implements IPaymentAccount{

	@Override
	public void add(PaymentAccount pa, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(pa);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr, PaymentAccount.class));
	}
	
	@Override
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
	
	@Override
	public void getPayAccount(BigInteger custID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(custID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,PaymentAccount.class));
	}
	
	@Override
	public void update(PaymentAccount paymentAccount) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(paymentAccount);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,PaymentAccount.class));
	}
	
	@Override
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
	
	@Override
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
