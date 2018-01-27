package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.CreditCard;
import entities.Customer;

public interface ICreditCard {
	
	/**
	 * add new {@link CreditCard} to the DataBase
	 * @param cc - new {@link CreditCard} to add
	 * @param getNextID - boolean to know if to return the new {@link CreditCard}'s ID
	 * @throws IOException
	 */
	public void add(CreditCard cc, boolean getNextID) throws IOException;
	
	/**
	 * handle the information from the server, back to (GUI / asking controller)
	 * </p>
	 * @param cards after parse
	 */
	public void handleGet(ArrayList<CreditCard> cards);
	
	/**
	 * ask from DataBase to select the {@link CreditCard} with the specific cardID given
	 * @param cardID - {@link CreditCard} ID
	 * @throws IOException
	 */
	public void getCreditCard(BigInteger cardID) throws IOException;
	
	/**
	 * ask from DataBase to select the {@link CreditCard} with the specific card number given
	 * @param ccNum - {@link CreditCard} number
	 * @throws IOException
	 */
	public void getCreditCardByNumber(String ccNum) throws IOException;
	
	/**
	 * handle the information from the server with the new {@link CreditCard}'s ID returned
	 * @param id
	 */
	public void handleInsert(BigInteger id);
	
	/**
	 * update {@link CreditCard} details in DataBase
	 * @param cc - {@link CreditCard} to update
	 * @throws IOException
	 */
	public void update(CreditCard cc) throws IOException;
	
	/**
	 * bill {@link CreditCard} according to the amount given (by random)
	 * @param cc
	 * @param amount
	 * @return success status
	 */
	boolean billCreditCardOfCustomer(CreditCard cc, float amount);

}
