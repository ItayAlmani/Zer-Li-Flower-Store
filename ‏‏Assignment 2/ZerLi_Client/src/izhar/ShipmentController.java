package izhar;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.DeliveryDetails;
import entities.ShipmentDetails;
import entities.CSMessage.MessageType;
import izhar.interfaces.IShipment;

public class ShipmentController extends ParentController implements IShipment {

	@Override
	public void addShipment(ShipmentDetails shipmentDetails) throws IOException {
		Context.fac.pickup.addPickup(new DeliveryDetails(
				shipmentDetails.getOrderID(),
				shipmentDetails.getDate(),
				shipmentDetails.isImmediate(),
				shipmentDetails.getStore()));
		myMsgArr.clear();
		myMsgArr.add(
				"INSERT INTO shipmentdetails (deliveryID, street, city, postCode, customerName, phoneNumber)" +
				"VALUES ('"+(Context.fac.pickup.getDelID()-1) + "', '"
						+ shipmentDetails.getStreet() + "', '"
						+ shipmentDetails.getCity() + "', '"
						+ shipmentDetails.getPostCode() + "', '"
						+ shipmentDetails.getCustomerName() + "', '"
						+ shipmentDetails.getPhoneNumber()
						+ "');"
				);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}
}