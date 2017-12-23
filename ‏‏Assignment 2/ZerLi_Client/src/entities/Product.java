package entities;

import entities.Product.ProductType;

public class Product{ 
	private int prdID;
	private String name;
	private ProductType type;
	private float price;
	private Color color;
	private boolean inCatalog;
	
	public enum ProductType {
		Bouqute, Empty;
	}
	
	public enum Color {
		White, Yellow, Red, Gray, Purple, Blue, Green, Black, Orange, Brown;
	}
	
	public Product(int prdID, String name) {
		super();
		this.prdID = prdID;
		this.name = name;
	}
	
	public Product(int prdID, String name, ProductType type) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.type = type;
	}

	public Product(int prdID, String name, ProductType type, float price, Color color, boolean inCatalog) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.type = type;
		this.price = price;
		this.color = color;
		this.inCatalog = inCatalog;
	}
	
	public Product(int prdID, String name, String type) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.setType(type);
	}

	public Product(int prdID, String name, String type, float price, String color, boolean inCatalog) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.setType(type);
		this.price = price;
		this.setColor(color);
		this.inCatalog = inCatalog;
	}

	public int getPrdId() {
		return prdID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
	
	public void setType(String type) {
		try {
			this.setType(ProductType.valueOf(type));
		} catch (IllegalArgumentException e) {
			System.err.println("No enum constant of type as "+type);
		}
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setColor(String color) {
		try {
			this.setColor(Color.valueOf(color));
		} catch (IllegalArgumentException e) {
			System.err.println("No enum constant of color as "+color);
		}
	}
}
