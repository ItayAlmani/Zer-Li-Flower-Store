package izhar.interfaces;

import java.util.ArrayList;

import entities.DeliveryDetails;
import interfaces.IParent;

public interface IPickup extends IParent {

	void insertPickupToDB(DeliveryDetails deliveryDetails);
}