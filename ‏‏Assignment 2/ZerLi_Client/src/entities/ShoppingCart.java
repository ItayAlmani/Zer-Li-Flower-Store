package entities;

import java.util.ArrayList;

public class ShoppingCart {

	private int cartID;
	private int orderID;
	private float finalPrice;
	private ArrayList<Product> products;
	private Customer customer;
	public int getCartID() {
		return cartID;
	}
	public void setCartID(int cartID) {
		this.cartID = cartID;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
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
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public ShoppingCart(int cartID) {
		super();
		this.cartID = cartID;
	}
	public ShoppingCart(int cartID, int orderID, float finalPrice, ArrayList<Product> products, Customer customer) {
		super();
		this.cartID = cartID;
		this.orderID = orderID;
		this.finalPrice = finalPrice;
		this.products = products;
		this.customer = customer;
	}
}