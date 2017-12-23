package izhar;

import entities.Order;
import izhar.OrderController;
import izhar.interfaces.IOrderProcess;

public class OrderProcessHandler implements IOrderProcess {

	private OrderController orderController;

	public String updateAfterCancellation(Order order) {
	}
	
	public void updateFinilizeOrder(Order order) {
	}

}