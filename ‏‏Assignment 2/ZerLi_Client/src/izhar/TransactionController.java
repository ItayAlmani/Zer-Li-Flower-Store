package izhar;

import java.io.IOException;
import java.math.BigInteger;
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

	public void setLastAutoIncrenment(ArrayList<Object> obj) throws IOException {
		Transaction.setIdInc((BigInteger)obj.get(10));
	}
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTransaction(Transaction transaction) throws IOException {
		myMsgArr.clear();
		String query = "INSERT INTO transaction (paymentMethod, orderID)"
				+ "VALUES ('" + transaction.getPaymentMethod().toString() + "', '" 
				+ transaction.getOrder().getOrderID().toString() + "');";
		query += "SELECT Max(transactionID) from transaction;";
		myMsgArr.add(query);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,Transaction.class));
		
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
	public Transaction parse(BigInteger tansID, String paymentMethod, BigInteger orderID) {
		return new Transaction(tansID, PayMethod.valueOf(paymentMethod), new Order(orderID));
		
	}
}