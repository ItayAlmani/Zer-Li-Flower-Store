package izhar;

import java.util.ArrayList;

import entities.Product;
import enums.PaymentAccountType;
import izhar.interfaces.IShoppingCart;

public class ShoppingCartController implements IShoppingCart {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCartEmpty(int cartID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showCartEmptyErrMsg() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getProductsInCart(int cartID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendProductsInCart(ArrayList<Product> products) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProductToCart(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFinalPriceByPAT(PaymentAccountType pat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProductsInDB(ArrayList<Product> products){
		// TODO Auto-generated method stub
		
	}
}