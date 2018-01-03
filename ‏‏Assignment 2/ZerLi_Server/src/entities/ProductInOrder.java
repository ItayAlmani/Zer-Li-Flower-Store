package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class ProductInOrder implements Serializable  {
	private BigInteger id;
	private Product product;
	private int quantity;
	private BigInteger orderID;
	private float finalPrice;
	
	public ProductInOrder(Product product, int quantity, BigInteger orderID) {
		this.product = product;
		this.quantity = quantity;
		this.orderID = orderID;
		setFinalPrice();
	}

	public ProductInOrder(BigInteger id,Product product, BigInteger orderID, int quantity) {
		this.product = product;
		this.quantity = quantity;
		this.orderID=orderID;
		setFinalPrice();
	}
	
	public ProductInOrder(BigInteger id,Product product, BigInteger orderID, int quantity, float finalPrice) {
		this.product = product;
		this.quantity = quantity;
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
		this.finalPrice =  quantity*product.getPrice();
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
		this.quantity++;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
}
