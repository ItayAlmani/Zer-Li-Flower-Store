package izhar;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import enums.Refund;
import interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {

	@Override
	public void getOrderFromServer(String orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM order WHERE orderID='"+orderID+"';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Order.class));
	}

	@Override
	public void noOrderIDErrMsg() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Refund checkOrderRefund(Order order) {
		
		return null;
	}

	@Override
	public boolean updateCustomerComplaintRefund(Complaint complaint) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addNewOrder(Order order) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getId()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
		
	}

	@Override
	public String cancelOrderFromServer(Order order) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryDetails getDeliveryFromServer(String deliveryID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrderFromServer(Order order) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Order> getAllOrdersFromServer(String storeid) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePriceWithShipmentFromServer(Order order) throws IOException {
		// TODO Auto-generated method stub
		
	}

}