package interfaces;

import entities.PayMethod;
import entities.Transaction;

public interface ITransaction {

	/**
	 * 
	 * @param customerID
	 * @param paymentMethod
	 * @param orderID
	 */
	void createNewTransaction(String customerID, PayMethod paymentMethod, String orderID);

	/**
	 * 
	 * @param transaction
	 */
	void addTransactionToDB(Transaction transaction);

}