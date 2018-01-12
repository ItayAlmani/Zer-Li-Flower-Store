package izhar.interfaces;

import java.io.IOException;
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
	 * @throws IOException 
	 */
	void updateFinilizeOrder(Order order) throws IOException;
}