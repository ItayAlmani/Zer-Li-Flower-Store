package izhar;

import java.util.ArrayList;

import controllers.ParentController;
import entities.*;
import enums.Refund;
import interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {

	public Order getOrder(String orderID) {
		
	}

	public void noOrderIDErrMsg() {
	}

	public Refund checkOrderRefund(Order order) {
	}

	public boolean updateCustomerComplaintRefund(Complaint complaint) {
	}
	
	public void insertOrderToDB(Order order) {
	}

	public String cancelOrder(Order order) {
	}

	public DeliveryDetails getDeliveryByID(String deliveryID) {
	}

	public Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery) {
	}

	public void updateOrderInDB(Order order) {
	}

	public ArrayList<Order> getOrdersByStore(String storeid) {
	}


	public void updatePriceWithShipment(Order order) {
	}

}