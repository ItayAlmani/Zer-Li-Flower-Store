package kfir.interfaces;

import entities.Customer;
import interfaces.IParent;

public interface IAdmin extends IParent{

	void UpdateUsers();

	/**
	 * 
	 * @param customer - sads
	 */
	void ChangePermission(Customer customer);

}