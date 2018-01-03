package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class Stock implements Serializable {
	private BigInteger id;
	private Product product;
	private int quantity;
	private BigInteger storeID;
	
	public Stock(BigInteger id, Product product, int quantity, BigInteger storeID) {
		super();
		this.id=id;
		this.product = product;
		this.quantity = quantity;
		this.storeID = storeID;
	}

	public Stock(Product product, int quantity, BigInteger storeID) {
		super();
		this.product = product;
		this.quantity = quantity;
		this.storeID = storeID;
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

	public BigInteger getStoreID() {
		return storeID;
	}

	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}
}
