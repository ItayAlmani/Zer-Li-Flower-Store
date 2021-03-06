package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

public class Subscription implements Serializable  {
	
	public enum SubscriptionType {
		Monthly, Yearly
	}
	
	private BigInteger subID;
	private SubscriptionType subType;
	private LocalDate subDate;
	
	private static int discountInPercent = 10; 

	/**
	 * 
	 * @param subType
	 */
	public Subscription(SubscriptionType subType) {
	}

	public Subscription(BigInteger subID) {
		super();
		this.subID = subID;
	}

	public BigInteger getSubID() {
		return subID;
	}

	public void setSubID(BigInteger subID) {
		this.subID = subID;
	}

	public SubscriptionType getSubType() {
		return subType;
	}

	public void setSubType(SubscriptionType subType) {
		this.subType = subType;
	}

	public LocalDate getSubDate() {
		return subDate;
	}

	public void setSubDate(LocalDate subDate) {
		this.subDate = subDate;
	}

	public static int getDiscountInPercent() {
		return discountInPercent;
	}

}