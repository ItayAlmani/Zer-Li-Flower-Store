package kfir;

import entities.*;
import enums.Refund;
import enums.SubscriptionType;

public class CustomerController extends UserController {

	/**
	 * 
	 * @param customer
	 */
	public PaymentAccount getPaymentAccount(Customer customer) {
		// TODO - implement CustomerController.getPaymentAccount
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param customerID
	 */
	public CreditCard getCreditCard(String customerID) {
		// TODO - implement CustomerController.getCreditCard
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param order
	 * @param refund
	 */
	public boolean updateRefundedOrderPrice(Order order, Refund refund) {
		// TODO - implement CustomerController.updateRefundedOrderPrice
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param customer
	 */
	public void updatePaymentAccountInDB(Customer customer) {
		// TODO - implement CustomerController.updatePaymentAccountInDB
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param customerID
	 */
	public Customer getCustomer(String customerID) {
		// TODO - implement CustomerController.getCustomer
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param customerID
	 * @param account
	 */
	public void SetPaymentAccount(String customerID, PaymentAccount account) {
		// TODO - implement CustomerController.SetPaymentAccount
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param account
	 */
	public Subscription getSubscription(PaymentAccount account) {
		// TODO - implement CustomerController.getSubscription
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param account
	 * @param type
	 */
	public Subscription CreateSubscription(PaymentAccount account, SubscriptionType type) {
		// TODO - implement CustomerController.CreateSubscription
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param account
	 */
	public void setSubscription(PaymentAccount account) {
		// TODO - implement CustomerController.setSubscription
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param customer
	 */
	public boolean billCreditCardOfCustomer(Customer customer) {
		// TODO - implement CustomerController.billCreditCardOfCustomer
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param customerID
	 * @param refundAmount
	 */
	public void updateRefundAccount(String customerID, String refundAmount) {
		// TODO - implement CustomerController.updateRefundAccount
		throw new UnsupportedOperationException();
	}

}