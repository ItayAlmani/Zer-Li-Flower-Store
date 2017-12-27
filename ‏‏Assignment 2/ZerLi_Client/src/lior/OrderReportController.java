package lior;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import common.Context;
import entities.Order;
import entities.OrderReport;
import entities.Product;
import entities.Product.ProductType;
import entities.ProductInOrder;
import enums.OrderType;
import izhar.OrderController;
import lior.interfaces.IOrderReportController;

public class OrderReportController implements IOrderReportController {
	private OrderReport oReport = new OrderReport();
	private Date rDate, startDate;

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ProduceOrderReport(Date reqDate, int storeID) throws ParseException {
		for (int i = 0; i < OrderType.values().length; i++) {
			oReport.addToCounterPerType(0);
			oReport.addToSumPerType(0f);
		}
		int flag=0;
		int type1cnt=0,type2cnt=0,type3cnt=0,type4cnt=0;
		float type1sum=0,type2sum=0,type3sum=0,type4sum=0;
		startDate=new Date();
		SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat day = new SimpleDateFormat("dd");
		DateFormat Month = new SimpleDateFormat("MM");
		DateFormat Year = new SimpleDateFormat("yyyy");
		String str="",str1="";
		str=day.format(reqDate)+"/";
		str1=Month.format(reqDate);
		int month=(Integer.parseInt(str1));
		if(month<=3) flag=1;
		month=(month-3+12)%12;
		str1=Integer.toString(month);
		str=str1+"/";
		str1=Year.format(reqDate);
		if(flag==1)
		{
			int year=Integer.parseInt(str1)-1;
			str1=Integer.toString(year);
			str+=str1;
		}
		else str+=Year.format(reqDate);
		startDate=myFormat.parse(str);
		try {
			Context.askingCtrl.add(OrderReportController.class.newInstance());
			Context.fac.order.getAllOrdersByStoreID(storeID);
		} catch (InstantiationException | IllegalAccessException e1) {
			System.err.println("OrderReportController\n");
			e1.printStackTrace();
		} catch (IOException e) {
			System.err.println("OrderReportController\n");
			e.printStackTrace();
		}
	}

	public void setOrders(ArrayList<Order> orders) {
		this.oReport.setOrders(orders);
		for(int i=0;i<orders.size();i++)
		{
			if(orders.get(i).getDate().getTime()<rDate.getTime()&&
					orders.get(i).getDate().getTime()>startDate.getTime())
			{
				ArrayList<ProductInOrder> products=new ArrayList<>();
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
		
		
		Order myOrder = null;
		for (Order ord : orders) {
			if(ord.getOrderID()==products.get(j).getOrderID()) {
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
	}
}