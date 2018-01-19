package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order implements Serializable {

	private static final long serialVersionUID = 9L;	
	private BigInteger orderID;
	private BigInteger customerID;
	private DeliveryDetails delivery = null;
	private OrderType type;
	private String greeting = null;
	private DeliveryType deliveryType = null;
	private OrderStatus orderStatus;
	private LocalDateTime date = LocalDateTime.now();
	private float finalPrice = 0f;
	private ArrayList<ProductInOrder> products;
	private PayMethod paymentMethod;
	
	public Order() {
		finalPrice=0f;
		products = new ArrayList<>();
		date = LocalDateTime.now();
	}

	public Order(BigInteger orderID, BigInteger customerID, DeliveryDetails delivery, OrderType type,
			PayMethod paymentMethod, String greeting, DeliveryType deliveryType, OrderStatus orderStatus, LocalDateTime date,
			float finalPrice) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.delivery = delivery;
		this.type = type;
		this.paymentMethod = paymentMethod;
		this.greeting = greeting;
		this.deliveryType = deliveryType;
		this.orderStatus = orderStatus;
		this.date = date;
		this.finalPrice=finalPrice;
	}

	public BigInteger getOrderID() {
		return this.orderID;
	}

	public BigInteger getCustomerID() {
		return this.customerID;
	}

	public DeliveryDetails getDelivery() {
		return this.delivery;
	}

	public void setDelivery(DeliveryDetails delivery) {
		this.delivery = delivery;
	}

	public String getGreeting() {
		return this.greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public DeliveryType getDeliveryType() {
		return this.deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public OrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setOrderID(BigInteger orderID) {
		this.orderID = orderID;
	}

	public void setCustomerID(BigInteger customerID) {
		this.customerID = customerID;
	}
	
	public float getFinalPrice() {
		return finalPrice;
	}
	
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	private void setFinalPrice() {
		if(products==null) return;
		for (ProductInOrder productInOrder : products)
			finalPrice+=productInOrder.getFinalPrice();
	}
	
	public ArrayList<ProductInOrder> getProducts() {
		return products;
	}
	
	public void setProducts(ArrayList<ProductInOrder> products) {
		this.products = products;
		this.finalPrice=0f;
		setFinalPrice();
	}


	public PayMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PayMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	@Override
	public String toString()
	{
		return this.orderID.toString();
	}
	
	public enum OrderStatus {
		InProcess,
		Delivered,
		Canceled,
		Paid,
		WaitingForCashPayment
	}
	
	public enum OrderType {
		InfoSystem,
		Manual
	}
	
	public enum DeliveryType {
		Shipment,
		Pickup
	}
	
	public enum Refund {
		Full,
		Partial,
		No
	}
	
	public enum PayMethod {
		CreditCard,
		Cash,
		Refund,
		RefundAndCreditCard
	}
}