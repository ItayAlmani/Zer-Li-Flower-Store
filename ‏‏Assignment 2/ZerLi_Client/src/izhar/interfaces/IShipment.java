package izhar.interfaces;


import entities.ShipmentDetails;
import interfaces.IParent;

public interface IShipment extends IParent  {

	void addShipment(ShipmentDetails shipmentDetails);
}