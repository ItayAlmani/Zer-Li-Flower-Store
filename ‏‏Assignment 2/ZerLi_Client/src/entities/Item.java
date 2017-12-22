package entities;

import enums.Color;
import enums.ItemType;

public class Item {

	private String itemID;
	private ItemType type;
	private float price;
	private Color dominantColor;
	private boolean inCatalog;
	
	private static Integer idCounter = 1;
	public Item() {
		this.itemID = idCounter.toString();
		idCounter++;
	}

	public ItemType getType() {
		return this.type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Color getDominantColor() {
		return this.dominantColor;
	}

	public void setDominantColor(Color dominantColor) {
		this.dominantColor = dominantColor;
	}

}