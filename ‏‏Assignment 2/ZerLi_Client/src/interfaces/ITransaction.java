package interfaces;

import java.util.ArrayList;

import entities.Transaction;
import enums.PayMethod;

public interface ITransaction {

	/**
	 * 
	 * @param customerID
	 * @param paymentMethod
	 * @param orderID
	 */
	void createNewTransaction(int customerID, PayMethod paymentMethod, int orderID);

	/**
	 * 
	 * @param transaction
	 */
	void addTransactionToDB(Transaction transaction);
}