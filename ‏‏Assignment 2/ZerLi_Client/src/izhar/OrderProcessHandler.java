package izhar;

import java.io.IOException;
import java.math.BigInteger;

import common.Context;
import common.Factory;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.ProductInOrder;
import entities.Order.DeliveryType;
import entities.ShipmentDetails;
import entities.Transaction;
import gui.controllers.ParentGUIController;
import izhar.gui.controllers.OrderGUIController;
import izhar.gui.controllers.PaymentGUIController;
import izhar.gui.controllers.UpdateOrderStatusGUIController;
import izhar.interfaces.IOrderProcess;

public class OrderProcessHandler extends ParentController implements IOrderProcess {

	private Order order;
	
	public String updateAfterCancellation(Order order) {
		return null;
	}
	
	public void updateFinilizeOrder(Order order) {
		Factory f = Context.fac;
		this.order=order;
		try {
			Context.askingCtrl.add(this);
			if(order.getDeliveryType().equals(DeliveryType.Pickup))
				f.pickup.addPickup(order.getDelivery());
			else if(order.getDeliveryType().equals(DeliveryType.Shipment))
				Context.fac.shipment.addShipment((ShipmentDetails) order.getDelivery());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setUpdateRespond(Integer id, Class<?> clasz) {
		Factory f = Context.fac;
		BigInteger biID = BigInteger.valueOf(id);
		try {
			if (clasz.equals(DeliveryDetails.class)) {
				order.getDelivery().setDeliveryID(biID);
				Context.askingCtrl.add(this);
				f.transaction.addTransaction(order.getTransaction());
			}
			else if (clasz.equals(ShipmentDetails.class)) {
				ShipmentDetails sd = (ShipmentDetails) order.getDelivery();
				sd.setShipmentID(biID);
				Context.askingCtrl.add(this);
				f.transaction.addTransaction(order.getTransaction());
			} else if (clasz.equals(Transaction.class)) {
				order.getTransaction().setTransID(biID);
				if (Context.existingOrder == false) {
					Context.askingCtrl.add(this);
					f.order.addOrder(order);
				}
				else {
					f.order.updateOrder(order);
					if(Context.currentGUI instanceof PaymentGUIController)
						((PaymentGUIController)Context.currentGUI).loadNextWindow();
				}
			} else if(clasz.equals(Order.class)) {
				order.setOrderID(biID);
				if(Context.currentGUI instanceof PaymentGUIController)
					((PaymentGUIController)Context.currentGUI).loadNextWindow();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}