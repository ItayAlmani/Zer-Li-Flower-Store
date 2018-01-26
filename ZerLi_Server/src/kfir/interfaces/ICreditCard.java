package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.CreditCard;

public interface ICreditCard {
	
	/**
	 * add new {@link CreditCard} to the DataBase
	 * @param cc - new {@link CreditCard} to add
	 * @param getNextID - boolean to know if to return the new {@link CreditCard}'s ID
	 * @throws IOException
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	
	public CreditCard parse(BigInteger id,String ccNumber, String ccValidity, String ccCVV);

	
	
	/**
	 * handle the information from the server, back to (GUI / asking controller)
	 * </p>
	 * @param cards after parse
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
	 * update {@link CreditCard} details in DataBase
	 * @param cc - {@link CreditCard} to update
	 * @throws IOException
	 */
	public ArrayList<Object> update(Object obj) throws Exception;

}
