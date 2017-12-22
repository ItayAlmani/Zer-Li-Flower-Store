package entities;
public class Order {

	private string orderID;
	private Customer customer;
	private ShoppingCart cart;
	private DeliveryDetails delivery;
	private OrderType type;
	private Transaction transaction;
	private string greeting;
	private DeliveryType deliveryType;
	private OrderStatus orderStatus = OrderStatus.InProcess;

	public string getOrderID() {
		return this.orderID;
	}

	public Customer getCustomer() {
		return this.customer;
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

	public string getGreeting() {
		return this.greeting;
	}

	public void setGreeting(string greeting) {
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

	/**
	 * 
	 * @param customer
	 * @param cart
	 */
	public Order(Customer customer, ShoppingCart cart) {
		// TODO - implement Order.Order
		throw new UnsupportedOperationException();
	}

}