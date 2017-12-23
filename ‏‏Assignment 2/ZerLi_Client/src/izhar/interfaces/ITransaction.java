package izhar.interfaces;


import entities.Transaction;
import enums.PayMethod;
import interfaces.IParent;

public interface ITransaction extends IParent {

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