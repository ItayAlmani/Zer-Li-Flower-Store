package interfaces;

import java.util.ArrayList;

import entities.Item;
import enums.PaymentAccountType;

public interface IShoppingCart {

	boolean isCartEmpty(int cartID);

	void showCartEmptyErrMsg();

	ArrayList<Item> getItemsInCart(int cartID);

	void addItemToCart(Item item);

	void updateFinalPriceByPAT(PaymentAccountType pat);

	boolean updateTBItemsInCartInDB(Item item);
}