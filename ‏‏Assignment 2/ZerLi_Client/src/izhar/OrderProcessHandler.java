package izhar;

import java.util.ArrayList;

import controllers.ParentController;
import entities.Order;
import izhar.OrderController;
import izhar.interfaces.IOrderProcess;

public class OrderProcessHandler extends ParentController implements IOrderProcess {

	public String updateAfterCancellation(Order order) {
		return null;
	}
	
	public void updateFinilizeOrder(Order order) {
	}

}