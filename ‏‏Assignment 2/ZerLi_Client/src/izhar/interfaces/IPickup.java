package izhar.interfaces;

import entities.DeliveryDetails;
import interfaces.IParent;

public interface IPickup extends IParent {

	void addPickup(DeliveryDetails deliveryDetails);
}