package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.CreditCard;

public interface ICreditCard {
	
	/**
	 * add new {@link CreditCard} to the DataBase
	 * @param arr - {@link ArrayList} that contain the {@link CreditCard} to add
	 * @throws Exception
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	
	/**
	 *<p>
	 *create new {@link CreditCard} object with data from DB
	 * <p>
	 * @param id - parameter for CardId field from DB
	 * @param ccNumber -parameter for CardNumber field from DB
	 * @param ccValidity - parameter for CardValidity field from DB
	 * @param ccCVV - parameter for CardCvv field from DB
	 * @return {@link CreditCard}
	 */
	public CreditCard parse(BigInteger id,String ccNumber, String ccValidity, String ccCVV);

	/**
	 * handle the information from the server send to the pars function, and than back to client
	 * @param obj - {@link ArrayList} of field from DB
	 * @throws Exception
	 */
	public ArrayList<Object> handleGet(ArrayList<Object> obj);
	
	/**
	 * ask from DataBase to select the {@link CreditCard} with the specific cardID given
	 * @param cardID - {@link CreditCard} ID
	 * @throws IOException
	 */
	public ArrayList<Object> getCreditCard(BigInteger cardID)throws SQLException;
	
	/**
	 * ask from DataBase to select the {@link CreditCard} with the specific card number given
	 * @param ccNum - {@link CreditCard} number
	 * @throws IOException
	 */
	public ArrayList<Object> getCreditCardByNumber(String ccNum) throws SQLException;
	
	
	/**
	 * update exist {@link CreditCard} details in DataBase
	 * @param obj - {@link CreditCard} to update
	 * @throws IOException
	 */
	public ArrayList<Object> update(Object obj) throws Exception;

}
