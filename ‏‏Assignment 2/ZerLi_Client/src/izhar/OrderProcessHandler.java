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

	private Order order;
	
	public String updateAfterCancellation(Order order) {
		return null;
	}
	
	public void updateFinilizeOrder(Order order) throws IOException {
		Factory f = Context.fac;
		this.order=order;
		this.order.setDate(LocalDateTime.now());
		try {
			if(order.getDeliveryType() != null) {
				Context.askingCtrl.add(this);
				f.pickup.addPickup(order.getDelivery());
				if(order.getDeliveryType().equals(DeliveryType.Shipment)) {
					Context.askingCtrl.add(this);
					f.shipment.addShipment((ShipmentDetails) order.getDelivery());
				}
			}
			else {
				Context.askingCtrl.add(this);
				f.order.addOrder(order);
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
			}
			else if (clasz.equals(ShipmentDetails.class) &&
					order != null && order.getDelivery()!=null) {
				ShipmentDetails sd = (ShipmentDetails) order.getDelivery();
				sd.setShipmentID(biID);
			} 
			if (order != null && clasz.equals(Order.class)==false) {
				f.order.updateOrder(order);
				if(Context.currentGUI instanceof PaymentGUIController)
					((PaymentGUIController)Context.currentGUI).loadNextWindow();
				f.stock.updateStock(order);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}