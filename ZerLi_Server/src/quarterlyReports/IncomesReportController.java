package quarterlyReports;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import common.EchoServer;
import common.ParentController;
import orderNproducts.entities.Order;
import orderNproducts.entities.ProductInOrder;
import quarterlyReports.entities.IncomesReport;
import quarterlyReports.interfaces.IIncomesReportController;

public class IncomesReportController extends ParentController implements IIncomesReportController {
	private IncomesReport iReport;
	
	public ArrayList<Object> getIncomesReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof BigInteger == false)
			return null;
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
			return produceIncomesReport(date, storeID);
		throw new Exception();
	}
	
	@Override
	public ArrayList<Object> produceIncomesReport(LocalDate date, BigInteger storeID) throws Exception {
		iReport=new IncomesReport(date,storeID);
		this.iReport.setEnddate(date);
		this.iReport.setStartdate(date.minusMonths(3).plusDays(1));
		this.iReport.setStoreID(storeID);
		this.iReport.setTotIncomes(0f);
		return analyzeOrders(EchoServer.fac.order.getOrdersForReportByStoreID(storeID, date.minusMonths(3).plusDays(1),date ));
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
			if(o instanceof Order) {
				Order ord = (Order)o;
				orders.add(ord);
				this.iReport.setTotIncomes(iReport.getTotIncomes()+ord.getFinalPrice());
			}
		}
		ArrayList<Object> ar = new ArrayList<>();
		ar.add(this.iReport);
		add(ar);
		return ar;
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj){
		if (obj == null || obj.size()<3)
			return new ArrayList<>();
		ArrayList<Object> irs = new ArrayList<>();
		BigInteger storeID = BigInteger.valueOf((Integer) obj.get(0));
		LocalDate endDate = ((Timestamp) obj.get(1)).toLocalDateTime().toLocalDate();
		IncomesReport ir = new IncomesReport(endDate,storeID);
		ir.setEnddate(endDate);
		ir.setStartdate(endDate.minusMonths(3).plusDays(1));
		irs.add(ir);
		for (int i = 0; i < obj.size(); i += 3) {
			parse(	ir,
					(Double) obj.get(i + 2)
				);
		}
		return irs;
	}

	private void parse(IncomesReport ir,Double totalPrice) {
		ir.setTotIncomes(ir.getTotIncomes()+totalPrice);
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr==null || arr.isEmpty() || arr.get(0) instanceof IncomesReport == false )
			throw new Exception();
		IncomesReport ir = (IncomesReport)arr.get(0);
		ArrayList<String> queries = new ArrayList<>();
		queries.add(String.format(
				"INSERT INTO incomesreport"
				+ " (storeID, endOfQuarterDate,totalPrice)"
				+ " VALUES ('%d', '%s','%f');",
				ir.getStoreID(),
				Timestamp.valueOf(ir.getEndOfQuarterDate().atStartOfDay()).toString(),
				ir.getTotIncomes()));
		EchoServer.fac.dataBase.db.insertWithBatch(queries);
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}

}
