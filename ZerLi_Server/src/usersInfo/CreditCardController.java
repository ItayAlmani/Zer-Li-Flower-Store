package usersInfo;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import common.ParentController;
import usersInfo.entities.CreditCard;
import usersInfo.interfaces.ICreditCard;

public class CreditCardController extends ParentController implements ICreditCard{
	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception{
		if(arr!=null && (arr.get(0) instanceof CreditCard == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		CreditCard cc = (CreditCard)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String query = "INSERT INTO creditcard (number, validity, cvv)"
				+ " VALUES ('" + cc.getCcNumber() + "'"
				+ ", '" + cc.getCcValidity() + "', '"
				+ cc.getCcCVV()+"')";
		EchoServer.fac.dataBase.db.updateQuery(query);
		if(isReturnNextID) {
			myMsgArr.clear();
			query ="SELECT creditCardID FROM creditcard WHERE number='"+cc.getCcNumber()+"'";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer) {
				BigInteger ccID= BigInteger.valueOf((Integer)myMsgArr.get(0));
				myMsgArr.set(0, ccID);
			}
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return new ArrayList<>();
		ArrayList<Object> cards = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 4) {
			cards.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)),
					(String) obj.get(i + 1),
					(String) obj.get(i + 2),
					(String)obj.get(i + 3)));
		}
		return cards;
	}

	@Override
	public CreditCard parse(BigInteger id,String ccNumber, String ccValidity, String ccCVV) {
		return new CreditCard(id,ccNumber, ccValidity, ccCVV);
	}

	
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof CreditCard) {
			CreditCard cc = (CreditCard)obj;
			String query=String.format(
					"UPDATE creditcard"
					+ " SET validity='%s', cvv='%s'"
					+ " WHERE creditCardID='%d' and number='%s'",
					cc.getCcValidity(), cc.getCcCVV(), cc.getCcID().intValue(), cc.getCcNumber());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		throw new Exception();
	}
	
	@Override
	public ArrayList<Object> getCreditCard(BigInteger cardID)throws SQLException {
		if(cardID==null)
			return null;
		String query ="SELECT *"
				+ " FROM creditcard"
				+ " WHERE creditCardID='"+cardID.toString()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	@Override
	public ArrayList<Object> getCreditCardByNumber(String ccNum) throws SQLException {
		if(ccNum==null)
			return null;
		String query ="SELECT *"
				+ " FROM creditcard"
				+ " WHERE number='"+ccNum+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
}