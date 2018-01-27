package usersInfo;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import common.EchoServer;
import common.ParentController;
import usersInfo.entities.Subscription;
import usersInfo.interfaces.ISubscription;

public class SubscriptionController extends ParentController implements ISubscription {

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj == null) return new ArrayList<>();
		ArrayList<Object> subs = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3) {
			subs.add(parse(
					BigInteger.valueOf((Integer)obj.get(i)),
					(String)obj.get(i+1),
					((Timestamp)obj.get(i+2)).toLocalDateTime().toLocalDate()
					));
		}
		return subs;
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Subscription == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Subscription sub = (Subscription)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String query = String.format(
				"INSERT INTO subscription (type, date)"
				+ " VALUES ('%s', '%s')",
				sub.getSubType().name(),
				(Timestamp.valueOf(sub.getSubDate().atStartOfDay())).toString());
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		if(isReturnNextID) {
			query="SELECT Max(subscriptionID) from subscription";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer)
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Subscription) {
			Subscription sub = (Subscription)obj;
			String query=String.format(
					"UPDATE subscription"
					+ " SET type='%s', date='%s'"
					+ " WHERE subscriptionID='%d'",
					sub.getSubType().name(),
					(Timestamp.valueOf(sub.getSubDate().atStartOfDay())).toString(),
					sub.getSubID().intValue());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		throw new Exception();
	}
	
	@Override
	public Subscription parse(BigInteger subID, String type, LocalDate date) throws Exception {
		return new Subscription(subID,type,date);
	}
	
	@Override
	public ArrayList<Object> getSubscriptionByID(BigInteger subID) throws Exception {
		String query = "SELECT * FROM subscription WHERE subscriptionID='" + subID + "'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
}
