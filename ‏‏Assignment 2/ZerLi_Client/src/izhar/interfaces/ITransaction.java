package izhar.interfaces;


import java.util.ArrayList;

import entities.Transaction;
import enums.PayMethod;
import interfaces.IParent;

public interface ITransaction extends IParent {
	/**
	 * 
	 * @param transaction
	 */
	void addTransaction(Transaction transaction);
	
	void getTransactionByOrder(int orderID);
	
	void sendTransactions(ArrayList<Transaction> transactions);
	
	void parse(int customerID, String paymentMethod, int orderID);
}