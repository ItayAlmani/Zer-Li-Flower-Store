package lior;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.Order;
import entities.OrderReport;
import entities.Product;
import entities.Product.ProductType;
import entities.ProductInOrder;
import lior.interfaces.IOrderReportController;

public class OrderReportController extends ParentController implements IOrderReportController {
	private OrderReport[] oReports;
	private LocalDate rDate, startDate;
	int ind;

	@Override
	public void handleGet(ArrayList<Object> obj) {
	}
	
	public void initproduceOrderReport(LocalDate date, BigInteger storeID) throws ParseException{
		this.oReports = new OrderReport[2];
		produceOrderReport(date, storeID);
	}
	
	public void sendOrderReports(ArrayList<OrderReport> oReports) {
		String methodName = "setOrderReports";
		Method m = null;
		try {
			//a controller asked data, not GUI
			/*if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl, orders);
				Context.askingCtrl.remove(0);
			}
			else {*/
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, oReports);
				oReports=null;
			//}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}

	}

	@Override
	public void produceOrderReport(LocalDate date, BigInteger storeID) throws ParseException {
		int ind = 1;
		if(this.oReports[0]==null)
			ind = 0;
		if(this.oReports[0]!=null&&this.oReports[1]!=null)
			ind = 0;
		oReports[ind]=new OrderReport();
		this.oReports[ind].setStoreID(storeID);
		for (int i = 0; i < Product.ProductType.values().length; i++) {
			this.oReports[ind].addToCounterPerType(0);
			this.oReports[ind].addToSumPerType(0f);
		}
		rDate=date;
		//startDate=new LocalDate();
		Calendar c = Calendar.getInstance(); 
		c.setTime(Date.from(rDate.atStartOfDay(ZoneId.systemDefault()).toInstant())); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		//rDate.setHours(23);
		//rDate.setMinutes(59);
		//rDate.setSeconds(59);
		this.oReports[ind].setStartdate(this.startDate);
		this.oReports[ind].setEnddate(this.rDate);
		try {
			Context.askingCtrl.add(this);
			
			Context.fac.order.getAllOrdersByStoreID(storeID);
		} catch (IOException e) {
			System.err.println("OrderReportController\n");
			e.printStackTrace();
		}
	}

	public void setOrders(ArrayList<Order> orders) {
		int flag=0;
		int ind = 1;
		if(this.oReports[0].getOrders().size()==0)
			ind = 0;
		if(this.oReports[0].getOrders().size()!=0&&this.oReports[1].getOrders().size()!=0)
			ind = 0;
		//oReports[ind]=new OrderReport();
		this.oReports[ind].setOrders(orders);
		for(int i=0;i<orders.size();i++)
		{
			Date date = Date.from(orders.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(Date.from(this.oReports[ind].getEnddate().atStartOfDay(ZoneId.systemDefault()).toInstant()))==false&&
					date.after(Date.from(this.oReports[ind].getStartdate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
					/*&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)*/
					)
			{
				flag=1;
				Context.askingCtrl.add(this);
				try {
					Context.fac.prodInOrder.getPIOsByOrder(orders.get(i).getOrderID());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
		if(flag==0)
		{
			ArrayList<OrderReport> ar = new ArrayList<>();
			ar.add(this.oReports[ind]);
			sendOrderReports(ar);
		}
	}

	public void setPIOs(ArrayList<ProductInOrder> products) {
		int ind = 1;
		if(Context.fac.prodInOrder.isAllPIOsFromSameOrder(products)==false)
			return;
		for (Order order : this.oReports[0].getOrders()) {
			if(order.getOrderID().equals(products.get(0).getOrderID())) {
				ind=0;
				break;
			}
		}
		ArrayList<Order> orders = this.oReports[ind].getOrders();
		ArrayList<Integer> cntType = this.oReports[ind].getCounterPerType();
		ArrayList<Float> sumType = this.oReports[ind].getSumPerType();
		
		Order myOrder = null;
		for (Order ord : orders) {
			if(ord.getOrderID().equals(products.get(0).getOrderID())) {
				myOrder = ord;
				break;
			}
		}
		if(myOrder==null)
			return;
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
		ArrayList<OrderReport> ar = new ArrayList<>();
		this.oReports[ind].setCounterPerType(cntType);
		this.oReports[ind].setSumPerType(sumType);
		ar.add(this.oReports[ind]);
		sendOrderReports(ar);
	}

	@Override
	public void produceOrderReport(Date reqDate, BigInteger storeID) throws ParseException {
		// TODO Auto-generated method stub
		
	}
}