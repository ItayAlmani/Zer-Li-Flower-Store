package kfir;

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
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import gui.controllers.ParentGUIController;

public class SubscriptionController extends ParentController {
	
	private static int discount_in_percent_of_month = 10;
	
	public Float getPriceBySubscription(Subscription sub, Float price) {
		LocalDate date = sub.getSubDate();
		SubscriptionType type = sub.getSubType();
		if(type.equals(SubscriptionType.Monthly)) {
			if(LocalDate.now().isBefore(date.plusMonths(1)) && date.isBefore(LocalDate.now()))
				return (1-discount_in_percent_of_month/100f)*price;
		}
		else if(type.equals(SubscriptionType.Yearly)) {
			if(LocalDate.now().isBefore(date.plusYears(1)) && date.isBefore(LocalDate.now()))
				return (1-(1.5f*discount_in_percent_of_month)/100f)*price;
		}
		return null;
	}
	
	public void getSubscriptionByID(BigInteger subID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(subID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Subscription.class));
	}
	
	public void update(Subscription sub) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(sub);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Subscription.class));
	}
	
	public void add(Subscription sub, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(sub);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr, Subscription.class));
	}
	
	public boolean isSubValid(Subscription sub) {
		
		if(sub == null ||
			sub.getSubDate() == null ||
			sub.getSubType() == null)
			throw new NullPointerException();
		LocalDate date = sub.getSubDate();
		if(sub.getSubType().equals(SubscriptionType.Monthly)) {
			if(LocalDate.now().isBefore(date.plusMonths(1)) && date.isBefore(LocalDate.now()))
				return false;
		}
		else if(sub.getSubType().equals(SubscriptionType.Yearly)) {
			if(LocalDate.now().isBefore(date.plusYears(1)) && date.isBefore(LocalDate.now()))
				return false;
		}
		return true;
	}
	
	public void handleInsert(BigInteger id) {
		String methodName = "setSubID";
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
