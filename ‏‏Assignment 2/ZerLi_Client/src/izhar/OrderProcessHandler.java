package izhar;

import java.io.IOException;

import common.Context;
import common.Factory;
import controllers.ParentController;
import entities.Order;
import entities.ProductInOrder;
import entities.Order.DeliveryType;
import entities.ShipmentDetails;
import izhar.interfaces.IOrderProcess;

public class OrderProcessHandler extends ParentController implements IOrderProcess {

	public String updateAfterCancellation(Order order) {
		return null;
	}
	
	public void updateFinilizeOrder(Order order) {
		Factory f = Context.fac;
		try {
			if(order.getDeliveryType().equals(DeliveryType.Pickup))
					f.pickup.addPickup(order.getDelivery());
			else if(order.getDeliveryType().equals(DeliveryType.Shipment))
				f.shipment.addShipment((ShipmentDetails) order.getDelivery());
			f.transaction.addTransaction(order.getTransaction());
			if (Context.existingOrder==false)
				f.order.addOrder(order);
			else
				f.order.updateOrder(order);
			Context.askOrder();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}