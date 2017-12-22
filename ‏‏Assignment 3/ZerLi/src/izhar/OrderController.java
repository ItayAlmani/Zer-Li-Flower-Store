package izhar;

import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import enums.Refund;
import interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {

	public Order getOrder(String orderID) {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
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