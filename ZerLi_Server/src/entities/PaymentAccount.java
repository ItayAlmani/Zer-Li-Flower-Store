package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class PaymentAccount implements Serializable {

	private static final long serialVersionUID = 11L;
	private BigInteger paID;
	private BigInteger customerID;
	private float refundAmount = 0f;
	private CreditCard creditCard;
	private Subscription sub = null;
	private Store store;
	
	public PaymentAccount(BigInteger paID,BigInteger CustomerID,
			Store store, CreditCard creditCard, Subscription subscription,float refund) {
		this.paID = paID;
		this.customerID = CustomerID;
		this.creditCard=creditCard;
		this.store=store;
		this.sub=subscription;
		this.refundAmount = refund;
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

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
}