package common;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.Customer;
import entities.Order;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.ProductInOrder;
import entities.Store;
import entities.StoreWorker;
import entities.User;
import entities.User.UserType;
import gui.controllers.ParentGUIController;
import kfir.gui.controllers.LogInGUIController;

/** The class which holds the data of each client. 
 * Static class which will help to hold the common data that
 * more than one class will use
*/
public class Context {
	
	/** Each client has <b>one</b> <code>ClientConsole</code> */
	public static ClientConsole clientConsole = null;
	
	public static ArrayList<Object> askingCtrl = new ArrayList<>();
	
	public static Factory fac = new Factory();
	
	private static User user = null;
	public static Order order = null;
	private static boolean needNewOrder = true;
	public static ParentGUIController mainScene = null;
	
	public static User getUser() {
		return user;
	}

	public static void setUser(User newuser) {
		try {
			user = newuser;
			user.setConnected(true);			
			if(user.getPermissions().equals(UserType.Customer)) {
					askingCtrl.add(Context.class.newInstance());
					fac.customer.getCustomerByUser(user.getUserID());
			}
			else if(user.getPermissions().equals(UserType.StoreWorker)||
					user.getPermissions().equals(UserType.StoreManager)) {
				askingCtrl.add(Context.class.newInstance());
				Context.fac.storeWorker.getStoreWorkerByUser(user.getUserID());
			} //all other user types
			else if(ParentGUIController.currentGUI instanceof LogInGUIController)
				((LogInGUIController)ParentGUIController.currentGUI).setUserConnected(Context.user);
				
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setStoreWorkers(ArrayList<StoreWorker> storeWorkers) {
		if(storeWorkers.size()!=0) {
			StoreWorker sw = storeWorkers.get(0);
			sw.setUser(user);
			user=sw;
		}
		if(ParentGUIController.currentGUI instanceof LogInGUIController)
			((LogInGUIController)ParentGUIController.currentGUI).setUserConnected(Context.user);
	}
	
	public static Customer getUserAsCustomer() throws Exception{
		if(user instanceof Customer)
			return (Customer)user;
		throw new Exception("User isn't customer");
	}

	public static StoreWorker getUserAsStoreWorker() throws Exception {
		if(user instanceof StoreWorker)
			return (StoreWorker)user;
		throw new Exception("User isn't Store Worker or Store Manager");
	}
	
	public static void askOrder(Store store) {
		if(user instanceof Customer) {
			try {
				askingCtrl.add(Context.class.newInstance());
				fac.order.getOrAddOrderInProcess(((Customer)user).getCustomerID(), store);
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				System.err.println("getOrderInProcess() not working in Context\n");
				e.printStackTrace();
			}
		}
	}
	
	public static void setCustomers(ArrayList<Customer> customers) {
		if(customers.size()!=0) {
			Customer cust = customers.get(0);
			cust.setUser(user);
			user=cust;
		}
		if(ParentGUIController.currentGUI instanceof LogInGUIController)
			((LogInGUIController)ParentGUIController.currentGUI).setUserConnected(Context.user);
	}
	
	public static void setOrders(ArrayList<Order> orders) {
		if(orders!=null && orders.size()!=0 && orders.get(0)!=null) {
			order = orders.get(0);
			needNewOrder=false;
			try {
				askingCtrl.add(Context.class.newInstance());
				if(order != null)
					fac.prodInOrder.getPIOsByOrder(order.getOrderID());
				else throw new Exception("order==null");
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				System.err.println("View Catalog");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(user instanceof Customer) {
			try {
				needNewOrder=true;
				askingCtrl.add(Context.class.newInstance());
				order = new Order(Context.getUserAsCustomer().getCustomerID(),
						OrderType.InfoSystem,
						OrderStatus.InProcess);
				fac.order.add(order,true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setPIOs(ArrayList<ProductInOrder> prds) {
		if(order!=null) {
			order.setProducts(prds);
		}
	}

	public static void setOrderID(BigInteger id) {
		if(needNewOrder==true)
			Context.order.setOrderID(id.subtract(BigInteger.ONE));
		else
			Context.order.setOrderID(id);
	}
}