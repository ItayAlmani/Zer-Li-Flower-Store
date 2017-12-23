package izhar.interfaces;

import java.util.ArrayList;

import entities.Product;
import enums.PaymentAccountType;
import interfaces.IParent;

public interface IShoppingCart extends IParent {

	boolean isCartEmpty(int cartID);

	void showCartEmptyErrMsg();

	void getProductsInCart(int cartID);
	
	void sendProductsInCart(ArrayList<Product> products);

	void addProductToCart(Product product);

	void updateFinalPriceByPAT(PaymentAccountType pat);

	void updateProductsInDB(ArrayList<Product> products);
}