package izhar.interfaces;


import java.io.IOException;
import java.util.ArrayList;

import entities.Transaction;
import interfaces.IParent;

public interface ITransaction extends IParent {
	/**
	 * 
	 * @param transaction
	 */
	void addTransaction(Transaction transaction) throws IOException;
	
	void getTransactionByOrder(int orderID) throws IOException;
	
	void sendTransactions(ArrayList<Transaction> transactions);
	
	Transaction parse(int tansID, String paymentMethod, int orderID);
}