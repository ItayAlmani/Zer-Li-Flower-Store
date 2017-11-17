package actors.Users;

import java.util.Date;
import actors.CreditCard;

public class Customer extends User {
	private String cusID;			//The private ID of the person
	private CreditCard cusCard;
	
	public Customer() {
		super(UserType.Customer);
	}
	
	/**
	 * Customer without Payment Account
	 * @param uFName	- user First Name
	 * @param uLName	- user Last Name
	 * @param uUName	- user User Name
	 * @param uPassword	- user Password
	 */
	public Customer(String uFName, String uLName, String uUName, String uPassword) {
		super(uFName, uLName, uUName, uPassword, UserType.Customer);
	}

	/**
	 * Customer with Payment Account - CreditCard object sent
	 * @param uFName	- user First Name
	 * @param uLName	- user Last Name
	 * @param uUName	- user User Name
	 * @param uPassword	- user Password
	 * @param cID		- customer Personal ID
	 * @param cCard		- customer Card Details
	 */
	public Customer(String uFName, String uLName, String uUName, String uPassword,
			String cID, CreditCard cCard) {
		super(uFName, uLName, uUName, uPassword, UserType.Customer);
		this.cusID = cID;
		this.cusCard = cCard;
	}
	
	/**
	 * Customer with Payment Account - Credit card details sent
	 * @param uFName		- user First Name
	 * @param uLName		- user Last Name
	 * @param uUName		- user User Name
	 * @param uPassword		- user Password
	 * @param cID			- customer Personal ID
	 * @param ccNumber		- customer credit card number
	 * @param ccValidity	- customer credit card validity
	 * @param ccCVV			- customer credit card CCV
	 */
	public Customer(String uFName, String uLName, String uUName, String uPassword,
			String cID, String ccNumber, Date ccValidity, String ccCVV) {
		super(uFName, uLName, uUName, uPassword, UserType.Customer);
		this.cusID = cID;
		this.cusCard = new CreditCard(ccNumber, ccValidity, ccCVV);
	}	
}
