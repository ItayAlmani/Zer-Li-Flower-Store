package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class Stock implements Serializable {
	
	private static final long serialVersionUID = 17L;
	private BigInteger id;
	private Product product;
	private int quantity;
	private BigInteger storeID;
	private Float salePercetage;
	
	public Stock(BigInteger id, Product product, int quantity, BigInteger storeID, Float salePercetage) {
		super();
		this.id=id;
		this.product = product;
		this.quantity = quantity;
		this.storeID = storeID;
		this.salePercetage=salePercetage;
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

	public Float getSalePercetage() {
		return salePercetage;
	}

	public void setSalePercetage(Float salePercetage) {
		this.salePercetage = salePercetage;
	}
	
	@Override
	public String toString() {
		return product.toString();
	}
}
