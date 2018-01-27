package usersInfo.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import usersInfo.entities.StoreWorker;
import usersInfo.entities.User;

public interface IStoreWorker {
	
	/**
	 * ask from DataBase to select the {@link StoreWorker} according to his {@link User}'s ID
	 * @param userID
	 * @throws IOException
	 */
	public void getStoreWorkerByUser(BigInteger userID) throws IOException;
	
	/**
	 * handle the information from the server, back to (GUI / asking controller)
	 * @param storeWorkers - {@link StoreWorker}s after parse
	 */
	public void handleGet(ArrayList<StoreWorker> storeWorkers);
	
	/**
	 * update exist {@link StoreWorker} in DataBase
	 * @param sw - {@link StoreWorker} to update
	 * @throws IOException
	 */
	public void update(StoreWorker sw) throws IOException;
	
	/**
	 * add new {@link StoreWorker} to DataBase
	 * @param sw - new {@link StoreWorker} to add
	 * @param getID - boolean to know if to return the new {@link StoreWorker}'s ID
	 * @throws IOException
	 */
	public void add(StoreWorker sw, boolean getID) throws IOException;

	/**
	 * delete {@link StoreWorker} from DataBase
	 * @param userID
	 * @throws Exception
	 */
	public void delete(BigInteger userID) throws Exception;
	
	/**
	 * handle the information from the server with the new {@link StoreWorker}'s ID returned
	 * @param id
	 */
	public void handleInsert(BigInteger id);


}
