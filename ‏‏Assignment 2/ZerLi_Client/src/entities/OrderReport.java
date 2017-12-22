package entities;
public class OrderReport extends QuarterlyReport {

	private Order[] order;
	private int[] counterPerType;

	public Order[] getOrder() {
		return this.order;
	}

	public void setCounterPerType(int[] counterPerType) {
		this.counterPerType = counterPerType;
	}

}