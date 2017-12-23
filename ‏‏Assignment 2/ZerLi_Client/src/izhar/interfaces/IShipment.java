package izhar.interfaces;


import entities.ShipmentDetails;
import interfaces.IParent;

public interface IShipment extends IParent  {

	void insertShipmentToDB(ShipmentDetails shipmentDetails);
}