package orderNproducts.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Store;

public interface IProductInOrder {

	//------------------------------------------------IN CLIENT--------------------------------------------------------------------		
	float calcFinalPrice(ProductInOrder p);

	void updatePricesByStock(ArrayList<ProductInOrder> prds, Store store) throws Exception;

	ProductInOrder getPIOFromArr(ArrayList<ProductInOrder> prods, Product prod);

	/**
	 * Creating new {@link ArrayList} of all {@link ProductInOrder}s where <code>{@link ProductInOrder#getQuantity()}>0</code>
	 * @param prds the {@link ArrayList} of the {@link ProductInOrder}s
	 * @return new {@link ArrayList} of all {@link ProductInOrder}s where <code>{@link ProductInOrder#getQuantity()}>0</code>
	 */
	ArrayList<ProductInOrder> getPIOsNot0Quantity(ArrayList<ProductInOrder> prds);

	void handleGet(ArrayList<ProductInOrder> pios);

	void handleInsert(BigInteger id);

	//------------------------------------------------IN SERVER--------------------------------------------------------------------
	void getPIOsByOrder(BigInteger orderID) throws IOException;

	void update(ProductInOrder p) throws IOException;

	void add(ProductInOrder p, boolean getID) throws IOException;

	void updatePriceOfPIO(ProductInOrder p) throws IOException;
	//-------------------------------------------------------------------------------------------------------------------------

}