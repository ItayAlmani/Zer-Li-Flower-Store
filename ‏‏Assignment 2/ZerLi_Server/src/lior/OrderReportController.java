package lior;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.EchoServer;
import entities.Order;
import entities.Order.OrderStatus;
import entities.OrderReport;
import entities.Product;
import entities.Product.ProductType;
import entities.ProductInOrder;

public class OrderReportController {
	private OrderReport oReport;
	private LocalDate rDate, startDate;

	public ArrayList<Object> produceOrderReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof BigInteger == false)
			throw new Exception();
		LocalDate date = (LocalDate)arr.get(0);
		BigInteger storeID = (BigInteger)arr.get(1);
		oReport=new OrderReport();
		this.oReport.setStoreID(storeID);
		for (int i = 0; i < Product.ProductType.values().length; i++) {
			this.oReport.addToCounterPerType(0);
			this.oReport.addToSumPerType(0f);
		}
		rDate=date;
		Calendar c = Calendar.getInstance(); 
		c.setTime(Date.from(rDate.atStartOfDay(ZoneId.systemDefault()).toInstant())); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.oReport.setStartdate(this.startDate);
		this.oReport.setEnddate(this.rDate);
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
		this.oReport.setOrders(orders);
		for(int i=0;i<orders.size();i++)
		{
			Date date = Date.from(orders.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(Date.from(this.oReport.getEnddate().atStartOfDay(ZoneId.systemDefault()).toInstant()))==false&&
					date.after(Date.from(this.oReport.getStartdate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
					&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)
					)
			{
				flag=1;
				setPIOsInOrder(EchoServer.fac.prodInOrder.getPIOsByOrder(orders.get(i).getOrderID()));
			}	
		}
		if(flag==1) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.oReport);
			return ar;
		}
		else {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.oReport);
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
		ArrayList<Order> orders = this.oReport/*s[ind]*/.getOrders();
		ArrayList<Integer> cntType = this.oReport/*s[ind]*/.getCounterPerType();
		ArrayList<Float> sumType = this.oReport/*s[ind]*/.getSumPerType();
		
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
			ProductType pt = products.get(j).getProduct().getType();
			int indx = -1;
			if(pt.equals(Product.ProductType.Bouquet))
				indx = 4;
			else if(pt.equals(Product.ProductType.Single))
				indx = 5;
			else if(pt.equals(Product.ProductType.Empty))
				indx = 6;
			else if(pt.equals(Product.ProductType.FlowerArrangment))
				indx = 0;
			else if(pt.equals(Product.ProductType.FloweringPlant))
				indx = 1;
			else if(pt.equals(Product.ProductType.FlowersCluster))
				indx = 3;
			else if(pt.equals(Product.ProductType.BridalBouquet))
				indx = 2;
			cntType.set(indx, cntType.get(indx)+1);
			sumType.set(indx, sumType.get(indx)+products.get(j).getFinalPrice());
		}
		this.oReport/*s[ind]*/.setCounterPerType(cntType);
		this.oReport/*s[ind]*/.setSumPerType(sumType);
	}
}