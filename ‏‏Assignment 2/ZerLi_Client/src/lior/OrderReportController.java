package lior;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.Order;
import entities.Order.OrderStatus;
import entities.OrderReport;
import entities.Product;
import entities.Product.ProductType;
import entities.ProductInOrder;
import izhar.OrderController;
import lior.interfaces.IOrderReportController;

public class OrderReportController extends ParentController implements IOrderReportController {
	private OrderReport[] oReports = new OrderReport[2];
	private Date rDate, startDate;
	int flag=-1;

	@Override
	public void handleGet(ArrayList<Object> obj) {
		
		
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
	public void produceOrderReport(Date reqDate, BigInteger storeID) throws ParseException {
		int ind = 1;
		if(oReports[0]==null)
			ind = 0;
		oReports[ind]=new OrderReport();
		oReports[ind].setStoreID(storeID);
		for (int i = 0; i < 4/*OrderType.values().length*/; i++) {
			oReports[ind].addToCounterPerType(0);
			oReports[ind].addToSumPerType(0f);
		}
		rDate=reqDate;
		startDate=new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(reqDate); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime();
		try {
			Context.askingCtrl.add(this);
			
			Context.fac.order.getAllOrdersByStoreID(storeID);
		} catch (IOException e) {
			System.err.println("OrderReportController\n");
			e.printStackTrace();
		}
	}

	public void setOrders(ArrayList<Order> orders) {
		int ind = 1;
		if(oReports[0].getOrders().size()==0)
			ind = 0;
		this.oReports[ind].setOrders(orders);
		rDate.setHours(23);
		rDate.setMinutes(59);
		rDate.setSeconds(59);
		this.oReports[ind].setStartdate(this.startDate);
		this.oReports[ind].setEnddate(this.rDate);
		for(int i=0;i<orders.size();i++)
		{
			Date date = Date.from(orders.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(rDate)==false&&
					date.after(startDate)
					/*&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)*/
					)
			{
				Context.askingCtrl.add(this);
				try {
					Context.fac.prodInOrder.getPIOsByOrder(orders.get(i).getOrderID());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void setPIOs(ArrayList<ProductInOrder> products) {
		int ind = 1;
		if(flag==-1)
			flag=0;
		if(Context.fac.prodInOrder.isAllPIOsFromSameOrder(products)==false)
			return;
		for (Order order : oReports[0].getOrders()) {
			if(order.getOrderID().equals(products.get(0).getOrderID())) {
				ind=0;
				break;
			}
		}
		ArrayList<Order> orders = oReports[ind].getOrders();
		ArrayList<Integer> cntType = oReports[ind].getCounterPerType();
		ArrayList<Float> sumType = oReports[ind].getSumPerType();
		
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
				indx = 0;
			else if(pt.equals(Product.ProductType.Single))
				indx = 1;
			else if(pt.equals(Product.ProductType.Empty))
				indx = 2;
			cntType.set(indx, cntType.get(indx)+1);
			sumType.set(indx, sumType.get(indx)+products.get(j).getFinalPrice());
		}
		ArrayList<OrderReport> ar = new ArrayList<>();
		flag++;
		oReports[ind].setCounterPerType(cntType);
		oReports[ind].setSumPerType(sumType);
		ar.add(oReports[ind]);
		if(flag==oReports[ind].getOrders().size())
		{
			oReports[ind]=null;
			flag=-1;
		}
		sendOrderReports(ar);
	}
}