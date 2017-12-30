package entities;

import java.math.BigInteger;

public class Transaction {

	private BigInteger transID;
	private PayMethod paymentMethod;
	private Order order;
	
	private static BigInteger idInc = null;
	
	public enum PayMethod {
		CreditCard,
		Cash,
		Refund,
		RefundAndCreditCard
	}

	public Transaction(BigInteger transID) {
		super();
		this.transID = transID;
	}

	public Transaction(PayMethod paymentMethod, Order order) {
		this.paymentMethod = paymentMethod;
		this.order = order;
	}
	
	public Transaction(BigInteger transID, PayMethod paymentMethod, Order order) {
		super();
		this.transID = transID;
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

	public BigInteger getTransID() {
		return transID;
	}

	public void setTansID(BigInteger transID) {
		this.transID = transID;
	}
	
	
}