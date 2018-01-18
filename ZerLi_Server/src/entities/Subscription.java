package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

public class Subscription implements Serializable  {
	
	private static final long serialVersionUID = -7302493294431501041L;
	
	public enum SubscriptionType {
		Monthly, Yearly
	}
	
	private BigInteger subID;
	private SubscriptionType subType;
	private LocalDate subDate;

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

}