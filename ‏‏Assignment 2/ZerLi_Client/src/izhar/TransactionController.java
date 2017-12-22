package izhar;

import entities.Transaction;
import enums.PayMethod;
import interfaces.ITransaction;

public class TransactionController implements ITransaction {

	public void createNewTransaction(String customerID, PayMethod paymentMethod, String orderID) {
	}

	public void addTransactionToDB(Transaction transaction) {
	}

}