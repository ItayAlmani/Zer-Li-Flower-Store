package usersInfo.interfaces;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import usersInfo.entities.Subscription;

public interface ISubscription {

	/**
	 * handle the information from the server send to the pars function, and than back to client
	 * @param obj - {@link ArrayList} of field from DB
	 * @throws Exception
	 */
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	
	/**
	 * add new {@link Subscription} to DataBase
	 * @param arr - {@link ArrayList} that contain the {@link Subscription} to add
	 * @throws Exception
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	
	/**
	 * update exist {@link Subscription} details in DataBase
	 * @param obj - {@link Subscription} to update
	 * @throws Exception
	 */
	public ArrayList<Object> update(Object obj) throws Exception;
	
	/**
	 *<p>
	 *create new {@link Subscription} object with data from DB
	 * <p>
	 * @param subID - parameter for subID field from DB
	 * @param type - parameter for type field from DB
	 * @param date - parameter for date field from DB
	 * @return {@link Subscription}
	 * @throws Exception
	 */
	public Subscription parse(BigInteger subID, String type, LocalDate date) throws Exception;
	
	/**
	 * ask from DataBase to select the {@link Subscription} with the given {@link Subscription} ID
	 * @param subID
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Object> getSubscriptionByID(BigInteger subID) throws Exception;
}
