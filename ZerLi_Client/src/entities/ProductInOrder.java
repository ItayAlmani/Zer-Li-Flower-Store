package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class ProductInOrder extends RecursiveTreeObject<ProductInOrder> implements Comparable<Product>,Serializable  {
	
	private static final long serialVersionUID = 13L;
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
		this.id=id;
		this.product = product;
		this.quantity = quantity;
		this.finalPrice = finalPrice;
		this.orderID=orderID;
	}

	public ProductInOrder() {
		// TODO Auto-generated constructor stub
	}

	public float getFinalPrice() {
		return finalPrice;
	}
	
	/**
	 * set finalPrice by {@link #quantity} and the {@link #product}'s {@link Product#getPrice()}
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
	
	/** add 1 to quantity of the {@link ProductInOrder}, and updates the price
	 */
	public void addOneToQuantity() {
		this.quantity++;
		this.finalPrice+=this.getProduct().getPrice();
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
	
	public String getFinalPriceAsString() {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(getFinalPrice()) + "¤";
	}
	
	@Override
	public String toString() {
		return "Product: "+product.toString()+", Order: "+orderID;
	}

	@Override
	public int compareTo(Product o) {
		return this.getProduct().compareTo(o);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ProductInOrder)
			return this.product.compareTo(((ProductInOrder)obj).product)==0;
		else if(obj instanceof Product)
			return this.product.compareTo((Product)obj)==0;
		else return super.equals(obj);
	}
}
