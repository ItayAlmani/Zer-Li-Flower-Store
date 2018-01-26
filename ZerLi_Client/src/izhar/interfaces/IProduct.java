package izhar.interfaces;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import entities.Customer;
import entities.Order;
import entities.Product;
import entities.Stock;
import entities.Subscription;
import entities.Product.Color;
import entities.Product.ProductType;
import gui.controllers.ParentGUIController;

public interface IProduct {

	//------------------------------------------------IN CLIENT--------------------------------------------------------------------
	/**
	 * A function that converts an image into an array of bytes
	 * @param imgFile- the image file
	 * @return An array of bytes that represents an image
	 * @throws Exception can be thrown if something went wrong with the parsing of the image
	 */
	byte[] insertImageToByteArr(File imgFile) throws Exception;

	/**
	 * Function that returns the unique price of a user with subscription
	 * @param order - represents the order
	 * @param p - represents the product
	 * @param price - the price
	 * @param customer - the customer
	 * @return null if the no change because the {@link Subscription}
	 * @throws Exception this function call to {@link getPaymentAccountOfStore} that can throw an exception
	 */
	Float getPriceWithSubscription(Order order, Product p, Float price, Customer customer) throws Exception;
	/**
	 * Function to assemble new order with not in catalog products
	 * @param type - the product type
	 * @param priceStart - the start of price range
	 * @param priceEnd - the end of the price range
	 * @param color - wanted color 
	 * @param stocks - how much stock
	 * @param sub - type of subscription
	 * @return ArratList of type Stock
	 */
	ArrayList<Stock> assembleProduct(ProductType type, Float priceStart, Float priceEnd, Color color,
			ArrayList<Stock> stocks, Subscription sub);

	//------------------------------------------------IN SERVER--------------------------------------------------------------------
	/**
	 * Function that get Product by ProductID
	 * @param prdID - The productId of the product we search for
	 * @throws IOException the Exception can be if there is a problem with the query that sent to the DB.
	 */
	void getProductByID(BigInteger prdID) throws IOException;

	void update(Product p) throws IOException;

	void handleGet(ArrayList<Product> prds);
	/**
	 * Get the {@link Product} id after doing {@link #add(Product, boolean)} when <code>getNextID = true</code>
	 * and sends it to the asker (by {@link ParentGUIController#currentGUI} or {@link Context#askingCtrl}
	 * @param id the id of the new row in the DataBase
	 */
	void handleInsert(BigInteger id);

	void add(Product p, boolean getID) throws IOException;
	/**
	 * Function that returns all products
	 * @throws IOException the Exception can be if there is a problem with the query that sent to the DB.
	 */
	void getAllProducts() throws IOException;
	/**
	 * Function that returns all products in catalog
	 * @throws IOException the Exception can be if there is a problem with the query that sent to the DB.
	 */
	void getProductsInCatalog() throws IOException;
	/**
	 * Function that returns all products not in catalog
	 * @throws IOException the Exception can be if there is a problem with the query that sent to the DB.
	 */
	void getAllProductsNotInCatalog() throws IOException;
	//-------------------------------------------------------------------------------------------------------------------------

}