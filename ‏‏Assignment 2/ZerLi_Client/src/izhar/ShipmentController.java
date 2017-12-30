package izhar;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.DeliveryDetails;
import entities.ShipmentDetails;
import entities.Transaction;
import entities.CSMessage.MessageType;
import izhar.interfaces.IShipment;

public class ShipmentController extends ParentController implements IShipment {
	private ShipmentDetails shipmentDetails = null;
	
	public void setLastAutoIncrenment(ArrayList<Object> obj) throws IOException {
		ShipmentDetails.setIdInc((BigInteger)obj.get(10));
	}
	
	@Override
	public void addShipment(ShipmentDetails shipmentDetails) throws IOException {
		Context.askingCtrl.add(Context.askingCtrl.size()-1,this);
		this.shipmentDetails=shipmentDetails;
		Context.fac.pickup.addPickup(new DeliveryDetails(
				shipmentDetails.getOrderID(),
				shipmentDetails.getDate(),
				shipmentDetails.isImmediate(),
				shipmentDetails.getStore()));
	}
	
	public void setUpdateRespond(Integer id, Class<?> clasz) {
		myMsgArr.clear();
		String query = "INSERT INTO shipmentdetails (deliveryID, street, city, postCode, customerName, phoneNumber)" +
				"VALUES ('"+id.toString() + "', '"
				+ shipmentDetails.getStreet() + "', '"
				+ shipmentDetails.getCity() + "', '"
				+ shipmentDetails.getPostCode() + "', '"
				+ shipmentDetails.getCustomerName() + "', '"
				+ shipmentDetails.getPhoneNumber()
				+ "');";
		query += "SELECT Max(shipmentID) from shipmentdetails;";
		myMsgArr.add(query);
		try {
			Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,ShipmentDetails.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}
}