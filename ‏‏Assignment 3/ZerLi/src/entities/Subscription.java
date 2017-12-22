package entities;

import java.util.Date;

import enums.SubscriptionType;

public class Subscription {

	private String subID;
	private SubscriptionType subType;
	private Date subDate;
	private String paID;
	
	private static Integer idCounter = 1;
	
	public Subscription() {
		this.subID = idCounter.toString();
		idCounter++;
	}

	/**
	 * 
	 * @param subType
	 */
	public Subscription(SubscriptionType subType) {
		this();
	}

}