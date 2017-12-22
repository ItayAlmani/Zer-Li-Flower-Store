package entities;

/**
 * Class of static arguments.
 * Customer will be assigned after login
 */
public class Context {

	public User user;
	private ShoppingCart cart;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ShoppingCart getCart() {
		return this.cart;
	}

	public void setCart(ShoppingCart cart) {
		this.cart = cart;
	}

	public Customer getUserAsCustomer() {
		if(user instanceof Customer)
			return (Customer)user;
		return null;
	}

	public StoreManager getUserAsStoreManager() {
		if(user instanceof StoreManager)
			return (StoreManager)user;
		return null;
	}

}