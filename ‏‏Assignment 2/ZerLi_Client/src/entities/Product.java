package entities;

public class Product{ 
	private int prdID;
	private String name;
	private ProductType type;
	
	public enum ProductType {
		Bouqute, Empty;
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
}
