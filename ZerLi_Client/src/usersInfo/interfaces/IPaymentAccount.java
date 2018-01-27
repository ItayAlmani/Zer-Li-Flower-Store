package usersInfo.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import orderNproducts.entities.Store;
import usersInfo.entities.Customer;
import usersInfo.entities.PaymentAccount;

public interface IPaymentAccount {
	
	/**
	 * add new {@link PaymentAccount} to DataBase
	 * @param pa - new {@link PaymentAccount} to add
	 * @param getID - boolean to know if to return the new {@link PaymentAccount}'s ID
	 * @throws IOException
	 */
	public void add(PaymentAccount pa, boolean getID) throws IOException;

	/**
	 * handle the information from the server, back to (GUI / asking controller)
	 * @param pa - {@link PaymentAccount}s after parse
	 */
	public void handleGet(ArrayList<PaymentAccount> pa);

	/**
	 * ask from DataBase to select the {@link PaymentAccount} of the given {@link Customer}
	 * @param custID
	 * @throws IOException
	 */
	public void getPayAccount(BigInteger custID) throws IOException;
	
	/**
	 * update exist {@link PaymentAccount} in DataBase
	 * @param paymentAccount - {@link PaymentAccount} to update
	 * @throws IOException
	 */
	public void update(PaymentAccount paymentAccount) throws IOException;
	
	/**
	 * looks for {@link PaymentAccount} in the {@link ArrayList}, where the {@link PaymentAccount}
	 * related to the specific {@link Store} and return it. If not exist, returns null.
	 * @param pas
	 * @param s
	 * @return
	 * @throws Exception 
	 */
	public PaymentAccount getPaymentAccountOfStore(ArrayList<PaymentAccount> pas, Store s) throws Exception;
	
	/**
	 * handle the information from the server with the new {@link PaymentAccount}'s ID returned
	 * @param id
	 */
	public void handleInsert(BigInteger id);

}
