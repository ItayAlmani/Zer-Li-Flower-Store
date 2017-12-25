package entities;

import java.io.Serializable;

public class Transaction implements Serializable{

	private int tansID;
	private PayMethod paymentMethod;
	private Order order;
	
	public enum PayMethod {
		CreditCard,
		Cash,
		Refund,
		RefundAndCreditCard
	}

	public Transaction(int tansID) {
		super();
		this.tansID = tansID;
	}

	public Transaction(int tansID, PayMethod paymentMethod, Order order) {
		super();
		this.tansID = tansID;
		this.paymentMethod = paymentMethod;
		this.order = order;
	}



	public PayMethod getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPaymentMethod(PayMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getTansID() {
		return tansID;
	}

	public void setTansID(int tansID) {
		this.tansID = tansID;
	}
	
	
}