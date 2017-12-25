package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import enums.DeliveryType;
import enums.OrderStatus;
import enums.OrderType;

public class Order implements Serializable {

	private int orderID;
	private int customerID;
	private DeliveryDetails delivery;
	private OrderType type;
	private Transaction transaction;
	private String greeting;
	private DeliveryType deliveryType;
	private OrderStatus orderStatus = OrderStatus.InProcess;
	private Date date;
	private float finalPrice;
	private ArrayList<Product> products;
	
	private Order() {
		finalPrice=0;
		products = new ArrayList<>();
	}
	
	public Order(Integer customerID) {
		this();
		this.customerID=customerID;
	}

	public Order(int orderID) {
		this();
		this.orderID = orderID;
	}

	public int getOrderID() {
		return this.orderID;
	}

	public int getCustomerID() {
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

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}


	public Order(int orderID, int customerID, DeliveryDetails delivery, OrderType type,
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

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	
	public float getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public void setItems(ArrayList<Product> products) {
		this.products = products;
	}
}