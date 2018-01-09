package izhar.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;

import entities.DeliveryDetails;
import entities.Store;
import interfaces.IParent;

public interface IPickup extends IParent {

	
	DeliveryDetails parse(BigInteger deliveryID, BigInteger orderID, Store store, LocalDateTime date, boolean isImmediate);
}