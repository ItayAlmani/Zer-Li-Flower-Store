package entities;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import orderNproducts.entities.Order;

public class IncomesReport extends QuarterlyReport {

	private static final long serialVersionUID = 8L;
	private ArrayList<Order> orders=new ArrayList<>();
	private double TotIncomes=-1;
	private LocalDate Startdate;
	private LocalDate Enddate;
	
	public IncomesReport(LocalDate endOfQuarterDate, BigInteger storeID) {
		super(endOfQuarterDate, storeID);
	}

	public LocalDate getStartdate() {
		return Startdate;
	}
	public void setStartdate(LocalDate startdate) {
		Startdate = startdate;
	}
	public LocalDate getEnddate() {
		return Enddate;
	}
	public void setEnddate(LocalDate enddate) {
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