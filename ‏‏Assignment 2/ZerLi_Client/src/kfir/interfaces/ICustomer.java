package kfir.interfaces;

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
	void getCustomer(int customerID);

	void getCustomerByUser(int userID);

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
	boolean billCreditCardOfCustomer(Customer customer);

	/**
	 * 
	 * @param customerID
	 * @param refundAmount
	 */
	void updateRefundAccount(String customerID, String refundAmount);

	Customer parse(int customerID, User user, PaymentAccount pa);
	
	void sendCustomers(ArrayList<Customer> customers);
}