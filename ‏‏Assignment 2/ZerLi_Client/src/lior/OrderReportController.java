package lior;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.Order;
import entities.OrderReport;
import entities.Product;
import izhar.OrderController;
import lior.interfaces.IOrderReportController;

public class OrderReportController implements IOrderReportController {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OrderReport ProduceOrderReport(Date Reqdate, int storeID) throws ParseException {
		OrderReport temp=new OrderReport();
		ArrayList<Order> orders=new ArrayList<>();
		ArrayList<Integer> counterPerType=new ArrayList<>();
		counterPerType.add(0);
		counterPerType.add(0);
		counterPerType.add(0);
		ArrayList<Float> sumPerType=new ArrayList<>();
		sumPerType.add(0f);
		sumPerType.add(0f);
		sumPerType.add(0f);
		int flag=0;
		int type1cnt=0,type2cnt=0,type3cnt=0,type4cnt=0;
		float type1sum=0,type2sum=0,type3sum=0,type4sum=0;
		Date startdate=new Date();
		SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat day = new SimpleDateFormat("dd");
		DateFormat Month = new SimpleDateFormat("MM");
		DateFormat Year = new SimpleDateFormat("yyyy");
		String str="",str1="";
		str=day.format(Reqdate)+"/";
		str1=Month.format(Reqdate);
		int month=(Integer.parseInt(str1));
		if(month<=3) flag=1;
		month=(month-3+12)%12;
		str1=Integer.toString(month);
		str=str1+"/";
		str1=Year.format(Reqdate);
		if(flag==1)
		{
			int year=Integer.parseInt(str1)-1;
			str1=Integer.toString(year);
			str+=str1;
		}
		else str+=Year.format(Reqdate);
		startdate=myFormat.parse(str);
		orders=OrderController.getAllOrdersByStoreID(storeID);
		for(int i=1;i<=orders.size();i++)
		{
			if(orders.get(i).getDate().getTime()<Reqdate.getTime()&&
					orders.get(i).getDate().getTime()>startdate.getTime())
			{
				ArrayList<Product> products=new ArrayList<>();
				products=orders.get(i).getAllProducts();
				for(int j=1;j<=products.size();j++)
				{
					if(products.get(j).getType().equals(Product.ProductType.Bouquet))
					{
						int num=counterPerType.get(1);
						num++;
						counterPerType.set(1, num);
						float num1=sumPerType.get(1);
						sumPerType.set(1, num1+products.get(j).getPrice());
					}
					else if(products.get(j).getType().equals(Product.ProductType.Single))
					{
						int num=counterPerType.get(2);
						num++;
						counterPerType.set(2, num);
						float num1=sumPerType.get(2);
						sumPerType.set(2, num1+products.get(j).getPrice());
					}
					else if(products.get(j).getType().equals(Product.ProductType.Empty))
					{
						int num=counterPerType.get(3);
						num++;
						counterPerType.set(3, num);
						float num1=sumPerType.get(3);
						sumPerType.set(3, num1+products.get(j).getPrice());
					}
				}
			}
		}
		temp.setCounterPerType(counterPerType);
		temp.setSumPerType(sumPerType);
		return null;
	}



}