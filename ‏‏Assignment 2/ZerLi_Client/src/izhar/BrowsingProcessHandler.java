package izhar;

import java.util.ArrayList;

import entities.Product;
import izhar.interfaces.IBrowsingProcess;

public class BrowsingProcessHandler implements IBrowsingProcess{

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Product> getProductsInCart(int cartID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product getItemDetails(int productID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateFinalPrice() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addProductToCart(Product product) {
		// TODO Auto-generated method stub
		return false;
	}

}