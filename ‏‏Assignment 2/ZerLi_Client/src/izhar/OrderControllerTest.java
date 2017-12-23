package izhar;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.ShoppingCart;
import entities.Transaction;
import enums.DeliveryType;
import enums.OrderStatus;
import enums.OrderType;
import enums.UserType;

public class OrderControllerTest {

	private Customer cst;
	private ShoppingCart cart;
	private DeliveryDetails delivery;
	private Transaction transaction;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testAddOrder() {
		Date today = new Date();
		today.setHours(0);
		try {
			Context.fac.order.addOrder(new Order(3,
					new Customer("izhar", "ananiev", "izhari6", "1234", UserType.ChainStoreManager, 1),
					new ShoppingCart(1), 
					new DeliveryDetails(1),
					OrderType.InfoSystem,
					new Transaction(1), 
					"Fuck Off!",
					DeliveryType.Pickup, 
					OrderStatus.InProcess, 
					today));
		} catch (IOException e) {
			System.err.println("testAddOrder not working!");
			e.printStackTrace();
		}
	}

}
