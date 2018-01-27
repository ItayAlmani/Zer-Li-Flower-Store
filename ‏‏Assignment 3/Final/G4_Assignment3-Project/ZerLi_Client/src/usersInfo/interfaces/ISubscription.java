package usersInfo.interfaces;

import java.io.IOException;
import java.math.BigInteger;

import orderNproducts.entities.Order;
import usersInfo.entities.Customer;
import usersInfo.entities.Subscription;

public interface ISubscription {
	
	/**
	 * return {@link Customer}'s {@link Order} price according to his {@link Subscription}
	 * @param sub - {@link Customer}'s {@link Subscription}
	 * @param price - {@link Order} price
	 * @return the final price
	 */
	public Float getPriceBySubscription(Subscription sub, Float price);
	
	/**
	 * ask from DataBase to select the {@link Subscription} according to his ID
	 * @param subID
	 * @throws IOException
	 */
	public void getSubscriptionByID(BigInteger subID) throws IOException;
	
	/**
	 * update exist {@link Subscription} in DataBase
	 * @param sub - {@link Subscription} to update
	 * @throws IOException
	 */
	public void update(Subscription sub) throws IOException;
	
	/**
	 * add new {@link Subscription} to DataBase
	 * @param sub - new {@link Subscription} to add
	 * @param getID - boolean to know if to return the new {@link Subscription}'s ID
	 * @throws IOException
	 */
	public void add(Subscription sub, boolean getID) throws IOException;
	
	/**
	 * check if the given {@link Subscription} is valid
	 * @param sub - {@link Subscription} to check
	 * @return True/False according to {@link Subscription}'s Validation
	 */
	public boolean isSubValid(Subscription sub);
	
	/**
	 * handle the information from the server with the new {@link Subscription}'s ID returned
	 * @param id
	 */
	public void handleInsert(BigInteger id);

}
