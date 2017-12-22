package entities;

import java.util.ArrayList;

public class ShoppingCart {

	private String cartID;
	private String orderID;
	private float finalPrice;
	private ArrayList<Item> items;
	private Customer customer;
	
	private static Integer idCounter = 1;
	public ShoppingCart() {
		this.cartID = idCounter.toString();
		idCounter++;
	}
}