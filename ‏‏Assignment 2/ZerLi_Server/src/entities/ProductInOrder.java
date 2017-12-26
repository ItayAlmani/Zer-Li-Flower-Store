package entities;

import java.math.BigInteger;

public class ProductInOrder extends Stock {
	
	private BigInteger orderID;
	private float finalPrice;

	public ProductInOrder(Product product, BigInteger orderID, int quantity) {
		super(product, quantity);
		this.orderID=orderID;
		setFinalPrice();
	}
	
	public ProductInOrder(Product product, BigInteger orderID, int quantity, float finalPrice) {
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

	public BigInteger getOrderID() {
		return orderID;
	}

	public void setOrderID(BigInteger orderID) {
		this.orderID = orderID;
	}
	
	public void addOneToQuantity() {
		setQuantity(getQuantity()+1);
	}
}
