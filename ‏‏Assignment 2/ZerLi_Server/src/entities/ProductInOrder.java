package entities;

public class ProductInOrder extends Stock {
	
	private float finalPrice;

	public ProductInOrder(Product product, int quantity, float finalPrice) {
		super(product, quantity);
		this.finalPrice = finalPrice;
	}

	public float getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	
}
