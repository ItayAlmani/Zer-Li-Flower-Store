package kfir.interfaces;

import java.math.BigInteger;
import java.util.ArrayList;
import entities.StoreWorker;
import entities.User;

public interface IStoreWorker {
	
	/**
	 * ask from DataBase to select the {@link StoreWorker} with the given {@link User} ID
	 * @param userID
	 * @throws Exception
	 */
	public ArrayList<Object> getStoreWorkerByUser(BigInteger userID) throws Exception;
	
	/**
	 * ask from DataBase to select the {@link StoreWorker} with the given {@link StoreWorker} ID
	 * @param swID
	 * @throws Exception
	 */
	public ArrayList<Object> getStoreWorkerByID(BigInteger swID) throws Exception;
	
	/**
	 * handle the information from the server send to the pars function, and than back to client
	 * @param obj - {@link ArrayList} of field from DB
	 * @throws Exception
	 */
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	
	/**
	  *<p>
	 *create new {@link StoreWorker} object with data from DB
	 * <p>
	 * @param storeWorkerID - parameter for storeWorkerID field from DB
	 * @param userID - parameter for userID field from DB
	 * @param storeID - parameter for storeID field from DB
	 * @return {@link StoreWorker}
	 * @throws Exception
	 */
	public StoreWorker parse(BigInteger storeWorkerID, BigInteger userID, BigInteger storeID) throws Exception;
	
	/**
	 * add new {@link StoreWorker} to DataBase
	 * @param arr - {@link ArrayList} that contain the {@link StoreWorker} to add
	 * @throws Exception
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception;

	/**
	 * update exist {@link StoreWorker} details in DateBase
	 * @param obj - {@link StoreWorker} to update
	 * @throws Exception
	 */
	public ArrayList<Object> update(Object obj) throws Exception;
	
	/**
	 * delete {@link StoreWorker} from DateBase
	 * @param obj - {@link StoreWorker} to delete
	 * @throws Exception
	 */
	public ArrayList<Object> delete(ArrayList<Object> obj) throws Exception;
}
