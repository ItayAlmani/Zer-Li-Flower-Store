package entities;

import java.util.ArrayList;
import java.util.Date;

public class OrderReport extends QuarterlyReport {

	private ArrayList<Order> orders = new ArrayList<>();
	private ArrayList<Integer> counterPerType = new ArrayList<>();
	private ArrayList<Float> sumPerType = new ArrayList<>();
	private Date Startdate=new Date();
	private Date Enddate=new Date();

	public ArrayList<Order> getOrders() {
		return this.orders;
	}

	public void setCounterPerType(ArrayList<Integer> counterPerType) {
		this.counterPerType = counterPerType;
	}
	
	public void setSumPerType(ArrayList<Float> sumPerType) {
		this.sumPerType = sumPerType;
	}

	public ArrayList<Integer> getCounterPerType() {
		return counterPerType;
	}

	public ArrayList<Float> getSumPerType() {
		return sumPerType;
	}

	public void setOrders(ArrayList<Order> order) {
		this.orders = order;
	}
	
	public void addToOrders(Order order) {
		this.orders.add(order);
	}
	
	public void addToCounterPerType(Integer cnt) {
		this.counterPerType.add(cnt);
	}

	public void addToSumPerType(Float sum) {
		this.sumPerType.add(sum);
	}
	
	public Date getStartdate() {
		return Startdate;
	}

	public void setStartdate(Date startdate) {
		Startdate = startdate;
	}

	public Date getEnddate() {
		return Enddate;
	}

	public void setEnddate(Date enddate) {
		Enddate = enddate;
	}

}