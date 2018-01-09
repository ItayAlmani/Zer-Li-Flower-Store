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
import entities.Product;
import entities.ShipmentDetails;
import entities.Store;
import entities.CSMessage.MessageType;
import izhar.interfaces.IPickup;

public class PickupController extends ParentController implements IPickup {
	
	@Override
	public DeliveryDetails parse(BigInteger deliveryID, BigInteger orderID, Store store, LocalDateTime date, boolean isImmediate) {
		return new DeliveryDetails(deliveryID, orderID, date, isImmediate, store);
	}

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<DeliveryDetails> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 6)
			prds.add(parse(
					(int) obj.get(i), 
					(int) obj.get(i + 1), 
					(int) obj.get(i + 3),
					((Timestamp) obj.get(i + 2)).toLocalDateTime(),
					((int) obj.get(i + 4)!= 0);
		sendProducts(prds);
	}
}