package entities;

import java.math.BigInteger;

public class PaymentAccount {

	private BigInteger paID;
	private BigInteger customerID;
	private float refundAmount = 0;
	private CreditCard creditCard;
	private Subscription sub;
	
	private static BigInteger idInc = null;
	
	/*public enum PaymentAccountType {
		Subscribed,
		NonSubscribed
	}*/

	public PaymentAccount(BigInteger paID) {
		super();
		this.paID = paID;
	}

	public float getRefundAmount() {
		return this.refundAmount;
	}

	public void setRefundAmount(float refundAmount) {
		this.refundAmount = refundAmount;
	}

	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Subscription getSub() {
		return this.sub;
	}

	public void setSub(Subscription sub) {
		this.sub = sub;
	}

	public BigInteger getPaID() {
		return paID;
	}

	public void setPaID(BigInteger paID) {
		this.paID = paID;
	}

	public BigInteger getCustomerID() {
		return customerID;
	}

	public void setCustomerID(BigInteger customerID) {
		this.customerID = customerID;
	}

	public static BigInteger getIdInc() {
		return idInc;
	}

	public static void setIdInc(BigInteger idInc) {
		PaymentAccount.idInc = idInc;
	}

}