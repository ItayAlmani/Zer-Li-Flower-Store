package usersInfo.entities;

import java.io.Serializable;
import java.math.BigInteger;

import orderNproducts.entities.Store;

public class PaymentAccount implements Serializable {

	private static final long serialVersionUID = 11L;
	private BigInteger paID;
	private BigInteger customerID;
	private float refundAmount = 0f;
	private CreditCard creditCard;
	private Subscription sub = null;
	private Store store;
	
	public PaymentAccount() {
		
	}
	
	public PaymentAccount(Store store) {
		super();
		this.store = store;
	}
	
	public PaymentAccount(BigInteger paID,BigInteger CustomerID, CreditCard creditCard, Subscription subscription,float refund) {
		this.paID = paID;
		this.customerID = CustomerID;
		this.creditCard=creditCard;
		this.sub=subscription;
		this.refundAmount = refund;
	}
	
	public PaymentAccount(BigInteger paID,BigInteger CustomerID,
			Store store, CreditCard creditCard, Subscription subscription,float refund) {
		this.paID = paID;
		this.customerID = CustomerID;
		this.creditCard=creditCard;
		this.store=store;
		this.sub=subscription;
		this.refundAmount = refund;
	}
	
	/*public enum PaymentAccountType {
		Subscribed,
		NonSubscribed
	}*/
	
	public void copyNewPayAcc(PaymentAccount p) {
		this.setPaID(p.paID);
		this.setCustomerID(p.customerID);
		this.setRefundAmount(p.getRefundAmount());
		this.setCreditCard(p.getCreditCard());
		this.setSub(p.getSub());
		this.setStore(p.getStore());
	}

	public PaymentAccount(BigInteger paID) {
		super();
		this.paID = paID;
	}

	public PaymentAccount(BigInteger customerID, Store store) {
		this.customerID=customerID;
		this.store=store;
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