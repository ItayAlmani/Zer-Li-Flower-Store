package izhar;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.operations.Bool;

import common.EchoServer;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.PayMethod;
import entities.ShipmentDetails;
import interfaces.IOrder;

public class OrderController extends ParentController implements IOrder {
	private String delIDSTR, shipIDSTR, payMeth, delTypeSTR, greeting;
	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Order == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Order order = (Order)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		BigInteger nextID = null;
		order.setDate(LocalDateTime.now());
		try {
			if(order.getOrderID()==null || orderExist(order.getOrderID())==false) {
				addOrderWithQuery(order);
				nextID=getNextID(order);
			}
			else if(order.getDeliveryType() != null) {
				if(order.getDeliveryType().equals(DeliveryType.Pickup))
					order.getDelivery().setDeliveryID(addPickupWithOrder(order.getDelivery()));
				else if(order.getDeliveryType().equals(DeliveryType.Shipment) && 
						order.getDelivery() instanceof ShipmentDetails) {
					ShipmentDetails ship = (ShipmentDetails) order.getDelivery();
					ship.setShipmentID(addShipmentWithOrder(ship));
				}
				update(order);
				nextID=getNextID(order).subtract(BigInteger.ONE);
			}
			myMsgArr.clear();
			if(isReturnNextID)
				myMsgArr.add(nextID);
			else
				myMsgArr.add(true);
			return myMsgArr;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private BigInteger addPickupWithOrder(DeliveryDetails del) throws Exception {
		try {
			myMsgArr.clear();
			myMsgArr.add(del);
			myMsgArr.add(true);
			myMsgArr = EchoServer.fac.pickup.add(myMsgArr);
			if(myMsgArr.get(0) instanceof BigInteger)
				return (BigInteger)myMsgArr.get(0);
			throw new Exception("addPickupWithOrder() error\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	private BigInteger addShipmentWithOrder(ShipmentDetails ship) throws Exception{
		try {
			myMsgArr.clear();
			myMsgArr.add(ship);
			myMsgArr.add(true);
			myMsgArr = EchoServer.fac.pickup.add(myMsgArr);
			if(myMsgArr.get(0) instanceof BigInteger)
				return (BigInteger)myMsgArr.get(0);
			throw new Exception("addShipmentWithOrder() error\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	private boolean orderExist(BigInteger id) throws SQLException {
		String query = "SELECT * FROM orders WHERE orderID='"+id.toString()+"'";
		myMsgArr = EchoServer.fac.dataBase.db.getQuery(query);
		if(myMsgArr!=null && myMsgArr.size()!=0)
			return true;
		return false;
	}

	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Order ) {
			Order order = (Order)obj;
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
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		else
			throw new Exception();
	}
	
	private void addOrderWithQuery(Order order) throws Exception {
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
		EchoServer.fac.dataBase.db.updateQuery(query);
	}
	
	private void prepareStrings(Order order) {
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
	
	private BigInteger getNextID(Order order) throws Exception {
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
		myMsgArr = EchoServer.fac.dataBase.db.getQuery(query);
		myMsgArr.set(0, ((Integer)myMsgArr.get(0))+1);
		if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer)
			return BigInteger.valueOf((Integer)myMsgArr.get(0));
		throw new Exception("Error Order Update - no id");
	}

	public ArrayList<Object> cancelOrder(Order order) {
		String query = "UPDATE orders SET status='" + OrderStatus.Canceled.toString() + "' WHERE orderID='"
				+ order.getOrderID() + "'";
		myMsgArr.clear();
		try {
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.add(true);
		} catch (SQLException e) {
			e.printStackTrace();
			myMsgArr.add(false);
		}
		return myMsgArr;
	}
	
	public ArrayList<Object> getOrdersByCustomerID(BigInteger customerID) throws SQLException {
		String query = "SELECT * FROM orders WHERE customerID='"+customerID+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	public ArrayList<Object> getOrderInProcess(BigInteger customerID) throws SQLException {
		String query = "SELECT * FROM orders WHERE customerID='"+	customerID+"' AND status='InProcess';";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getAllOrdersByStoreID(BigInteger storeID) throws SQLException {
		String query = 
				"SELECT ord.*" + 
				" FROM orders AS ord" + 
				" JOIN deliverydetails ON ord.orderID=deliverydetails.orderID" + 
				" WHERE deliverydetails.storeID='"+storeID+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getProductsInOrder(BigInteger orderID) throws SQLException {
		String query = "SELECT productID, quantity, totalprice FROM" + 
				"(" + 
				"	SELECT ordCart.* FROM" + 
				"	(" + 
				"		SELECT crt.*" + 
				"		FROM cart AS crt" + 
				"		JOIN orders ON crt.orderID=orders.orderID" + 
				"		where crt.orderID = '"+orderID+"'" + 
				"	) AS ordCart" + 
				"	JOIN product ON ordCart.productID=product.productID" + 
				") AS prodInOrd";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getOrdersWaitingForPaymentByCustomerID(BigInteger customerID) throws SQLException {
		String query = "SELECT * FROM orders WHERE customerID='"+
				customerID+"' AND status='WaitingForCashPayment'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return obj;
		ArrayList<Object> ords = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 11) {
			BigInteger deliveryID = obj.get(i + 2)==null?null:BigInteger.valueOf(Long.valueOf((int)obj.get(i + 2))),
					shipmentID=obj.get(i + 4)==null?null:BigInteger.valueOf(Long.valueOf((int) obj.get(i + 4)));
			ords.add(parse(
					BigInteger.valueOf(Long.valueOf((int)obj.get(i))), 
					BigInteger.valueOf(Long.valueOf((int) obj.get(i + 1))),
					deliveryID, 
					obj.get(i + 3)==null?null:(String)obj.get(i + 3),
					shipmentID,
					obj.get(i + 5)==null?null:(String)obj.get(i + 5),
					(String) obj.get(i + 6),
					(String) obj.get(i + 7),
					(String) obj.get(i + 8),
					(Timestamp)obj.get(i + 9),
					(float)obj.get(i+10)
					));
		}
		return ords;
	}
	
	public Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethod, BigInteger shipmentID, String type,
			String greeting, String deliveryType, String orderStatus, Timestamp date, float price) {
		LocalDateTime ldtDate = date.toLocalDateTime();
		if(deliveryType==null)
			return new Order(orderID,
					customerID,
					OrderType.valueOf(type),
					payMethod==null?null:PayMethod.valueOf(payMethod), 
					greeting,
					OrderStatus.valueOf(orderStatus), 
					ldtDate,
					price);
		else if(DeliveryType.valueOf(deliveryType).equals(DeliveryType.Pickup))
			return new Order(orderID,
					customerID,
					new DeliveryDetails(deliveryID),
					OrderType.valueOf(type),
					PayMethod.valueOf(payMethod), 
					greeting,
					DeliveryType.valueOf(deliveryType), 
					OrderStatus.valueOf(orderStatus), 
					ldtDate, 
					price);
		else if(DeliveryType.valueOf(deliveryType).equals(DeliveryType.Shipment))
			return new Order(orderID,
					customerID,
					new ShipmentDetails(shipmentID),
					OrderType.valueOf(type),
					PayMethod.valueOf(payMethod), 
					greeting,
					DeliveryType.valueOf(deliveryType), 
					OrderStatus.valueOf(orderStatus), 
					ldtDate,
					price);
		else return null;
	}
}