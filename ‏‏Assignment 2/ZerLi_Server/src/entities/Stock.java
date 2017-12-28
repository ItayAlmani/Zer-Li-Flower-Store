package entities;

public class Stock {
	private int id;
	private Product product;
	private int quantity;
	private int storeID;
	
	public Stock(int id, Product product, int quantity, int storeID) {
		super();
		this.id=id;
		this.product = product;
		this.quantity = quantity;
		this.storeID = storeID;
	}

	public Stock(Product product, int quantity, int storeID) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoreID() {
		return storeID;
	}

	public void setStoreID(int storeID) {
		this.storeID = storeID;
	}
}
