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
		// TODO - implement Context.getUserAsCustomer
		throw new UnsupportedOperationException();
	}

	public StoreManager getUserAsStoreManager() {
		// TODO - implement Context.getUserAsStoreManager
		throw new UnsupportedOperationException();
	}

}