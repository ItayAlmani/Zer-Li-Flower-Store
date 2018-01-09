package lior;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.EchoServer;
import entities.IncomesReport;
import entities.Order;
import entities.Order.OrderStatus;
import lior.interfaces.IIncomesReportController;
import entities.ProductInOrder;

public class IncomesReportController implements IIncomesReportController {
	private IncomesReport iReport;
	private LocalDate rDate, startDate;
	int ind;
	
	/* (non-Javadoc)
	 * @see lior.IIncomesReportController#ProduceIncomesReport(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> ProduceIncomesReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof BigInteger == false)
			throw new Exception();
		LocalDate date = (LocalDate)arr.get(0);
		BigInteger storeID = (BigInteger)arr.get(1);
		iReport=new IncomesReport();
		this.iReport.setStoreID(storeID);
		iReport.setTotIncomes(0);
		rDate=date;
		Calendar c = Calendar.getInstance(); 
		c.setTime(Date.from(rDate.atStartOfDay(ZoneId.systemDefault()).toInstant())); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.iReport.setStartdate(this.startDate);
		this.iReport.setEnddate(this.rDate);
		return analyzeOrders(EchoServer.fac.order.getAllOrdersByStoreID(storeID));
	}
	
	private ArrayList<Object> analyzeOrders(ArrayList<Object> objs) throws Exception{
		ArrayList<Order> orders= new ArrayList<>();
		if(objs == null || objs.isEmpty())
			throw new Exception();
		for (Object o : objs) {
			if(o instanceof Order)
				orders.add((Order)o);
		}
		int flag=0;
		this.iReport.setOrders(orders);
		for(int i=0;i<orders.size();i++)
		{
			Date date = Date.from(orders.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(Date.from(this.iReport.getEnddate().atStartOfDay(ZoneId.systemDefault()).toInstant()))==false&&
					date.after(Date.from(this.iReport.getStartdate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
					&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)
					)
			{
				flag=1;
				setPIOsInOrder(EchoServer.fac.prodInOrder.getPIOsByOrder(orders.get(i).getOrderID()));
			}	
		}
		if(flag==1) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.iReport);
			return ar;
		}
		else {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.iReport);
			return ar;
		}
	}
	
	private void setPIOsInOrder(ArrayList<Object> objs) throws Exception{
		ArrayList<ProductInOrder> products= new ArrayList<>();
		if(objs == null || objs.isEmpty())
			throw new Exception();
		for (Object o : objs) {
			if(o instanceof ProductInOrder)
				products.add((ProductInOrder)o);
		}
		if(EchoServer.fac.prodInOrder.isAllPIOsFromSameOrder(products)==false)
			throw new Exception();
		for (Order order : this.iReport.getOrders()) {
			if(order.getOrderID().equals(products.get(0).getOrderID())) {
				ind=0;
				break;
			}
		}
		ArrayList<Order> orders = this.iReport.getOrders();
		double Totalincomessum=iReport.getTotIncomes();
		
		Order myOrder = null;
		for (Order ord : orders) {
			if(ord.getOrderID().equals(products.get(0).getOrderID())) {
				myOrder = ord;
				break;
			}
		}
		if(myOrder==null)
			throw new Exception();
		myOrder.setProducts(products);
		
		for(int j=0;j<products.size();j++)
		{
			Totalincomessum+=products.get(j).getFinalPrice();
		}
		this.iReport.setTotIncomes(Totalincomessum);
	}
}
