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
	
	public ArrayList<Object> getCustomerByUser(BigInteger userID) throws SQLException {
		String query = "SELECT * FROM customer WHERE userID='"+userID+"'";
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

	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return null;
		ArrayList<Object> customers = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 3) {
			User u = new User(BigInteger.valueOf((Integer) obj.get(i+1)));
			customers.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)), 
					u,
					new PaymentAccount(BigInteger.valueOf((Integer) obj.get(i+2)))
					));
		}
		return customers;
	}

	public Customer parse(BigInteger customerID, User user, PaymentAccount pa) {
		return new Customer(user, customerID, pa);
	}
	
	public void getCustomerByPrivateID(String privateID) throws IOException {
				
	}

	public ArrayList<Object> getAllCustomers() throws SQLException {
		String query = "SELECT * FROM customer";
		return getAllCustomersData(handleGet(EchoServer.fac.dataBase.db.getQuery(query)));
	}
	
	private ArrayList<Object> getAllCustomersData(ArrayList<Object> customers) throws SQLException {
		for (Object object : customers) {
			if(object instanceof Customer) {
				Customer cust = (Customer)object;
				getUsersByCustomer(cust);
			}
		}
		return customers;
	}

	private void getUsersByCustomer(Customer cust) throws SQLException{
		ArrayList<Object> users = EchoServer.fac.user.getUser(cust.getUserID());
		if(users!=null && users.size()==1 && users.get(0) instanceof User) {
			User user = (User)users.get(0);
			cust.setUser(user);
			getPayAccOfCustomer(cust);
		}
	}
	
	private void getPayAccOfCustomer(Customer cust) throws SQLException{
		ArrayList<Object> pas = EchoServer.fac.paymentAccount.getPayAccount(cust.getCustomerID());
		if(pas!=null && pas.size()==1 && pas.get(0) instanceof PaymentAccount) {
			PaymentAccount pa = (PaymentAccount)pas.get(0);
			cust.setPaymentAccount(pa);
			getCreCardOfPayAcc(pa);
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