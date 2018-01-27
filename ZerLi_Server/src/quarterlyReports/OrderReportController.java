package quarterlyReports;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import common.EchoServer;
import common.ParentController;
import orderNproducts.entities.Order;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Order.OrderStatus;
import orderNproducts.entities.Product.ProductType;
import quarterlyReports.entities.OrderReport;
import quarterlyReports.interfaces.IOrderReportController;

public class OrderReportController extends ParentController implements IOrderReportController {
	private OrderReport oReport;

	public ArrayList<Object> getOrderReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof BigInteger == false){
			return null;
		}
		//=====CHANGE DATE TO THE END OF THE QUARTER======
		LocalDate date = (LocalDate)arr.get(0);
		BigInteger storeID = (BigInteger)arr.get(1);
		String query = String.format(
				"SELECT *"
				+ " FROM orderreport "
				+ " WHERE storeID='%d'"
				+ " AND endOfQuarterDate='%s'",
					storeID.intValue(),
					Timestamp.valueOf(date.atStartOfDay()).toString());
		ArrayList<Object> orObjs = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(orObjs == null)	throw new Exception();
		if(orObjs.size()==1 && orObjs.get(0) instanceof OrderReport)
			return orObjs;
		else if(orObjs.isEmpty())
			return produceOrderReport(date, storeID);
		throw new Exception();
	}
	
	@Override
	public ArrayList<Object> produceOrderReport(LocalDate date, BigInteger storeID) throws Exception {
		oReport=new OrderReport(date,storeID);
		this.oReport.setStoreID(storeID);
		for (ProductType pt : ProductType.values()) {
			this.oReport.addToCounterPerType(pt,0);
			this.oReport.addToSumPerType(pt,0f);
		}
		LocalDate start = oReport.getEndOfQuarterDate().minusMonths(3).plusDays(1), 
				end =oReport.getEndOfQuarterDate();
		return analyzeOrders(EchoServer.fac.order.getOrdersForReportByStoreID(storeID, start, end));
	}

	public ArrayList<Object> analyzeOrders(ArrayList<Object> objs) throws Exception{
		ArrayList<Order> orders= new ArrayList<>();
		if(objs == null)
			throw new Exception();
		if(objs.isEmpty()) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.oReport);
			add(ar);
			return ar;
		}
		for (Object o : objs) {
			if(o instanceof Order) {
				orders.add((Order)o);
				setPIOsInOrder(EchoServer.fac.prodInOrder.getPIOsByOrder(((Order)o).getOrderID()));
			}
		}
		ArrayList<Object> ar = new ArrayList<>();
		ar.add(this.oReport);
		add(ar);
		return ar;
	}

	public void setPIOsInOrder(ArrayList<Object> objs) throws Exception{
		HashMap<ProductType, Integer> cntType = this.oReport.getCounterPerType();
		HashMap<ProductType, Float> sumType = this.oReport.getSumPerType();
		
		for (Object pioObj : objs) {
			if(pioObj instanceof ProductInOrder) {
				ProductInOrder pio = (ProductInOrder)pioObj;
				ProductType pt = pio.getProduct().getType();
				cntType.put(pt, cntType.get(pt)+pio.getQuantity());
				sumType.put(pt, sumType.get(pt)+pio.getFinalPrice());
			}
		}
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj){
		if (obj == null || obj.size()<5)
			return new ArrayList<>();
		ArrayList<Object> ors = new ArrayList<>();
		BigInteger storeID = BigInteger.valueOf((Integer) obj.get(0));
		LocalDate endDate = ((Timestamp) obj.get(1)).toLocalDateTime().toLocalDate();
		OrderReport or = new OrderReport(endDate,storeID);
		ors.add(or);
		for (int i = 0; i < obj.size(); i += 5) {
			parse(	or,
					(String) obj.get(i + 2),
					(Integer) obj.get(i + 3),
					(Float) obj.get(i + 4)
				);
		}
		return ors;
	}

	private void parse(OrderReport or, String productType, Integer quantity, Float totalPrice) {
		ProductType pt = ProductType.valueOf(productType);
		or.addToCounterPerType(pt, quantity);
		or.addToSumPerType(pt, totalPrice);
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr==null || arr.isEmpty() || arr.get(0) instanceof OrderReport == false )
			throw new Exception();
		OrderReport or = (OrderReport)arr.get(0);
		ArrayList<String> queries = new ArrayList<>();
		for (ProductType pt : or.getCounterPerType().keySet()) {
			queries.add(String.format(
					"INSERT INTO orderreport"
					+ " (storeID, endOfQuarterDate, productType, quantity, totalPrice)"
					+ " VALUES ('%d', '%s', '%s', '%d', '%f');",
					or.getStoreID(),
					Timestamp.valueOf(or.getEndOfQuarterDate().atStartOfDay()).toString(),
					pt.name(),
					or.getCounterPerType().get(pt),
					or.getSumPerType().get(pt)));
		}
		EchoServer.fac.dataBase.db.insertWithBatch(queries);
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}

}