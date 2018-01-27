package interfaces;

import java.math.BigInteger;
import java.util.ArrayList;

import orderNproducts.entities.Product;

public interface IProductController {
	/**
	 * A function that gets all data as parameters, 
	 * and creates a Product instance with all associated data.
	 * @param prdID - Product ID
	 * @param name - Product name
	 * @param type - Product type(4 types)
	 * @param price - Product price
	 * @param color - Product color
	 * @param inCatalog - if the product is a catalog item 
	 * @param imageURL - Product image URL
	 * @param byteArr - Product image parse into bytes
	 * @return new Product instance with all the required data.
	 * @throws Exception Can be thrown when updating if update fails
	 */
	Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog,
			String imageURL, byte[] byteArr) throws Exception;
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj - an array list of objects the represent row in product table in DB.
	 * @return an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	/**
	 * <p>A function that inserts a row into a product table in DB</p>
	 * @param arr - ArrayList of Object that represents the data that we want to insert as row in DB
	 * @return myMsgArr-An ArrayList of Objects that represnets the answer from DB
	 * @throws Exception the Exception can be throwed in 2 situations, if there is a problem with the query that sent to the DB.
	 * or if the returned values is not from the type we expected. 
	 */
	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	/**
	 * Function that get Product by ProductID
	 * @param prdID - The productId of the product we search for
	 * @return rrayList with the data we pulled from the DB
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> getProductByID(BigInteger prdID) throws Exception;
	/**
	 * Function that returns all products
	 * @return ArrayList with all the data we pulled from the DB
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> getAllProducts() throws Exception;
	/**
	 * Function that returns all products in catalog
	 * @return ArrayList with all the data we pulled from the DB
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> getProductsInCatalog() throws Exception;
	/**
	 * Function that returns all products not in catalog
	 * @return ArrayList with all the data we pulled from the DB
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> getAllProductsNotInCatalog() throws Exception;
	
	ArrayList<Object> update(Object obj) throws Exception;

}