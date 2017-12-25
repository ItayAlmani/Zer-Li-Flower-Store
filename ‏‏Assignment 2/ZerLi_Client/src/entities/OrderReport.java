package entities;

import java.util.ArrayList;

public class OrderReport extends QuarterlyReport {

	private ArrayList<Order> order;
	private ArrayList<Integer> counterPerType;
	private ArrayList<Float> sumPerType;

	public ArrayList<Order> getOrder() {
		return this.order;
	}

	public void setCounterPerType(ArrayList<Integer> counterPerType) {
		this.counterPerType = counterPerType;
	}
	
	public void setSumPerType(ArrayList<Float> sumPerType) {
		this.sumPerType = sumPerType;
	}

}