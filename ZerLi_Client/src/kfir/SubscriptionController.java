package kfir;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Subscription;
import entities.Subscription.SubscriptionType;

public class SubscriptionController extends ParentController {
	
	private static int discountInPercent = 10;
	
	public Float getPriceBySubscription(Subscription sub, Float price) {
		LocalDate date = sub.getSubDate();
		SubscriptionType type = sub.getSubType();
		if(type.equals(SubscriptionType.Monthly)) {
			if(date.plusMonths(1).isBefore(LocalDate.now()))
				return (1-discountInPercent/100f)*price;
		}
		else if(type.equals(SubscriptionType.Yearly)) {
			if(date.plusYears(1).isBefore(LocalDate.now()))
				return (1-(1.5f*discountInPercent)/100f)*price;
		}
		return price;
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
}
