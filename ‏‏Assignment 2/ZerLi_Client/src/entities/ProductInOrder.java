package entities;

public class ProductInOrder extends Stock {
	
	private int orderID;
	private float finalPrice;

	public ProductInOrder(Product product, int orderID, int quantity) {
		super(product, quantity);
		this.orderID=orderID;
		setFinalPrice();
	}
	
	public ProductInOrder(Product product, int orderID, int quantity, float finalPrice) {
		super(product, quantity);
		this.finalPrice = finalPrice;
		this.orderID=orderID;
	}

	public float getFinalPrice() {
		return finalPrice;
	}
	
	/**
	 * set finalPrice by quantity and Product price
	 */
	public void setFinalPrice() {
		this.finalPrice =  getQuantity()*getProduct().getPrice();
	}

	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	
	
}
