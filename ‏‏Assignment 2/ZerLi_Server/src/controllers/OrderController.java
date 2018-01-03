package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

import entities.*;
import entities.CSMessage.MessageType;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.PayMethod;
import entities.Order.Refund;
import entities.Subscription.SubscriptionType;

public class OrderController {
	private static String delIDSTR, shipIDSTR, payMeth, delTypeSTR, greeting;
	public static BigInteger addOrder(Order order) {
		order.setDate(LocalDateTime.now());
		try {
			if(order.getOrderID()==null || orderExist(order.getOrderID())==false)
				return addOrderWithQuery(order);
			if(order.getDeliveryType() != null) {
				if(order.getDeliveryType().equals(DeliveryType.Pickup)) {
					try {
						BigInteger id = PickupController.addPickup(order.getDelivery());
						order.getDelivery().setDeliveryID(id);
					} catch (Exception e) {
						System.err.println("Error with get id in order process");
						e.printStackTrace();
					}
				}
				else if(order.getDeliveryType().equals(DeliveryType.Shipment) && 
						order.getDelivery() instanceof ShipmentDetails) {
					try {
						ShipmentDetails ship = (ShipmentDetails) order.getDelivery();
						BigInteger id = ShipmentController.addShipment(ship);
						ship.setShipmentID(id);
					} catch (Exception e) {
						System.err.println("Error with get id in order process");
						e.printStackTrace();
					}
				}
			}
			return updateOrder(order);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean orderExist(BigInteger id) throws Exception {
		String query = "SELECT * FROM orders WHERE orderID='"+id.toString()+"'";
		ArrayList<Object> arr =  ServerController.db.getQuery(query);
		if(arr!=null && arr.size()!=0)
			return true;
		return false;
	}
	
	
	
	public static BigInteger updateOrder(Order order) throws Exception {
		prepareStrings(order);
		String query = "UPDATE orders SET customerID = '" + order.getCustomerID() 
		+ "',deliveryID = " + delIDSTR 
		+ ",type = '" + order.getType().toString() 
		+ "',paymentMethod = " + payMeth
		+ ",shipmentID = " + shipIDSTR
		+ ",greeting = " + greeting 
		+ ",deliveryType = " + delTypeSTR
		+ ",status='" + order.getOrderStatus().toString() 
		+ "',date = '" + (Timestamp.valueOf(order.getDate())).toString()
		+ "',price = '"+ order.getFinalPrice()
		+ "' WHERE orderID='" + order.getOrderID() + "'";
		ServerController.db.updateQuery(query);
		return getNextID(order).subtract(BigInteger.ONE);
	}
	
	public static BigInteger addOrderWithQuery(Order order) throws Exception {
		prepareStrings(order);
		String query = "INSERT INTO orders (customerID, deliveryID, type, paymentMethod, shipmentID, greeting, deliveryType, status, date, price)"
				+ "VALUES ('" + order.getCustomerID() + "'"
				+ ", " + delIDSTR + ", '"
				+ order.getType().toString() + "', "
				+ payMeth + ", " 
				+ shipIDSTR + ", "
				+  greeting + ", "
				+ delTypeSTR + ", '" 
				+ order.getOrderStatus().toString() + "', '"
				+ (Timestamp.valueOf(order.getDate())).toString() + "','"+
				 order.getFinalPrice()+"');";
		ServerController.db.updateQuery(query);
		return getNextID(order);
	}
	
	private static void prepareStrings(Order order) {
		delIDSTR ="NULL";shipIDSTR="NULL"; payMeth = "NULL"; delTypeSTR="NULL"; greeting = "NULL";
		DeliveryType type = order.getDeliveryType();
		if(type != null) {
			DeliveryDetails del = order.getDelivery();
			if(type.equals(DeliveryType.Pickup) && del != null) {
				BigInteger delID = del.getDeliveryID();
				if(delID!=null)
					delIDSTR="'"+delID.toString()+"'";
			}
			else if(type.equals(DeliveryType.Shipment) && del != null) {
				ShipmentDetails ship = ((ShipmentDetails)del);
				BigInteger shipID = ship.getShipmentID();
				if(ship!=null && shipID!=null)
					shipIDSTR="'"+shipID.toString()+"'";
			}
			delTypeSTR="'"+type.toString()+"'";
		}
		if(order.getPaymentMethod()!=null)
			payMeth="'"+order.getPaymentMethod().toString()+"'";
		if(order.getGreeting()!=null)
			greeting = "'"+order.getGreeting()+"'";
	}
	
	private static BigInteger getNextID(Order order) throws Exception {
		String query = "SELECT Max(orderID) from orders";
		if(order.getDelivery()!=null && order.getDelivery() instanceof ShipmentDetails == false &&
				order.getDelivery().getDeliveryID()!=null)
			query = "SELECT orderID from orders where deliveryID='"+order.getDelivery().getDeliveryID()+"'";
		else if(order.getDeliveryType()!=null &&
				order.getDeliveryType().equals(DeliveryType.Shipment) &&
				order.getDelivery() instanceof ShipmentDetails &&
				((ShipmentDetails)order.getDelivery())!=null &&
				((ShipmentDetails)order.getDelivery()).getShipmentID()!=null) {
			query = "SELECT orderID from orders where shipmentID='"+
					((ShipmentDetails)order.getDelivery()).getShipmentID()+"'";
		}
		ArrayList<Object> arr =  ServerController.db.getQuery(query);
		arr.set(0, ((Integer)arr.get(0))+1);
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)arr.get(0));
		throw new Exception("Error Order Update - no id");
	}
}