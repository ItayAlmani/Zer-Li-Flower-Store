package entities;

import java.util.ArrayList;

public class OrderReport extends QuarterlyReport {

	private ArrayList<Order> order;
	private ArrayList<Integer> counterPerType;

	public ArrayList<Order> getOrder() {
		return this.order;
	}

	public void setCounterPerType(ArrayList<Integer> counterPerType) {
		this.counterPerType = counterPerType;
	}

}