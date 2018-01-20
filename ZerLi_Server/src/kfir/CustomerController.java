package kfir;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import common.EchoServer;
import controllers.ParentController;
import entities.CreditCard;
import entities.Customer;
import entities.PaymentAccount;
import entities.StoreWorker;
import entities.User;

public class CustomerController extends ParentController {
	
	public ArrayList<Object> getCustomerByUser(BigInteger userID) throws Exception {
		String query = "SELECT * FROM customer WHERE userID='"+userID+"'";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	public ArrayList<Object> getCustomerByID(BigInteger customerID) throws Exception {
		String query = "SELECT * FROM customer WHERE customerID='"+customerID+"'";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	public boolean billCreditCardOfCustomer(Customer customer, float amount) {
		//return billCard(customer.getPaymentAccount().getCreditCard(), amount);
		return new Random().nextBoolean();
	}

	//Suppose to be external function - the billing is external
	public boolean billCard(CreditCard cc, float amount) {
		return new Random().nextBoolean();
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

	public Customer parse(BigInteger customerID, User user) {
		return new Customer(user, customerID);
	}

	public ArrayList<Object> getAllCustomers() throws Exception {
		String query = "SELECT * FROM customer";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	private ArrayList<Object> getAllCustomersData(ArrayList<Object> customers) throws Exception {
		for (Object object : customers) {
			if(object instanceof Customer) {
				Customer cust = (Customer)object;
				getUsersByCustomer(cust);
			}
		}
		return customers;
	}

	private void getUsersByCustomer(Customer cust) throws Exception{
		ArrayList<Object> users = EchoServer.fac.user.getUser(cust.getUserID());
		if(users!=null && users.size()==1 && users.get(0) instanceof User) {
			User user = (User)users.get(0);
			cust.setUser(user);
			getPayAccOfCustomer(cust);
		}
	}
	
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
		if(arr!=null && (arr.get(0) instanceof BigInteger == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		BigInteger userID = BigInteger.valueOf((Integer) arr.get(0));
		boolean isReturnNextID = (boolean)arr.get(1);
		String query = String.format(
				"INSERT INTO customer (userID)"
				+ " VALUES ('%d')",
				userID);
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
	
	public ArrayList<Object> delete(Customer cust) throws Exception{
		String query=String.format("DELETE FROM customer"
				+ " WHERE userID='%d'",
				cust.getUserID());
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		myMsgArr.add(true);
		return myMsgArr;		
	}
}