package interfaces;

import entities.Order;

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