package entities;

import java.util.Date;

public class Subscription {
	
	public enum SubscriptionType {
		Monthly, Yearly
	}
	
	private int subID;
	private int paID;
	private SubscriptionType subType;
	private Date subDate;

	/**
	 * 
	 * @param subType
	 */
	public Subscription(SubscriptionType subType) {
	}

}