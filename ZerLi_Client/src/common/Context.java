package common;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import common.gui.controllers.ParentGUIController;
import orderNproducts.entities.Order;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Store;
import orderNproducts.entities.Order.OrderStatus;
import orderNproducts.entities.Order.OrderType;
import orderNproducts.gui.controllers.ManualTransactionGUIController;
import usersInfo.entities.Customer;
import usersInfo.entities.StoreWorker;
import usersInfo.entities.User;
import usersInfo.entities.User.UserType;
import usersInfo.gui.controllers.LogInGUIController;

/** The class which holds the data of each client. 
 * Static class which will help to hold the common data that
 * more than one class will use
*/
public class Context {
	
	/** Each client has <b>one</b> <code>ClientConsole</code> */
	public static ClientConsole clientConsole = null;
	
	/** if controller want to ask from another controller some data,
	 * the controller will add itself.<br>
	 * Now, when the server will return the wanted data, {@link ClientController} will know
	 * how asked the data, and will send it to it by the askingCtrl, which will build as
	 * queue. */
	public static ArrayList<Object> askingCtrl = new ArrayList<>();
	
	/** Because we don't need several different instances of the same controller,
	 * but we don't want them to be static (for privacy matters),
	 * we will construct one instance of each by the {@link Factory} class. */
	public static Factory fac = new Factory();
	
	/** will save data of current {@link User} */
	private static User user = null;
	
	/** will save data for current {@link Order} if exist */
	public static Order order = null;
	
	/** will indicates if new order asked (for next ID), or the current order's id */
	private static boolean needNewOrder = true;
	
	/** the controller of the main scene, which include menu and content */
	public static ParentGUIController mainScene = null;
	/**
	 * getter for {@link #user}
	 * @return {@link #user}
	 */
	public static User getUser() {
		return user;
	}
	
	/**
	 * {@link User#getPermissions()} will indicate the current {@link User}'s permission.<br>
	 * Because we have also {@link Customer}s and {@link StoreWorker}s as entities,
	 * if the permission equals to Customer or StoreWorker, we will save {@link #user} as
	 * {@link Customer} or {@link StoreWorker}, which inherits from {@link User}
	 * @param newuser the new {@link User} that connected
	 */
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
	
	/**
	 * The answer from Server, after asking {@link StoreWorker}/s
	 * @param storeWorkers the {@link StoreWorker}/s asked from Server
	 */
	public static void setStoreWorkers(ArrayList<StoreWorker> storeWorkers) {
		if(storeWorkers.size()!=0) {
			StoreWorker sw = storeWorkers.get(0);
			sw.setUser(user);
			user=sw;
		}
		if(ParentGUIController.currentGUI instanceof LogInGUIController)
			((LogInGUIController)ParentGUIController.currentGUI).setUserConnected(Context.user);
	}
	
	/**
	 * if {@link #user} is {@link INSTANCEOF} {@link Customer}, return {@link #user} as {@link Customer} 
	 * @return {@link #user} as {@link Customer}
	 * @throws Exception if {@link #user} isn't {@link INSTANCEOF} {@link Customer}
	 */
	public static Customer getUserAsCustomer() throws Exception{
		if(user instanceof Customer)
			return (Customer)user;
		throw new Exception("User isn't customer");
	}

	/**
	 * if {@link #user} is {@link INSTANCEOF} {@link StoreWorker}, return {@link #user} as {@link StoreWorker} 
	 * @return {@link #user} as {@link StoreWorker}
	 * @throws Exception if {@link #user} isn't {@link INSTANCEOF} {@link StoreWorker}
	 */
	public static StoreWorker getUserAsStoreWorker() throws Exception {
		if(user instanceof StoreWorker)
			return (StoreWorker)user;
		throw new Exception("User isn't Store Worker or Store Manager");
	}
	
	/**
	 * ask an InProcess {@link Order} for current {@link Customer}
	 * @param store where the {@link Order} being made
	 */
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
	
	/**
	 * ask an InProcess {@link Order} for {@link ManualTransactionGUIController} - by {@link StoreWorker}
	 * @param customerID the {@link Customer#getCustomerID()} which the order's is his/her
	 * @param store where the {@link Order} being made
	 */
	public static void askOrder(BigInteger customerID, Store store) {
		if(user instanceof StoreWorker) {
			try {
				askingCtrl.add(Context.class.newInstance());
				fac.order.getOrAddOrderInProcess(customerID, store);
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				System.err.println("getOrderInProcess() not working in Context\n");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The answer from Server, after asking {@link Customer}/s
	 * @param customers the {@link Customer}/s asked from Server
	 */
	public static void setCustomers(ArrayList<Customer> customers) {
		if(customers.size()!=0) {
			Customer cust = customers.get(0);
			cust.setUser(user);
			user=cust;
		}
		if(ParentGUIController.currentGUI instanceof LogInGUIController)
			((LogInGUIController)ParentGUIController.currentGUI).setUserConnected(Context.user);
	}
	
	/**
	 * The answer from Server, after asking {@link Order}/s
	 * @param orders the {@link Order}/s asked from Server
	 */
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
	
	/**
	 * The answer from Server, after asking {@link ProductInOrder}/s
	 * @param prds the {@link ProductInOrder}/s asked from Server
	 */
	public static void setPIOs(ArrayList<ProductInOrder> prds) {
		if(order!=null) {
			order.setProducts(prds);
		}
	}

	/**
	 * The answer from Server, after asking to add an {@link Order}
	 * @param id the new {@link Order}'s id, which added by the Server
	 */
	public static void setOrderID(BigInteger id) {
		if(needNewOrder==true)
			Context.order.setOrderID(id.subtract(BigInteger.ONE));
		else
			Context.order.setOrderID(id);
	}
}