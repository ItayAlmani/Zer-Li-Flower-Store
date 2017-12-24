package izhar;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Transaction;
import entities.Transaction.PayMethod;
import entities.CSMessage.MessageType;
import entities.Order;
import izhar.interfaces.ITransaction;

public class TransactionController extends ParentController implements ITransaction {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTransaction(Transaction transaction) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(
				"INSERT INTO orders (paymentMethod, orderID)"
						+ "VALUES ('" + transaction.getPaymentMethod() + "', '" 
						+ transaction.getOrder().getOrderID() + "');"
								);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
		
	}

	@Override
	public void getTransactionByOrder(int orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM transaction WHERE orderID = '"+orderID+"';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Transaction.class));
		
	}

	@Override
	public void sendTransactions(ArrayList<Transaction> transactions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transaction parse(int tansID, String paymentMethod, int orderID) {
		return new Transaction(tansID, PayMethod.valueOf(paymentMethod), new Order(orderID));
		
	}
}