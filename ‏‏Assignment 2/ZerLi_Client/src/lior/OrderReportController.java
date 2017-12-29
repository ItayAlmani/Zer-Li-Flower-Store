package lior;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import common.Context;
import entities.Order;
import entities.OrderReport;
import entities.Product;
import entities.Product.ProductType;
import entities.ProductInOrder;
import enums.OrderStatus;
import enums.OrderType;
import izhar.OrderController;
import lior.interfaces.IOrderReportController;

public class OrderReportController implements IOrderReportController {
	private OrderReport oReport = new OrderReport();
	private Date rDate, startDate;

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
	public void produceOrderReport(Date reqDate, int storeID) throws ParseException {
		for (int i = 0; i < 4/*OrderType.values().length*/; i++) {
			oReport.addToCounterPerType(0);
			oReport.addToSumPerType(0f);
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
		this.oReport.setOrders(orders);
		rDate.setHours(23);
		rDate.setMinutes(59);
		rDate.setSeconds(59);
		this.oReport.setStartdate(this.startDate);
		this.oReport.setEnddate(this.rDate);
		for(int i=0;i<orders.size();i++)
		{
			if(orders.get(i).getDate().after(rDate)==false&&
					orders.get(i).getDate().after(startDate)
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
		ArrayList<Order> orders = oReport.getOrders();
		ArrayList<Integer> cntType = oReport.getCounterPerType();
		ArrayList<Float> sumType = oReport.getSumPerType();
		
		if(Context.fac.prodInOrder.isAllPIOsFromSameOrder(products)==false)
			return;
		
		Order myOrder = null;
		for (Order ord : orders) {
			if(ord.getOrderID()==products.get(0).getOrderID()) {
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
			int ind = -1;
			if(pt.equals(Product.ProductType.Bouquet))
				ind = 0;
			else if(pt.equals(Product.ProductType.Single))
				ind = 1;
			else if(pt.equals(Product.ProductType.Empty))
				ind = 2;
			cntType.set(ind, cntType.get(ind)+1);
			sumType.set(ind, sumType.get(ind)+products.get(j).getFinalPrice());
		}
		ArrayList<OrderReport> ar = new ArrayList<>();
		ar.add(oReport);
		sendOrderReports(ar);
	}
}