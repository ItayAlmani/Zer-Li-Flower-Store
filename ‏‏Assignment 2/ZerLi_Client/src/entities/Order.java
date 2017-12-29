package entities;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Order {
	private BigInteger orderID;
	private BigInteger customerID;
	private DeliveryDetails delivery;
	private OrderType type;
	private Transaction transaction;
	private String greeting;
	private DeliveryType deliveryType;
	private OrderStatus orderStatus;
	private Date date;
	private float finalPrice;
	private ArrayList<ProductInOrder> products;
	
	private Order() {
		finalPrice=0;
		products = new ArrayList<>();
		date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public Order(BigInteger customerID, ArrayList<ProductInOrder> products) {
		super();
		this.customerID = customerID;
		this.type = OrderType.InfoSystem;
		this.orderStatus = OrderStatus.InProcess;
		this.products = products;
	}

	public Order(Integer customerID) {
		this();
		this.customerID=BigInteger.valueOf(customerID);
	}

	public Order(BigInteger orderID) {
		this();
		this.orderID = orderID;
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

	public Transaction getTransaction() {
		return this.transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setOrderID(BigInteger orderID) {
		this.orderID = orderID;
	}


	public Order(BigInteger orderID, BigInteger customerID, DeliveryDetails delivery, OrderType type,
			Transaction transaction, String greeting, DeliveryType deliveryType, OrderStatus orderStatus, Date date) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.delivery = delivery;
		this.type = type;
		this.transaction = transaction;
		this.greeting = greeting;
		this.deliveryType = deliveryType;
		this.orderStatus = orderStatus;
		this.date = date;
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
	public ArrayList<ProductInOrder> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<ProductInOrder> products) {
		this.products = products;
	}
	
	public void addToFinalPrice(float amount) {
		this.finalPrice+=amount;
	}
	
	public ProductInOrder containsProduct(Product p) {
		for (ProductInOrder productInOrder : products) {
			if(productInOrder.getProduct().getPrdID()==p.getPrdID())
				return productInOrder;
		}
		return null;
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
}