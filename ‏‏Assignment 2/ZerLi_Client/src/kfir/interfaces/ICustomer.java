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

public interface ICustomer extends IParent {

	/**
	 * 
	 * @param customer
	 */
	PaymentAccount getPaymentAccount(Customer customer);

	/**
	 * 
	 * @param customerID
	 */
	CreditCard getCreditCard(String customerID);

	/**
	 * 
	 * @param order
	 * @param refund
	 */
	boolean updateRefundedOrderPrice(Order order, Refund refund);

	/**
	 * 
	 * @param customer
	 */
	void updatePaymentAccountInDB(Customer customer);

	/**
	 * 
	 * @param customerID
	 */
	void getCustomer(BigInteger customerID);

	void getCustomerByUser(BigInteger userID);
	
	void getCustomerByPrivateID(String privateID) throws IOException;

	/**
	 * 
	 * @param customerID
	 * @param account
	 */
	void SetPaymentAccount(String customerID, PaymentAccount account);

	/**
	 * 
	 * @param account
	 */
	Subscription getSubscription(PaymentAccount account);

	/**
	 * 
	 * @param account
	 * @param type
	 */
	Subscription CreateSubscription(PaymentAccount account, SubscriptionType type);

	/**
	 * 
	 * @param account
	 */
	void setSubscription(PaymentAccount account);

	/**
	 * 
	 * @param customer
	 */
	boolean billCreditCardOfCustomer(Customer customer, float amount);

	/**
	 * 
	 * @param customerID
	 * @param refundAmount
	 */
	void updateRefundAccount(String customerID, String refundAmount);

	Customer parse(BigInteger customerID, User user, PaymentAccount pa);
	
	void sendCustomers(ArrayList<Customer> customers);
}