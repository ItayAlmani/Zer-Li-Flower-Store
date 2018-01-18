package itayNron;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;

public class StockController extends ParentController {
	
	public ArrayList<Object> getStockByStore(BigInteger storeID) throws SQLException {
		String query =  "SELECT sto.stockID,product.*,sto.quantity,sto.storeID, salePercetage" + 
				" FROM stock AS sto" + 
				" JOIN product ON sto.productID=product.productID" + 
				" WHERE sto.storeID='"+storeID+"'"
				;
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Order)
			updateByOrder((Order)obj);
		else if(obj instanceof Stock)
			updateByStock((Stock)obj);
		else {
			System.err.println("Error in StockController#update()");
			throw new Exception();
		}
		myMsgArr.clear();
		myMsgArr.add(true);
		return myMsgArr;
	}
	
	private void updateByOrder(Order order) throws Exception {
		if(order.getProducts()!=null) {				
			for (ProductInOrder productInOrder : order.getProducts()) {
				int qu = productInOrder.getQuantity();
				String str;
				if(qu>0)
					str = " - '"+qu+"'";
				else
					str = " + '"+(-1)*qu+"'";
				String query = String.format(
						" UPDATE stock" + 
						" SET quantity=quantity %s" + 
						" WHERE storeID="
						+ "("
						+	"SELECT storeID" + 
						"	 FROM deliverydetails AS del," + 
						"	 (" + 
						"		SELECT deliveryID" + 
						"        FROM orders" + 
						"        WHERE orderID='94'" + 
						"	 ) AS ords" + 
						"	 WHERE del.deliveryID=ords.deliveryID AND stock.productID='6'"
						+ ");",
						str,
						order.getOrderID(),
						productInOrder.getProduct().getPrdID());
				EchoServer.fac.dataBase.db.updateQuery(query);
			}
		}
		throw new Exception();
	}
	
	private void updateByStock(Stock stk) throws Exception {
		String query = String.format(
				"UPDATE stock"
				+ " SET productID='%d',"
				+ " storeID='%d',"
				+ " quantity='%d',"
				+ " salePercetage='%f'"
				+ " WHERE stockID='%d'"
				,stk.getProduct().getPrdID().intValue(),
				stk.getStoreID().intValue(),
				stk.getQuantity(),
				stk.getSalePercetage().floatValue(),
				stk.getId().intValue());
		EchoServer.fac.dataBase.db.updateQuery(query);
		throw new Exception();
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return new ArrayList<>();
		ArrayList<Object> stocks = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 11) {
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
					BigInteger.valueOf(Long.valueOf((int)obj.get(i + 9))),
					(Float)obj.get(i+10)
					));
		}
		return stocks;
	}
	
	public Stock parse(BigInteger stckID, 
			BigInteger prdID, String name, String type, float price, 
			String color, boolean inCatalog, String imageURL,
			int quantity, BigInteger storeID, Float salePercetage){
		Product p = EchoServer.fac.product.parse(prdID, name, type, price, color, inCatalog, imageURL);
		return new Stock(stckID, p, quantity, storeID, salePercetage);
	}

	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && arr.size()==2 && (
				!(arr.get(0) instanceof Stock) ||
				!(arr.get(1) instanceof Boolean)))
			throw new Exception();
		
		Stock stk = (Stock)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		
		String query = String.format(
				"INSERT INTO stock (productID, storeID, quantity, salePercetage)"
				+ " VALUES ('%d', '%d', '%d', '%f')",
				stk.getProduct().getPrdID().intValue(),
				stk.getStoreID().intValue(),
				stk.getQuantity(),
				stk.getSalePercetage().floatValue());
		EchoServer.fac.dataBase.db.updateQuery(query);
		
		if(isReturnNextID) {
			query = "SELECT Max(stockID) from stock";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer) {
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			}
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}	
	
	public ArrayList<Object> addProductToAllStores(Product p) throws Exception {
		if(p==null)
			throw new Exception();
		ArrayList<Object> sObjs = EchoServer.fac.store.getAllStores();
		if(sObjs == null || sObjs.isEmpty())	return new ArrayList<>();
		for (Object o : sObjs) {
			if(o instanceof Store) {
				Stock s = new Stock(p, 0, ((Store)o).getStoreID());
				s.setSalePercetage(0f);
				myMsgArr.clear();
				myMsgArr.add(s);
				myMsgArr.add(false);
				add(myMsgArr);
			}
		}
		myMsgArr.clear();
		myMsgArr.add(true);
		return myMsgArr;
	}
}