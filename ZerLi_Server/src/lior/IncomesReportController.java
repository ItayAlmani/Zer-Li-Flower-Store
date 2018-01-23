package lior;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import common.EchoServer;
import entities.IncomesReport;
import entities.Order;
import entities.OrderReport;
import entities.Order.OrderStatus;
import entities.Product.ProductType;
import lior.interfaces.IIncomesReportController;
import entities.ProductInOrder;

public class IncomesReportController implements IIncomesReportController {
	private IncomesReport iReport;
	
	public ArrayList<Object> getIncomesReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof BigInteger == false)
			return null;
		//=====CHANGE DATE TO THE END OF THE QUARTER======
		LocalDate date = (LocalDate)arr.get(0);
		BigInteger storeID = (BigInteger)arr.get(1);
		String query = String.format(
				"SELECT *"
				+ " FROM incomesreport "
				+ " WHERE storeID='%d'"
				+ " AND endOfQuarterDate='%s'",
					storeID.intValue(),
					Timestamp.valueOf(date.atStartOfDay()).toString());
		ArrayList<Object> inObjs = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(inObjs == null)	throw new Exception();
		if(inObjs.size()==1 && inObjs.get(0) instanceof IncomesReport)
			return inObjs;
		else if(inObjs.isEmpty())
			return ProduceIncomesReport(date, storeID);
		throw new Exception();
	}
	
	/* (non-Javadoc)
	 * @see lior.IIncomesReportController#ProduceIncomesReport(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> ProduceIncomesReport(LocalDate date, BigInteger storeID) throws Exception {
		iReport=new IncomesReport(date,storeID);
		this.iReport.setStoreID(storeID);
		this.iReport.setTotIncomes(0);
		return analyzeOrders(EchoServer.fac.order.getAllOrdersByStoreID(storeID));
	}
	
	public ArrayList<Object> analyzeOrders(ArrayList<Object> objs) throws Exception{
		ArrayList<Order> orders= new ArrayList<>();
		if(objs == null)
			throw new Exception();
		if(objs.isEmpty()) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.iReport);
			add(ar);
			return ar;
		}
		for (Object o : objs) {
			if(o instanceof Order)
				orders.add((Order)o);
		}
		for(int i=0;i<orders.size();i++){
			LocalDate start = iReport.getEndOfQuarterDate().minusMonths(3).plusDays(1), 
					end =iReport.getEndOfQuarterDate(),
					ordDate = orders.get(i).getDate().toLocalDate();
			if(ordDate.isAfter(end)==false && ordDate.isBefore(start)==false){
				if(orders.get(i).getOrderStatus().equals(OrderStatus.Paid) ||
						orders.get(i).getOrderStatus().equals(OrderStatus.Canceled))
				setPIOsInOrder(EchoServer.fac.prodInOrder.getPIOsByOrder(orders.get(i).getOrderID()));
			}	
		}
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.iReport);
			add(ar);
			return ar;
	}
	
	public void setPIOsInOrder(ArrayList<Object> objs) throws Exception{
		double Totalincomessum=iReport.getTotIncomes();
		for (Object pioObj : objs) {
			if(pioObj instanceof ProductInOrder) {
				ProductInOrder pio = (ProductInOrder)pioObj;
				Totalincomessum+=pio.getFinalPrice();
			}
		}
		this.iReport.setTotIncomes(Totalincomessum);
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj){
		if (obj == null || obj.size()<4)
			return new ArrayList<>();
		ArrayList<Object> irs = new ArrayList<>();
		BigInteger storeID = BigInteger.valueOf((Integer) obj.get(0));
		LocalDate endDate = ((Timestamp) obj.get(1)).toLocalDateTime().toLocalDate();
		IncomesReport ir = new IncomesReport(endDate,storeID);
		irs.add(ir);
		for (int i = 0; i < obj.size(); i += 5) {
			parse(	ir,
					(Integer) obj.get(i + 2),
					(Double) obj.get(i + 3)
				);
		}
		return irs;
	}

	private void parse(IncomesReport ir,Integer OrderID, Double totalPrice) {
		ir.setTotIncomes(ir.getTotIncomes()+totalPrice);
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr==null || arr.isEmpty() || arr.get(0) instanceof IncomesReport == false )
			throw new Exception();
		IncomesReport ir = (IncomesReport)arr.get(0);
		ArrayList<String> queries = new ArrayList<>();
		for (Order or : ir.getOrders()) {
			queries.add(String.format(
					"INSERT INTO incomesreport"
					+ " (storeID, endOfQuarterDate, orderID,totalPrice)"
					+ " VALUES ('%d', '%s', '%s', '%d', '%lf');",
					ir.getStoreID(),
					Timestamp.valueOf(ir.getEndOfQuarterDate().atStartOfDay()).toString(),
					or.getOrderID(),
					ir.getTotIncomes()));
		}
		EchoServer.fac.dataBase.db.insertWithBatch(queries);
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}
}
