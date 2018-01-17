package kfir;

import java.time.LocalDate;

import entities.Subscription;
import entities.Subscription.SubscriptionType;

public class SubscriptionController {
	
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
}
