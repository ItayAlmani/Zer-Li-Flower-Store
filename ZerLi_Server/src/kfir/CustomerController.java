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
	
	public void getCustomerByPrivateID(String privateID) throws IOException {
				
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
					//getCreCardOfPayAcc(pa);
				}
			}
		}
	}
	
	private void getCreCardOfPayAcc(PaymentAccount pa) throws SQLException {
		ArrayList<Object> ccs = EchoServer.fac.creditCard.getCreditCard(pa.getCreditCard().getCcID());
		if(ccs!=null && ccs.size()==1 && ccs.get(0) instanceof CreditCard) {
			CreditCard cc = (CreditCard)ccs.get(0);
			pa.setCreditCard(cc);
		}
	}
	
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}