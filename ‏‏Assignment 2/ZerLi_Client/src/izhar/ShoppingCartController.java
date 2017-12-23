package izhar;

import java.util.ArrayList;

import entities.Item;
import enums.PaymentAccountType;
import izhar.interfaces.IShoppingCart;

public class ShoppingCartController implements IShoppingCart {

	public boolean isCartEmpty(String cartID) {
	}

	public void showCartEmptyErrMsg() {
	}

	public ArrayList<Item> getItemsInCart(String cartID) {
	}

	public void addItemToCart(Item item) {
	}

	public void updateFinalPriceByPAT(PaymentAccountType pat) {
	}

	public boolean updateTBItemsInCartInDB(Item item) {
	}
}