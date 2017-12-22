package interfaces;

import java.util.ArrayList;

import entities.Item;
import enums.PaymentAccountType;

public interface IShoppingCart {

	boolean isCartEmpty(String cartID);

	void showCartEmptyErrMsg();

	ArrayList<Item> getItemsInCart(String cartID);

	void addItemToCart(Item item);

	void updateFinalPriceByPAT(PaymentAccountType pat);

	boolean updateTBItemsInCartInDB(Item item);

}