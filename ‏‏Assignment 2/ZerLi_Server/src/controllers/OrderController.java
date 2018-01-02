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
				else if(order.getDeliveryType().equals(DeliveryType.Shipment)) {
					try {
						BigInteger id = ShipmentController.addShipment(order.getDelivery(),(ShipmentDetails) order.getDelivery());
						order.getDelivery().setDeliveryID(id);
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
		String delID ="NULL", shipID="NULL", payMeth = "NULL", delType="NULL", greeting = "NULL";
		if(order.getDeliveryType() != null) {
			if(order.getDeliveryType().equals(DeliveryType.Pickup) && order.getDelivery() != null)
				delID="'"+order.getDelivery().getDeliveryID().toString()+"'";
			else if(order.getDeliveryType().equals(DeliveryType.Shipment) && order.getDelivery() != null)
				shipID="'"+((ShipmentDetails)order.getDelivery()).getOrderID().toString()+"'";
			delType="'"+order.getDeliveryType().toString()+"'";
		}
		if(order.getPaymentMethod()!=null)
			payMeth="'"+order.getPaymentMethod().toString()+"'";
		if(order.getGreeting()!=null)
			greeting = "'"+order.getGreeting()+"'";
		String query = "UPDATE orders SET customerID = '" + order.getCustomerID() 
		+ "',deliveryID = " + delID 
		+ ",type = '" + order.getType().toString() 
		+ "',paymentMethod = " + payMeth
		+ ",shipmentID = " + shipID
		+ ",greeting = " + greeting 
		+ ",deliveryType = " + delType
		+ ",status='" + order.getOrderStatus().toString() 
		+ "',date = '" + (Timestamp.valueOf(order.getDate())).toString()
		+ "',price = '"+ order.getFinalPrice()
		+ "' WHERE orderID='" + order.getOrderID() + "'";
		ServerController.db.updateQuery(query);
		return getNextID(order).subtract(BigInteger.ONE);
	}
	
	public static BigInteger addOrderWithQuery(Order order) throws Exception {
		String delID ="NULL", shipID="NULL", payMeth = "NULL", delType="NULL", greeting = "NULL";
		if(order.getDeliveryType() != null) {
			if(order.getDeliveryType().equals(DeliveryType.Pickup) && order.getDelivery() != null &&
					order.getDelivery().getDeliveryID() != null)
				delID="'"+order.getDelivery().getDeliveryID().toString()+"'";
			else if(order.getDeliveryType().equals(DeliveryType.Shipment) && order.getDelivery() != null)
				shipID="'"+((ShipmentDetails)order.getDelivery()).getOrderID().toString()+"'";
			delType="'"+order.getDeliveryType().toString()+"'";
		}
		if(order.getPaymentMethod()!=null)
			payMeth="'"+order.getPaymentMethod().toString()+"'";
		if(order.getGreeting()!=null)
			greeting = "'"+order.getGreeting()+"'";
		String query = "INSERT INTO orders (customerID, deliveryID, type, paymentMethod, shipmentID, greeting, deliveryType, status, date, price)"
				+ "VALUES ('" + order.getCustomerID() + "'"
				+ ", " + delID + ", '"
				+ order.getType().toString() + "', "
				+ payMeth + ", " 
				+ shipID + ", "
				+  greeting + ", "
				+ delType + ", '" 
				+ order.getOrderStatus().toString() + "', '"
				+ (Timestamp.valueOf(order.getDate())).toString() + "','"+
				 order.getFinalPrice()+"');";
		ServerController.db.updateQuery(query);
		return getNextID(order);
	}
	
	private static BigInteger getNextID(Order order) throws Exception {
		String query;
		if(order.getDelivery()!=null && order.getDelivery().getDeliveryID()!=null)
			query = "SELECT orderID from orders where deliveryID='"+order.getDelivery().getDeliveryID()+"'";
		else if(order.getDeliveryType()!=null &&
				order.getDeliveryType().equals(DeliveryType.Shipment) &&
				((ShipmentDetails)order.getDelivery())!=null &&
				((ShipmentDetails)order.getDelivery()).getShipmentID()!=null)
			query = "SELECT orderID from orders where shipmentID='"+
					((ShipmentDetails)order.getDelivery()).getShipmentID()+"'";
		else {
			query = "SELECT Max(orderID) from orders";
		}
		ArrayList<Object> arr =  ServerController.db.getQuery(query);
		arr.set(0, ((Integer)arr.get(0))+1);
		if(arr!=null && arr.size()==1 && arr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)arr.get(0));
		throw new Exception("Error Order Update - no id");
	}
}