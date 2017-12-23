package entities;

import java.util.ArrayList;
import java.util.Date;

import enums.DeliveryType;
import enums.OrderStatus;
import enums.OrderType;

public class Order {

	private int orderID;
	private int customerID;
	private ShoppingCart cart;
	private DeliveryDetails delivery;
	private OrderType type;
	private Transaction transaction;
	private String greeting;
	private DeliveryType deliveryType;
	private OrderStatus orderStatus = OrderStatus.InProcess;
	private Date date;

	public int getOrderID() {
		return this.orderID;
	}

	public int getCustomerID() {
		return this.customerID;
	}

	public ShoppingCart getCart() {
		return this.cart;
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
	
	public ArrayList<Product> getAllItems(){
		return cart.getProducts();
	}
	

	/**
	 * 
	 * @param customer
	 * @param cart
	 */
	public Order(Customer customer, ShoppingCart cart) {
		
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


	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}

	public Order(int orderID, int customerID, ShoppingCart cart, DeliveryDetails delivery, OrderType type,
			Transaction transaction, String greeting, DeliveryType deliveryType, OrderStatus orderStatus, Date date) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.cart = cart;
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
	
	
}