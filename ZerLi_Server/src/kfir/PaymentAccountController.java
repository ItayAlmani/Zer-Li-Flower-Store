package kfir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.CSMessage;
import entities.Complaint;
import entities.CreditCard;
import entities.Order;
import entities.PaymentAccount;
import entities.ProductInOrder;
import entities.Store;
import entities.Subscription;
import entities.CSMessage.MessageType;

public class PaymentAccountController extends ParentController{

	/**
	 * 
	 * @param customerID
	 * @return 
	 * @throws Exception 
	 * @throws IOException
	 */
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof PaymentAccount == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		PaymentAccount pa = (PaymentAccount)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String subStr = pa.getSub()==null?"NULL":"'"+pa.getSub().getSubID().toString()+"'";
		String query = "INSERT INTO paymentaccount (customerID, creditcardID, subscriptionID, storeID, refundAmount)"
				+ " VALUES ('" 
				+ pa.getCustomerID() + "'" 
				+ ", '" + pa.getCreditCard().getCcID() 
				+ "', "+subStr
				+ ", '"+pa.getStore().getStoreID().toString()
				+ "', "+"'0');";
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		if(isReturnNextID) {
			query="SELECT Max(paymentAccountID) from paymentaccount";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer)
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}

	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception{
		if(obj == null) return new ArrayList<>();
		ArrayList<Object> acc = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 6) {
			BigInteger ccID = (Integer)obj.get(i + 2)==null?null:BigInteger.valueOf((Integer)obj.get(i + 2)),
					subID = (Integer)obj.get(i + 4)==null?null:BigInteger.valueOf((Integer)obj.get(i + 3));
			acc.add(parse(
					BigInteger.valueOf((Integer)obj.get(i)),
					BigInteger.valueOf((Integer)obj.get(i + 1)),
					ccID,
					BigInteger.valueOf((Integer)obj.get(i + 3)),
					subID, 
					(float) obj.get(i + 5))
					);
		}
		return acc;
	}

	public PaymentAccount parse(BigInteger paID, BigInteger CustomerID, BigInteger creditCardID,
			BigInteger storeID, BigInteger subscriptionID, float refund) throws Exception {
		ArrayList<Object> storesObj = EchoServer.fac.store.getStoreByID(storeID), 
				cardsObj = new ArrayList<>();
		if(creditCardID!=null)
			cardsObj = EchoServer.fac.creditCard.getCreditCard(creditCardID);
		else
			cardsObj.add(null);
		ArrayList<Object> subObjs = EchoServer.fac.sub.getSubscriptionByID(subscriptionID);
		Subscription sub = null;
		if(subObjs == null)
			throw new Exception();
		else if(subObjs.size()==1)
			sub = (Subscription)subObjs.get(0);
		if(storesObj.size()==1) {
			return new PaymentAccount(paID, CustomerID, 
					(Store)storesObj.get(0),
					(CreditCard)cardsObj.get(0), 
					sub,
					refund);
		}
		throw new Exception();
	}

	public ArrayList<Object> getPayAccount(BigInteger custID) throws Exception {
		String query = "SELECT * FROM paymentaccount WHERE customerID='" + custID + "'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof PaymentAccount) {
			PaymentAccount pa = (PaymentAccount)obj;
		String subStr = pa.getSub()==null || pa.getSub().getSubID()==null?"NULL":"'"+pa.getSub().getSubID().toString()+"'";
		String query=String.format("UPDATE paymentaccount SET customerID='%d',"
				+ " creditCardID='%d'"
				+ ", storeID='%d'"
				+ ", subscriptionID=%s"
				+ ", refundAmount='%f'"
				+ " WHERE paymentAccountID='%d'",
				pa.getCustomerID(),
				pa.getCreditCard().getCcID(),
				pa.getStore().getStoreID(),
				subStr,
				pa.getRefundAmount(),
				pa.getPaID());
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		myMsgArr.add(true);
		return myMsgArr;
		
				
		}
		else throw new Exception();
	}
	
	public ArrayList<Object> getPayAccontByCredCard(BigInteger ccID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
