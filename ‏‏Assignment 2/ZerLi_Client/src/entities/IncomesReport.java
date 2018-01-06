package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class IncomesReport extends QuarterlyReport {

	private ArrayList<Order> orders=new ArrayList<>();
	private double TotIncomes=-1;
	private LocalDate Startdate;
	private LocalDate Enddate;

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