package usersInfo;

import java.math.BigInteger;
import java.util.ArrayList;

import common.EchoServer;
import common.ParentController;
import usersInfo.entities.Customer;
import usersInfo.entities.PaymentAccount;
import usersInfo.entities.User;
import usersInfo.interfaces.ICustomer;

public class CustomerController extends ParentController implements ICustomer {
	
	@Override
	public ArrayList<Object> getCustomerByUser(BigInteger userID) throws Exception {
		String query = "SELECT * FROM customer WHERE userID='"+userID+"'";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	@Override
	public ArrayList<Object> getAllCustomersOfStore(BigInteger storeID) throws Exception {
		String query = String.format("SELECT c.*\n" + 
				"FROM customer AS c, paymentaccount AS p\n" + 
				"WHERE p.customerID=c.customerID AND p.storeID='%d'", storeID);
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	@Override
	public ArrayList<Object> getCustomerByID(BigInteger customerID) throws Exception {
		String query = "SELECT * FROM customer WHERE customerID='"+customerID+"'";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	@Override
	public ArrayList<Object> getCustomerByPrivateID(String privateID) throws Exception {
		String query = "SELECT * FROM customer WHERE privateID='"+privateID+"'";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return null;
		ArrayList<Object> customers = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 2) {
			User u = new User(BigInteger.valueOf((Integer) obj.get(i+1)));
			customers.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)), 
					u
					));
		}
		return customers;
	}

	@Override
	public Customer parse(BigInteger customerID, User user) {
		return new Customer(user, customerID);
	}

	@Override
	public ArrayList<Object> getAllCustomers() throws Exception {
		String query = "SELECT * FROM customer";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	/**
	 * get all {@link User} data for the given {@link Customer}s in the {@link ArrayList}
	 * @param customers - {@link ArrayList} of {@link Customer}s
	 * @return - {@link ArrayList} of {@link Customer} with all {@link User} data
	 * @throws Exception
	 */
	private ArrayList<Object> getAllCustomersData(ArrayList<Object> customers) throws Exception {
		for (Object object : customers) {
			if(object instanceof Customer) {
				Customer cust = (Customer)object;
				getUsersByCustomer(cust);
			}
		}
		return customers;
	}

	/**
	 * 
	 * @param cust
	 * @throws Exception
	 */
	private void getUsersByCustomer(Customer cust) throws Exception{
		ArrayList<Object> users = EchoServer.fac.user.getUser(cust.getUserID());
		if(users!=null && users.size()==1 && users.get(0) instanceof User) {
			User user = (User)users.get(0);
			cust.setUser(user);
			getPayAccOfCustomer(cust);
		}
	}
	
	/**
	 * ask from DataBase to select the {@link PaymentAccount} of the given {@link Customer}
	 * @param cust
	 * @throws Exception
	 */
	private void getPayAccOfCustomer(Customer cust) throws Exception{
		ArrayList<Object> pas = EchoServer.fac.paymentAccount.getPayAccount(cust.getCustomerID());
		if(pas!=null && pas.isEmpty()==false) {
			for (Object o : pas) {
				if(o instanceof PaymentAccount) {
					PaymentAccount pa = (PaymentAccount)o;
					cust.addPaymentAccount(pa);
				}
			}
		}
	}
	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Customer == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Customer cust = (Customer) arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String query = String.format(
				"INSERT INTO customer (userID)"
				+ " VALUES ('%d')",
				cust.getUserID());
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		if(isReturnNextID) {
			query="SELECT Max(customerID) from customer";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer)
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}

	/**Dummy function*/
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}