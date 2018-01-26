package kfir.interfaces;

import java.math.BigInteger;
import java.util.ArrayList;

import entities.Customer;
import entities.PaymentAccount;

public interface IPaymentAccount {
	
	/**
	 * add new {@link PaymentAccount} to the DataBase
	 * @param arr - {@link ArrayList} that contain the {@link PaymentAccount} to add
	 * @throws Exception
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;

	/**
	 * handle the information from the server send to the pars function, and than back to client
	 * @param obj - {@link ArrayList} of field from DB
	 * @throws Exception
	 */
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;

	/**
	  *<p>
	 *create new {@link PaymentAccount} object with data from DB
	 * <p>
	 * @param paID - parameter for AccountId field from DB
	 * @param CustomerID - parameter for CustomerId field from DB
	 * @param creditCardID - parameter for CardId field from DB
	 * @param storeID - parameter for StoerId field from DB
	 * @param subscriptionID - parameter for subscriptionID field from DB
	 * @param refund - parameter for refund field from DB
	 * @return {@link PaymentAccount}
	 * @throws Exception
	 */
	public PaymentAccount parse(BigInteger paID, BigInteger CustomerID, BigInteger creditCardID,
			BigInteger storeID, BigInteger subscriptionID, float refund) throws Exception;

	/**
	 * ask from DataBase to select the {@link PaymentAccount} of the given {@link Customer}
	 * @param custID
	 * @throws Exception
	 */
	public ArrayList<Object> getPayAccount(BigInteger custID) throws Exception;

	/**
	 * update exist {@link PaymentAccount} details in DataBase
	 * @param obj - {@link PaymentAccount} to update
	 * @throws Exception
	 */
	public ArrayList<Object> update(Object obj) throws Exception;
}
