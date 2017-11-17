package actors;

import java.util.ArrayList;

public class Cart {
	private int cartID;
	private ArrayList<Item> cartItems;
	private float cartFinalPrice;
	
	public void addItemsToCart(Item item) {
		this.cartItems.add(item);	//Maybe we should check if already exist there
		this.cartFinalPrice+=item.getPrice();
	}
	
	public ArrayList<Item> viewCart(){
		return this.cartItems;
	}
}
