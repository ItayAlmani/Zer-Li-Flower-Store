package izhar;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;

import common.Context;
import common.Factory;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderType;
import entities.ProductInOrder;
import entities.ShipmentDetails;
import izhar.gui.controllers.PaymentGUIController;
import izhar.interfaces.IOrderProcess;

public class OrderProcessHandler extends ParentController implements IOrderProcess {
	
	public String updateAfterCancellation(Order order) {
		return null;
	}
	
	public void updateFinilizeOrder(Order order) throws IOException {
		Context.fac.order.addOrder(order);
	}
}