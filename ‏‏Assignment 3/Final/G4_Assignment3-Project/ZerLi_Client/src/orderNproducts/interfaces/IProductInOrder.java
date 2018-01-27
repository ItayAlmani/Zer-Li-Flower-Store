package orderNproducts.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import common.gui.controllers.ParentGUIController;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Store;

public interface IProductInOrder {

	//------------------------------------------------IN CLIENT--------------------------------------------------------------------	
	/**
	 * Function that calculates the final price
	 * @param p - p is ProductInOrder representation
	 * @return the value of the final price
	 */
	float calcFinalPrice(ProductInOrder p);
	/**
	 * Function that updates a product price to all stock if its in sale.
	 * @param prds arraylist of ProductInOrder
	 * @param store The store that this products in sale
	 * @throws Exception {@link getPriceWithSubscription} can throw an exception
	 */
	void updatePricesByStock(ArrayList<ProductInOrder> prds, Store store) throws Exception;
	/**
	 * Function that found particular PIO in arr
	 * @param prods - arraylist of all products
	 * @param prod - the product we want to find
	 * @return ProductInOrder that represnt the product we searched for
	 */
	ProductInOrder getPIOFromArr(ArrayList<ProductInOrder> prods, Product prod);

	/**
	 * Creating new {@link ArrayList} of all {@link ProductInOrder}s where <code>{@link ProductInOrder#getQuantity()}>0</code>
	 * @param prds the {@link ArrayList} of the {@link ProductInOrder}s
	 * @return new {@link ArrayList} of all {@link ProductInOrder}s where <code>{@link ProductInOrder#getQuantity()}>0</code>
	 */
	ArrayList<ProductInOrder> getPIOsNot0Quantity(ArrayList<ProductInOrder> prds);

	void handleGet(ArrayList<ProductInOrder> pios);
	/**
	 * Get the {@link Product} id after doing {@link #add(Product, boolean)} when <code>getNextID = true</code>
	 * and sends it to the asker (by {@link ParentGUIController#currentGUI} or {@link Context#askingCtrl}
	 * @param id the id of the new row in the DataBase
	 */
	void handleInsert(BigInteger id);

	//------------------------------------------------IN SERVER--------------------------------------------------------------------
	/**
	 * get all ProductInOrder that in specific order
	 * @param orderID - the order we want to find his PIO
	 * @throws IOException - the transfer to the server can throw an IOException
	 */
	void getPIOsByOrder(BigInteger orderID) throws IOException;
	
	void update(ProductInOrder p) throws IOException;

	void add(ProductInOrder p, boolean getID) throws IOException;
	/**
	 * Function that send a request to the server for update in price of specific PIO in DB
	 * @param p - the PIO we want to update
	 * @throws IOException - the transfer to the server can throw an IOException
	 */
	void updatePriceOfPIO(ProductInOrder p) throws IOException;
	//-------------------------------------------------------------------------------------------------------------------------

}