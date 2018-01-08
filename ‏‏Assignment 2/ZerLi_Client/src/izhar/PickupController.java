package izhar;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.DeliveryDetails;
import entities.ShipmentDetails;
import entities.Store;
import entities.CSMessage.MessageType;
import izhar.interfaces.IPickup;

public class PickupController extends ParentController implements IPickup {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}
	
	
}