package entities;

import enums.PayMethod;

public class Transaction {

	private String tansID;
	private PayMethod paymentMethod;
	private Order order;
	
	private static Integer idCounter = 1;
	public Transaction() {
		this.tansID = idCounter.toString();
		idCounter++;
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

}