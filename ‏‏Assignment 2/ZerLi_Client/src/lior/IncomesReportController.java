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
import entities.ProductInOrder;
import lior.interfaces.IIncomesReportController;

public class IncomesReportController extends ParentController implements IIncomesReportController {

	private IncomesReport[] iReport;
	private Date rDate, startDate;
	int ind;
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub	
	}
	
	public void initProduceIncomesReport(Date Reqdate, BigInteger storeID) throws ParseException
	{
		this.iReport = new IncomesReport[2];
		ProduceIncomesReport(Reqdate, storeID);
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
	public void ProduceIncomesReport(Date Reqdate, BigInteger storeID) throws ParseException {
		int ind = 1;
		if(this.iReport[0]==null)
			ind = 0;
		if(this.iReport[0]!=null&&this.iReport[1]!=null)
			ind = 0;
		iReport[ind]=new IncomesReport();
		this.iReport[ind].setStoreID(storeID);
		iReport[ind].setTotIncomes(0);
		iReport[ind].setStoreID(storeID);
		rDate=Reqdate;
		startDate=new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(Reqdate); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime();
		rDate.setHours(23);
		rDate.setMinutes(59);
		rDate.setSeconds(59);
		this.iReport[ind].setStartdate(this.startDate);
		this.iReport[ind].setEnddate(this.rDate);
		try {
			Context.askingCtrl.add(this);
			Context.fac.order.getAllOrdersByStoreID(storeID);
		} catch (IOException e) {
			System.err.println("IncomesReportController\n");
			e.printStackTrace();
		}
	}

	public void setOrders(ArrayList<Order> orders) {
		int flag=0;
		int ind = 1;
		if(iReport[0].getOrders()==null || this.iReport[0].getOrders().size()==0)
			ind = 0;
		if(iReport[0].getOrders()!=null && 
				this.iReport[0].getOrders().size()!=0&&this.iReport[1].getOrders().size()!=0)
			ind = 0;
		this.iReport[ind].setOrders(orders);
		for(int i=0;i<orders.size();i++)
		{
			Date date = Date.from(orders.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(iReport[ind].getEnddate())==false&&
					date.after(iReport[ind].getStartdate())
					//&& orders.get(i).getOrderStatus().equals(OrderStatus.Paid)
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
		if(flag==0)
		{
			ArrayList<IncomesReport> ar = new ArrayList<>();
			ar.add(iReport[ind]);
			sendIncomeReports(ar);
		}
	}

	public void setPIOs(ArrayList<ProductInOrder> products) {
		int ind = 1;
		if(Context.fac.prodInOrder.isAllPIOsFromSameOrder(products)==false)
			return;
		for (Order order : this.iReport[0].getOrders()) {
			if(order.getOrderID().equals(products.get(0).getOrderID())) {
				ind=0;
				break;
			}
		}
		ArrayList<Order> orders = iReport[ind].getOrders();
		double Totalincomessum=iReport[ind].getTotIncomes();
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
		iReport[ind].setTotIncomes(Totalincomessum);
		ArrayList<IncomesReport> ar = new ArrayList<>();
		ar.add(iReport[ind]);
		sendIncomeReports(ar);
	}
}