package izhar;

import java.util.ArrayList;

import entities.Transaction;
import enums.PayMethod;
import izhar.interfaces.ITransaction;

public class TransactionController implements ITransaction {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getTransactionByOrder(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTransactions(ArrayList<Transaction> transactions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parse(int customerID, String paymentMethod, int orderID) {
		// TODO Auto-generated method stub
		
	}
}