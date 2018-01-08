package common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import entities.Customer;
import entities.Order;
import entities.ProductInOrder;
import entities.Store;
import entities.StoreWorker;
import entities.User;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.User.UserType;
import gui.controllers.MainMenuGUIController;
import gui.controllers.ParentGUIController;
import javafx.stage.Stage;
import kfir.gui.controllers.LogInGUIController;

/** The class which holds the data of each client. 
 * Static class which will help to hold the common data that
 * more than one class will use
*/
public class Context {
	
	/** The default port of the server */
	public static int DEFAULT_PORT = 5555;
	
	/** The default host of the server */
	public static String DEFAULT_HOST="localhost";
	
	public static boolean dbConnected = false;
	
	/** The path of the project: "C:.../ZerLi_Client" */
	//public final static String projectPath=System.getProperty("user.dir");
	
	/** The path of the ServerAddress.txt file - the file
	 * that contains the server's details: host and port */
	private final static String serTxtPath="/common/ServerAddress.txt";
	
	/** Each client has <b>one</b> <code>ClientConsole</code> */
	public static ClientConsole clientConsole = null;
	
	public static ArrayList<Object> askingCtrl = new ArrayList<>();
	
	public static Factory fac = new Factory();
	
	private static User user;
	public static Order order;
	private static boolean needNewOrder=true;
	public static ParentGUIController mainScene = null;
	
	/**
	 * Looking for the .txt file at <code>projectPath</code>+<code>serTxtPath</code> path,
	 * and will take the data from there.
	 * If not found or invalid data, the system will try to connect by
	 * <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * @throws IOException - when the data in the .txt file is wrong, or the file couldn't be found
	 */
	public static void connectToServer() throws IOException{
		int serSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			Scanner scnr = null;
			scnr = new Scanner(Context.class.getResourceAsStream(serTxtPath));
			scnr.useDelimiter("\\w");
			String[] args = new String[2];
			for(int i = 0;i<2 && scnr.hasNextLine();i++) {
				String[] tempSplit = scnr.nextLine().split("\\W+");
				args[i]= tempSplit[tempSplit.length-1];
			}
			scnr.close();
			clientConsole = new ClientConsole(args[0],Integer.parseInt(args[1]));
			
			/*connection succeeded, default data will be changed in .txt file and in default attributes*/
			serSuccessFlag = 1;
			DEFAULT_HOST = args[0];
			DEFAULT_PORT=Integer.parseInt(args[1]);
			writeNewServerDataIntoTxt();
		} catch (IOException e) {
			System.err.println("Context->ServerAddress.txt data is corrupted or Can't find txt file at "+serTxtPath+".\n");
		}
		if(serSuccessFlag==0) {	//db data corrupted 
			try {
				clientConsole = new ClientConsole(DEFAULT_HOST,DEFAULT_PORT);
			} catch (IOException e) {
				System.err.println("Context->Default Server data is wrong!\n");
				throw e;
			}
		}
	}

	/**
	 * Trying to connect into server at (<code>host</code>,<code>port</code>).
	 * If succeeded will change the .txt file at <code>projectPath</code>+<code>serTxtPath</code> path.
	 * If not, the system will try to connect by <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * @param host - new <code>DEFAULT_HOST</code> of the Server trying to connect to
	 * @param port - new <code>DEFAULT_PORT</code> of the Server trying to connect to
	 * @throws IOException - when the data in the .txt file is wrong, or the file couldn't be found
	 */
	public static void connectToServer(String host, int port) throws IOException{
		int serSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			clientConsole = new ClientConsole(host,port);
			DEFAULT_HOST = host;
			DEFAULT_PORT=port;
			writeNewServerDataIntoTxt();
			serSuccessFlag = 1;
		} catch (IOException e) {
			System.err.println("\nServerAddress.txt data is corrupted or Can't find txt file at "+serTxtPath+".");
			System.err.println("Go to Context for the process\n");
		}
		
		if(serSuccessFlag==0) {	//db data corrupted 
			try {
				clientConsole = new ClientConsole(DEFAULT_HOST,DEFAULT_PORT);
				throw new IOException();
			} catch (IOException e) {
				System.err.println("\nDefault Server data is wrong!\nGo to Context to fix it!\n");
				throw e;
			}
		}
	}

	/**
	 * Will flush all data in .txt file at <code>projectPath</code>+<code>serTxtPath</code>,
	 * and will write the new Server data: <code>DEFAULT_HOST</code> and <code>DEFAULT_PORT</code>.
	 * @throws IOException - will be thrown when the .txt file not found
	 */
	private static void writeNewServerDataIntoTxt() throws IOException {
		File f = null;
		try {
			f = new File(Context.class.getResource(serTxtPath).toURI());
		} catch (URISyntaxException e) {
			f.createNewFile();
			e.printStackTrace();
		}
		if (f.exists() == false) //Create a new file if doesn't exists yet
			f.createNewFile();
		PrintStream output = new PrintStream(f);
		output.flush();//flush whole txt file
		output.println("Host: "+DEFAULT_HOST);
		output.println("Port: "+DEFAULT_PORT);
		output.close();
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User newuser) {
		try {
			user = newuser;
			if(user.getPermissions().equals(UserType.Customer)) {
					askingCtrl.add(Context.class.newInstance());
					fac.customer.getCustomerByUser(user.getUserID());
			}
			else if(user.getPermissions().equals(UserType.StoreWorker)||
					user.getPermissions().equals(UserType.StoreManager)) {
				/*askingCtrl.add(Context.class.newInstance());
				Context.fac.storeWorker.getStoreWorkerByUser(user.getUserID());*/
				
				//JUST FOR TEST - MUST DELETE!!!
				StoreWorker sw = new StoreWorker(user, BigInteger.ONE, new Store(BigInteger.ONE));
				ArrayList<StoreWorker> sws = new ArrayList<>();
				sws.add(sw);
				setStoreWorkers(sws);
			} //all other user types
			else if(ParentGUIController.currentGUI instanceof LogInGUIController)
				((LogInGUIController)ParentGUIController.currentGUI).setUserConnected(Context.user);
				
		} catch (InstantiationException | IllegalAccessException e) {
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
	
	public static Customer getUserAsCustomer() {
		if(user instanceof Customer)
			return (Customer)user;
		return null;
	}

	public static StoreWorker getUserAsStoreWorker() {
		if(user instanceof StoreWorker)
			return (StoreWorker)user;
		return null;
	}
	
	public static void askOrder() {
		if(user instanceof Customer) {
			try {
				askingCtrl.add(Context.class.newInstance());
				fac.order.getOrderInProcess(((Customer)user).getCustomerID());
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
				fac.order.add(order);
			} catch (IOException | InstantiationException | IllegalAccessException e) {
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
