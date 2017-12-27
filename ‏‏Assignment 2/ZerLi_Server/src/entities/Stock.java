package entities;

public class Stock {
	private int id;
	private Product product;
	private int quantity;
	
	public Stock(int id, Product product, int quantity) {
		super();
		this.id=id;
		this.product = product;
		this.quantity = quantity;
	}

	public Stock(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
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
	
	
}
