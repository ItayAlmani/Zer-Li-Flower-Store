package izhar;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.PayMethod;
import entities.ProductInOrder;
import entities.ShipmentDetails;
import entities.Store;

public class OrderController extends ParentController implements IOrder{
	private String delIDSTR, shipIDSTR, payMeth, delTypeSTR, greeting;

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if (arr != null && (arr.get(0) instanceof Order == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Order order = (Order) arr.get(0);
		boolean isReturnNextID = (boolean) arr.get(1);
		BigInteger nextID = null;
		order.setDate(LocalDateTime.now());
		try {
			if (order.getDeliveryType() != null) {
				
				//update first the delivery in db
				updatePickupWithOrderInDB(order.getDelivery());
				if (order.getDeliveryType().equals(DeliveryType.Shipment)
						&& order.getDelivery() instanceof ShipmentDetails) {
					ShipmentDetails ship = (ShipmentDetails) order.getDelivery();
					
					//add first the shipment to db
					ship.setShipmentID(addShipmentWithOrder(ship));
				}
			}
			if (order.getOrderID() == null || orderExist(order.getOrderID()) == false) {
				addOrderWithQuery(order);
				nextID = getNextID(order);
			}
			else {
				update(order);
				nextID = getNextID(order).subtract(BigInteger.ONE);
			}
			myMsgArr.clear();
			if (isReturnNextID)
				myMsgArr.add(nextID);
			else
				myMsgArr.add(true);
			return myMsgArr;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void updatePickupWithOrderInDB(DeliveryDetails del) throws Exception {
		try {
			myMsgArr.clear();
			myMsgArr.add(del);
			myMsgArr.add(true);
			myMsgArr = EchoServer.fac.pickup.update(del);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private BigInteger addShipmentWithOrder(ShipmentDetails ship) throws Exception {
		try {
			myMsgArr.clear();
			myMsgArr.add(ship);
			myMsgArr.add(true);
			myMsgArr = EchoServer.fac.shipment.add(myMsgArr);
			if (myMsgArr.get(0) instanceof BigInteger)
				return (BigInteger) myMsgArr.get(0);
			throw new Exception("addShipmentWithOrder() error\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private boolean orderExist(BigInteger id) throws SQLException {
		String query = "SELECT * FROM orders WHERE orderID='" + id.toString() + "'";
		myMsgArr = EchoServer.fac.dataBase.db.getQuery(query);
		if (myMsgArr != null && myMsgArr.size() != 0)
			return true;
		return false;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if (obj instanceof Order) {
			Order order = (Order) obj;
			prepareStrings(order);
			String query = "UPDATE orders SET customerID = '" + order.getCustomerID() + "',deliveryID = " + delIDSTR
					+ ",type = '" + order.getType().toString() + "',paymentMethod = " + payMeth + ",shipmentID = "
					+ shipIDSTR + ",greeting = " + greeting + ",deliveryType = " + delTypeSTR + ",status='"
					+ order.getOrderStatus().toString() + "',date = '" + (Timestamp.valueOf(order.getDate())).toString()
					+ "',price = '" + order.getFinalPrice() + "' WHERE orderID='" + order.getOrderID() + "'";
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		} else
			throw new Exception();
	}

	private void addOrderWithQuery(Order order) throws Exception {
		prepareStrings(order);
		String query = "INSERT INTO orders (customerID, deliveryID, type, paymentMethod, shipmentID, greeting, deliveryType, status, date, price)"
				+ "VALUES ('" + order.getCustomerID() + "'" + ", " + delIDSTR + ", '" + order.getType().toString()
				+ "', " + payMeth + ", " + shipIDSTR + ", " + greeting + ", " + delTypeSTR + ", '"
				+ order.getOrderStatus().toString() + "', '" + (Timestamp.valueOf(order.getDate())).toString() + "','"
				+ order.getFinalPrice() + "');";
		EchoServer.fac.dataBase.db.updateQuery(query);
	}

	private void prepareStrings(Order order) {
		delIDSTR = "NULL";
		shipIDSTR = "NULL";
		payMeth = "NULL";
		delTypeSTR = "NULL";
		greeting = "NULL";
		DeliveryDetails del = order.getDelivery();
		if(del != null) {
			DeliveryType type = order.getDeliveryType();
			if (type!=null) {
				if (type.equals(DeliveryType.Pickup)) {
					BigInteger delID = del.getDeliveryID();
					if (delID != null)
						delIDSTR = "'" + delID.toString() + "'";
				} else if (type.equals(DeliveryType.Shipment) && del != null) {
					ShipmentDetails ship = ((ShipmentDetails) del);
					BigInteger shipID = ship.getShipmentID();
					if (ship != null && shipID != null)
						shipIDSTR = "'" + shipID.toString() + "'";
				}
				delTypeSTR = "'" + type.toString() + "'";
			}
			else {
				BigInteger delID = del.getDeliveryID();
				if (delID != null)
					delIDSTR = "'" + delID.toString() + "'";
			}
		}
		if (order.getPaymentMethod() != null)
			payMeth = "'" + order.getPaymentMethod().toString() + "'";
		if (order.getGreeting() != null)
			greeting = "'" + order.getGreeting() + "'";
	}

	private BigInteger getNextID(Order order) throws Exception {
		String query;
		boolean tryNext = true;
		if (order.getDelivery() != null && order.getDelivery() instanceof ShipmentDetails == false
				&& order.getDelivery().getDeliveryID() != null) {
			query = "SELECT orderID from orders where deliveryID='" + order.getDelivery().getDeliveryID() + "'";
			myMsgArr = EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr != null && myMsgArr.isEmpty()==false)
				tryNext=false;
		}
		if (tryNext==true && order.getDeliveryType() != null && order.getDeliveryType().equals(DeliveryType.Shipment)
				&& order.getDelivery() instanceof ShipmentDetails && ((ShipmentDetails) order.getDelivery()) != null
				&& ((ShipmentDetails) order.getDelivery()).getShipmentID() != null) {
			query = "SELECT orderID from orders where shipmentID='"
					+ ((ShipmentDetails) order.getDelivery()).getShipmentID() + "'";
			myMsgArr = EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr != null && myMsgArr.isEmpty()==false)
				tryNext=false;
		}
		if(tryNext==true){
			query = "SELECT Max(orderID) from orders";
			myMsgArr = EchoServer.fac.dataBase.db.getQuery(query);
		}
		if(myMsgArr!=null && myMsgArr.size()==1) {
			myMsgArr.set(0, ((Integer) myMsgArr.get(0)) + 1);
			if (myMsgArr != null && myMsgArr.size() == 1 && myMsgArr.get(0) instanceof Integer)
				return BigInteger.valueOf((Integer) myMsgArr.get(0));
		}
		throw new Exception("Error Order Update - no id");
	}

	@Override
	public ArrayList<Object> getOrdersByCustomerID(BigInteger customerID) throws Exception {
		String query = "SELECT * FROM orders WHERE customerID='" + customerID + "'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> getCancelableOrdersByCustomerID(BigInteger customerID) throws Exception {
		String query = "SELECT ord.*"
				+	" FROM orders AS ord, deliverydetails AS del"
				+	" WHERE customerID='" + customerID + "' AND ord.status='Paid'" + 
					"	AND ord.deliveryID=del.deliveryID AND curdate() < del.date";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	@Override
	public ArrayList<Object> getOrAddOrderInProcess(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof BigInteger == false) || arr.get(1) instanceof Store == false)
			throw new Exception();
		BigInteger customerID = (BigInteger)arr.get(0);
		Store store = (Store)arr.get(1);
		String query = "SELECT ord.*" + 
				" FROM orders AS ord, deliverydetails AS del" + 
				" WHERE ord.deliveryID=del.deliveryID AND" + 
				"	del.storeID='"+store.getStoreID()+"' AND" + 
				"	ord.customerID='"+customerID+"' AND" + 
				"    ord.status='InProcess'";
		ArrayList<Object> ords = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		//Order InProccess of specific store exist!
		if(ords!=null && ords.isEmpty()==false)
			return ords;
		
		//Need to add new InProccess order in specific store 
		Order ord = new Order();
		DeliveryDetails del = new DeliveryDetails(store);
		myMsgArr.clear();
		myMsgArr.add(del);
		myMsgArr.add(true);
		myMsgArr = EchoServer.fac.pickup.add(myMsgArr);
		if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof BigInteger) {
			del.setDeliveryID((BigInteger)myMsgArr.get(0));
			ord.setDelivery(del);
			ord.setCustomerID(customerID);
			ord.setType(OrderType.InfoSystem);
			ord.setOrderStatus(OrderStatus.InProcess);
			myMsgArr.clear();
			myMsgArr.add(ord);
			myMsgArr.add(true);
			myMsgArr=add(myMsgArr);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof BigInteger) {
				ord.setOrderID(((BigInteger)myMsgArr.get(0)).subtract(BigInteger.ONE));
				myMsgArr.clear();
				myMsgArr.add(ord);
				return myMsgArr;
			}
			throw new Exception();
		}
		throw new Exception();
	}

	@Override
	public ArrayList<Object> getOrdersForReportByStoreID(BigInteger storeID, LocalDate startDate, LocalDate endDate) throws Exception {
		String query=null;
		if(!storeID.equals(BigInteger.valueOf(-1))) {
		query = String.format("SELECT ord.*\n" + 
				" FROM orders AS ord, deliverydetails AS del, shipmentdetails AS sh\n" + 
				" WHERE\n" + 
				"	(\n" + 
				"		del.deliveryID=ord.deliveryID\n" + 
				"		OR\n" + 
				"		ord.shipmentID=sh.shipmentID AND sh.deliveryID=del.deliveryID\n" + 
				"    )\n" + 
				"    AND del.storeID='%d'\n" + 
				"    AND ord.date>='%s' AND ord.date<='%s'\n" + 
				"    AND (ord.status='Paid' OR ord.status='Canceled');"
				, storeID,
				(Timestamp.valueOf(startDate.atStartOfDay())).toString(),
				(Timestamp.valueOf(endDate.atTime(LocalTime.of(23, 59, 59)))).toString());}
		else {
			query = String.format("SELECT ord.*\n" + 
					" FROM orders AS ord, deliverydetails AS del, shipmentdetails AS sh\n" + 
					" WHERE\n" + 
					"	(\n" + 
					"		del.deliveryID=ord.deliveryID\n" + 
					"		OR\n" + 
					"		ord.shipmentID=sh.shipmentID AND sh.deliveryID=del.deliveryID\n" + 
					"    )\n" + 
					"    AND ord.date>='%s' AND ord.date<='%s'\n" + 
					"    AND (ord.status='Paid' OR ord.status='Canceled');"
					,(Timestamp.valueOf(startDate.atStartOfDay())).toString(),
					(Timestamp.valueOf(endDate.atTime(LocalTime.of(23, 59, 59)))).toString());
		}
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> getProductsInOrder(BigInteger orderID) throws Exception {
		String query = "SELECT productID, quantity, totalprice FROM" + "(" + "	SELECT ordCart.* FROM" + "	("
				+ "		SELECT crt.*" + "		FROM cart AS crt" + "		JOIN orders ON crt.orderID=orders.orderID"
				+ "		where crt.orderID = '" + orderID + "'" + "	) AS ordCart"
				+ "	JOIN product ON ordCart.productID=product.productID" + ") AS prodInOrd";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if (obj == null)
			return new ArrayList<>();
		ArrayList<Object> ords = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 11) {
			BigInteger deliveryID = obj.get(i + 2) == null ? null: BigInteger.valueOf((Integer) obj.get(i + 2)),
					shipmentID = obj.get(i + 4) == null ? null : BigInteger.valueOf((Integer) obj.get(i + 4));
			ords.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)),
					BigInteger.valueOf((Integer) obj.get(i+1)),
					deliveryID,
					obj.get(i + 3) == null ? null : (String) obj.get(i + 3),
					shipmentID,
					obj.get(i + 5) == null ? null : (String) obj.get(i + 5),
					(String) obj.get(i + 6),
					(String) obj.get(i + 7),
					(String) obj.get(i + 8),
					(Timestamp) obj.get(i + 9),
					(float) obj.get(i + 10)));
		}
		return ords;
	}

	@Override
	public Order parse(BigInteger orderID, BigInteger customerID, BigInteger deliveryID, String payMethod,
			BigInteger shipmentID, String type, String greeting, String deliveryType, String orderStatus,
			Timestamp date, float price) throws Exception {
		LocalDateTime ldtDate = date.toLocalDateTime();
		DeliveryDetails dl = null;
		if(deliveryType!=null && deliveryType.isEmpty()==false) {
			DeliveryType dt = DeliveryType.valueOf(deliveryType);
			if(dt.equals(DeliveryType.Shipment)) {
				if(shipmentID!=null) {
					ArrayList<Object> shipsObj = EchoServer.fac.shipment.getShipmentByID(shipmentID);
					if(shipsObj!=null && shipsObj.size()==1 && shipsObj.get(0) instanceof ShipmentDetails)
						dl=(DeliveryDetails)shipsObj.get(0);
					else {
						System.err.println("No shipment");
						throw new Exception();
					}
				}
			}
		}
		if(dl==null) {
			if(deliveryID != null) {
				ArrayList<Object> delsObj = EchoServer.fac.pickup.getDeliveryByID(deliveryID);
				if(delsObj!=null && delsObj.size()==1 && delsObj.get(0) instanceof DeliveryDetails)
					dl=(DeliveryDetails)delsObj.get(0);
				else {
					System.err.println("No delivery");
					throw new Exception();
				}
			}
		}
		Order ord =  new Order(orderID, 
				customerID, 
				dl, 
				type == null ? null : OrderType.valueOf(type),
				payMethod == null ? null : PayMethod.valueOf(payMethod), 
				greeting,
				deliveryType == null ? null : DeliveryType.valueOf(deliveryType),
				orderStatus == null ? null : OrderStatus.valueOf(orderStatus), 
				ldtDate, 
				price);
		ArrayList<ProductInOrder> pios = new ArrayList<>();
		for (Object object : EchoServer.fac.prodInOrder.getPIOsByOrder(orderID)) {
			if(object instanceof ProductInOrder)
				pios.add((ProductInOrder)object);
		}
		ord.setProducts(pios);
		return ord;
	}
}