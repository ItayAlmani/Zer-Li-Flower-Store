package itayNron;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.Order;
import entities.ProductInOrder;
import entities.Stock;

public class StockController extends ParentController {
	
	public ArrayList<Object> getStockByStore(BigInteger storeID) throws SQLException {
		String query =  "SELECT sto.stockID,product.*,sto.quantity,sto.storeID" + 
				" FROM stock AS sto" + 
				" JOIN product ON sto.productID=product.productID" + 
				" WHERE sto.storeID='"+storeID+"'"
				;
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Order) {
			Order order = (Order)obj;
			ArrayList<Object> pios = EchoServer.fac.prodInOrder.getPIOsByOrder(order.getOrderID());
			for (Object object : pios) {
				if(object instanceof ProductInOrder == false) throw new Exception();
				ProductInOrder productInOrder = (ProductInOrder)object;
				String query = String.format(
						" UPDATE stock" + 
						" SET quantity=quantity - '%d'" + 
						" WHERE storeID="
						+ "("
							+ " SELECT storeID"
							+ " FROM deliverydetails "
							+ " WHERE orderID='%d' AND productID='%d'"
						+ ");",
						productInOrder.getQuantity(),
						order.getOrderID(),
						productInOrder.getProduct().getPrdID());
				EchoServer.fac.dataBase.db.updateQuery(query);
			}
		}
		myMsgArr.clear();
		myMsgArr.add(true);
		return myMsgArr;
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return null;
		ArrayList<Object> stocks = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 10) {
			stocks.add(parse(
					BigInteger.valueOf(Long.valueOf((int)obj.get(i))),
					BigInteger.valueOf(Long.valueOf((int)obj.get(i + 1))),
					(String) obj.get(i + 2), 
					(String) obj.get(i + 3),
					(float) obj.get(i + 4),
					(String) obj.get(i + 5),
					((int)obj.get(i + 6))!= 0,
					(String)obj.get(i+7),
					(int)obj.get(i + 8),
					BigInteger.valueOf(Long.valueOf((int)obj.get(i + 9)))
					));
		}
		return stocks;
	}
	
	public Stock parse(BigInteger stckID, 
			BigInteger prdID, String name, String type, float price, 
			String color, boolean inCatalog, String imageURL,
			int quantity, BigInteger storeID){
		return new Stock(stckID, 
				EchoServer.fac.product.parse(prdID, name, type, price, color, inCatalog, imageURL),
				quantity, storeID);
	}

	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
}