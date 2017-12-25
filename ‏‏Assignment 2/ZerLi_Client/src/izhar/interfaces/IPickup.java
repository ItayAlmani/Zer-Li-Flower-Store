package izhar.interfaces;

import java.io.IOException;

import entities.DeliveryDetails;
import entities.Store;
import interfaces.IParent;

public interface IPickup extends IParent {

	void addPickup(DeliveryDetails deliveryDetails) throws IOException;
	
	DeliveryDetails parse(int deliveryID, int orderID, Store store, java.util.Date date, boolean isImmediate);
}