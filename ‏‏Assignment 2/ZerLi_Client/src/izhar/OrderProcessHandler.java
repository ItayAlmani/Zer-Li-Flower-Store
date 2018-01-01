package izhar;

import java.io.IOException;
import java.math.BigInteger;

import common.Context;
import common.Factory;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.ShipmentDetails;
import izhar.gui.controllers.PaymentGUIController;
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
			if(order.getDeliveryType() != null) {
				Context.askingCtrl.add(this);
				if(order.getDeliveryType().equals(DeliveryType.Pickup))
					f.pickup.addPickup(order.getDelivery());
				else if(order.getDeliveryType().equals(DeliveryType.Shipment))
					f.shipment.addShipment((ShipmentDetails) order.getDelivery());
			}
			else {
				if (Context.existingOrder == false) {
					Context.askingCtrl.add(this);
					f.order.addOrder(order);
				}
				else {
					f.order.updateOrder(order);
					if(Context.currentGUI instanceof PaymentGUIController)
						((PaymentGUIController)Context.currentGUI).loadNextWindow();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setUpdateRespond(Integer id, Class<?> clasz) {
		Factory f = Context.fac;
		BigInteger biID = BigInteger.valueOf(id);
		try {
			if (clasz.equals(DeliveryDetails.class) &&
					order != null && order.getDelivery()!=null) {
				order.getDelivery().setDeliveryID(biID);
				Context.askingCtrl.add(this);
			}
			else if (clasz.equals(ShipmentDetails.class) &&
					order != null && order.getDelivery()!=null) {
				ShipmentDetails sd = (ShipmentDetails) order.getDelivery();
				sd.setShipmentID(biID);
			} else if (order != null && clasz.equals(Order.class)==false) {
				if (Context.existingOrder == false) {
					Context.askingCtrl.add(this);
					f.order.addOrder(order);
				}
				else {
					f.order.updateOrder(order);
					if(Context.currentGUI instanceof PaymentGUIController)
						((PaymentGUIController)Context.currentGUI).loadNextWindow();
				}
			} else if(clasz.equals(Order.class) && order != null ) {
				order.setOrderID(biID);
				if(Context.currentGUI instanceof PaymentGUIController)
					((PaymentGUIController)Context.currentGUI).loadNextWindow();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}