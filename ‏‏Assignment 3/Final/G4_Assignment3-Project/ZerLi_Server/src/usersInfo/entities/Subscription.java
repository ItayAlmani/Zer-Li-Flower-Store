package usersInfo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

public class Subscription implements Serializable  {
	
	private static final long serialVersionUID = 20L;	
	private BigInteger subID;
	private SubscriptionType subType;
	private LocalDate subDate;

	public Subscription(BigInteger subID, SubscriptionType subType, LocalDate subDate) {
		this.subID = subID;
		this.subType = subType;
		this.subDate = subDate;
	}
	
	public Subscription(BigInteger subID, String subTypeStr, LocalDate subDate) {
		this.subID = subID;
		this.subType = SubscriptionType.valueOf(subTypeStr);
		this.subDate = subDate;
	}

	public Subscription(SubscriptionType subType) {
		this.subType = subType;
		this.subDate = LocalDate.now();
	}

	public Subscription(BigInteger subID) {
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
	
	public enum SubscriptionType {
		Monthly, Yearly
	}

}