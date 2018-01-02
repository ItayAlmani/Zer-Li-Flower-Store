package lior;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.Context;
import controllers.ParentController;
import entities.IncomesReport;
import entities.Order;
import entities.Order.OrderStatus;
import entities.OrderReport;
import entities.Product;
import entities.ProductInOrder;
import entities.Product.ProductType;
import lior.interfaces.IIncomesReportController;

public class IncomesReportController extends ParentController implements IIncomesReportController {

	private IncomesReport iReport = new IncomesReport();
	private Date rDate, startDate;
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub	
	}
	
	public void sendIncomeReports(ArrayList<IncomesReport> iReports) {
		String methodName = "setIncomeReports";
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
				m.invoke(Context.currentGUI, iReports);
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
	public IncomesReport ProduceIncomesReport(Date Reqdate, BigInteger storeID) throws ParseException {
		iReport.setTotIncomes(0);
		rDate=Reqdate;
		startDate=new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(Reqdate); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime();
		try {
			Context.askingCtrl.add(this);
			Context.fac.order.getAllOrdersByStoreID(storeID);
		} catch (IOException e) {
			System.err.println("IncomesReportController\n");
			e.printStackTrace();
		}
		return null;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.iReport.setOrders(orders);
		rDate.setHours(23);
		rDate.setMinutes(59);
		rDate.setSeconds(59);
		this.iReport.setStartdate(this.startDate);
		this.iReport.setEnddate(this.rDate);
		for(int i=0;i<orders.size();i++)
		{
			Date date = Date.from(orders.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(rDate)==false&&
					date.after(startDate)
					&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)
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
		ArrayList<Order> orders = iReport.getOrders();
		double Totalincomessum=iReport.getTotIncomes();
		iReport.setEnddate(rDate);
		iReport.setStartdate(startDate);
		if(Context.fac.prodInOrder.isAllPIOsFromSameOrder(products)==false)
			return;
		
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
			Totalincomessum+=products.get(j).getFinalPrice();
		}
		iReport.setTotIncomes(Totalincomessum);
		ArrayList<IncomesReport> ar = new ArrayList<>();
		ar.add(iReport);
		sendIncomeReports(ar);
	}
}