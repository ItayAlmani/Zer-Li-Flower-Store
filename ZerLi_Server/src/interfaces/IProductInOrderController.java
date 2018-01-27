package interfaces;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;

public interface IProductInOrderController {

	ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	/**
	 * Function that returns all products that relates in the required order
	 * @param orderID - the order we search the products the relates to him.
	 * @return ArrayList with all the data we pulled from the DB
	 * @throws Exception the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> getPIOsByOrder(BigInteger orderID) throws Exception;

	ArrayList<Object> update(Object obj) throws Exception;
	/**
	 * A function that updates the table row so that the price is adjusted to the ordered quantity.
	 * @param p - ProductInOrder instance that we wnt to update his row with the new data we got.
	 * @return ArrayList with all the data we pulled from the DB
	 * @throws SQLException the Exception can be if there is a problem with the query that sent to the DB.
	 */
	ArrayList<Object> updatePriceOfPIO(ProductInOrder p) throws SQLException;
	/**
	 * Function that check if all products is in the same order
	 * @param products ArrayList of ProductInOrder
	 * @return <b>true</b>- if ask products is in the same <b>false</b>-if ask products is not in the same
	 */
	boolean isAllPIOsFromSameOrder(ArrayList<ProductInOrder> products);
	/**
	 * <p> A function that get objects from the DB and calls {@link parse}.
	 *  after parse it pushes the data back as a report </p>
	 * @param obj - an array list of objects the represent row in ProductInOrdr table in DB.
	 * @return an ArrayList of Objects with all the information that related to the asking report
	 * @throws Exception This function uses {@link parse} that can throw an exception
	 */
	ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	/**
	 *  A function that gets all data as parameters, 
	 * and creates a ProductInOrder instance with all associated data.
	 * @param id - the id of the productinorder
	 * @param prod - the product
	 * @param orderID - the order id
	 * @param quantity - quantity of product
	 * @param finalPrice - final price of all of this products
	 * @return new ProductInOrder instance with all the required data.
	 */
	ProductInOrder parse(BigInteger id, Product prod, BigInteger orderID, int quantity, float finalPrice);

}