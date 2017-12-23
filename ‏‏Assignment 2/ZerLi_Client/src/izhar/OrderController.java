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
	public void getOrderWithProducts(String orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT prd.*, ord.*" + 
				"FROM orders ord, productincart pic, product prd, shoppingcart sc" + 
				"join orders ON sc.orderID=orders.orderID" + 
				"join productincart ON sc.cartID=productincart.cartID" + 
				"where pic.productID = prd.productID and ord.orderID = '"+orderID+"';");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Order.class));
	}
	
	public

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
	public String cancelOrder(Order order) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeliveryDetails getDelivery(String deliveryID) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Refund differenceDeliveryTimeAndCurrent(DeliveryDetails delivery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrder(Order order) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Order> getAllOrders(String storeid) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePriceWithShipment(Order order) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Parsing obj to ArrayList of <code>Product</code> and sending it to the client
	 * @param obj - ArrayList of each cell in the table
	 * For example, go to controllers.ProductController
	 */
	public static void handleGet(ArrayList<Object> obj) {
		// TODO - implement handleGet
	}
	
}