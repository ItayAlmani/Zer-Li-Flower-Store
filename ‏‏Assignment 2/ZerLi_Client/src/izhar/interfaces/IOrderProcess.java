package izhar.interfaces;

import java.util.ArrayList;

import entities.Order;
import interfaces.IParent;

public interface IOrderProcess {

	/**
	 * 
	 * @param order
	 */
	String updateAfterCancellation(Order order);

	/**
	 * 
	 * @param order
	 */
	void updateFinilizeOrder(Order order);
}