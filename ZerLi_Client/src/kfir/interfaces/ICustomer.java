package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.CreditCard;
import entities.Customer;
import entities.Order;
import entities.PaymentAccount;
import entities.Subscription;
import entities.User;
import entities.Order.Refund;
import entities.Subscription.SubscriptionType;
import interfaces.IParent;

public interface ICustomer{

	/**
	 * asks from db all customers with all User details - names, privateID...
	 * @throws IOException 
	 */
	void getAllCustomers() throws IOException;


	void getCustomerByUser(BigInteger userID) throws IOException;

	/**
	 * 
	 * @param customer
	 */
	boolean billCreditCardOfCustomer(Customer customer, float amount);
}