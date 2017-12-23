package entities;

import enums.PayMethod;

public class Transaction {

	private int tansID;
	private PayMethod paymentMethod;
	private Order order;

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