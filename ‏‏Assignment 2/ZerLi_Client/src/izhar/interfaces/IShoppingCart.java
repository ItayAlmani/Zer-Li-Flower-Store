package izhar.interfaces;

import java.util.ArrayList;

import entities.Product;
import enums.PaymentAccountType;
import interfaces.IParent;

public interface IShoppingCart extends IParent {

	boolean isCartEmpty(int cartID);

	void showCartEmptyErrMsg();

	ArrayList<Product> getProductsInCart(int cartID);

	void addProductToCart(Product product);

	void updateFinalPriceByPAT(PaymentAccountType pat);

	boolean updateTBProductsInCartInDB(Product product);
}