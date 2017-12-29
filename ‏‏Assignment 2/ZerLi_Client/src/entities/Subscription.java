package entities;

import java.math.BigInteger;
import java.util.Date;

public class Subscription {
	
	public enum SubscriptionType {
		Monthly, Yearly
	}
	
	private BigInteger subID;
	private BigInteger paID;
	private SubscriptionType subType;
	private Date subDate;

	/**
	 * 
	 * @param subType
	 */
	public Subscription(SubscriptionType subType) {
	}

}