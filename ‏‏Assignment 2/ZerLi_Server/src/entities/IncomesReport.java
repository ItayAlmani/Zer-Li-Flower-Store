package entities;

import java.util.ArrayList;
import java.util.Date;

public class IncomesReport extends QuarterlyReport {

	private ArrayList<Order> orders;
	private double TotIncomes;
	private Date Startdate=new Date();
	private Date Enddate=new Date();

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
	public double getTotIncomes() {
		return TotIncomes;
	}
	public void setTotIncomes(double totIncomes) {
		TotIncomes = totIncomes;
	}
	public ArrayList<Order> getOrders() {
		return orders;
	}
	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}
}