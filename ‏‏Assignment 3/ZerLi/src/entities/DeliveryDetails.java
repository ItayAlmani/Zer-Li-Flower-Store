package entities;
public class DeliveryDetails {

	private string deliveryID;
	private string orderID;
	protected DateTime date;
	private boolean isImmediate = true;
	private Store store;

	public void setOrderID(string orderID) {
		this.orderID = orderID;
	}

	public DateTime getDate() {
		return this.date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public void setIsImmediate(boolean isImmediate) {
		this.isImmediate = isImmediate;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}