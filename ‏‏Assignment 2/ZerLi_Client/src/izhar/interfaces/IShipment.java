package izhar.interfaces;


import java.io.IOException;

import entities.ShipmentDetails;
import interfaces.IParent;

public interface IShipment extends IParent  {

	void addShipment(ShipmentDetails shipmentDetails) throws IOException;
}