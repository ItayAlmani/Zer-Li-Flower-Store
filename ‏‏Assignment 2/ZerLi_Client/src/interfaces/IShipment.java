package interfaces;

import java.util.ArrayList;

import entities.ShipmentDetails;

public interface IShipment {

	void insertShipmentToDB(ShipmentDetails shipmentDetails);
}