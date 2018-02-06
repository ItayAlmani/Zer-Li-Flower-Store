package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import common.Context;
import orderNproducts.entities.DeliveryDetails;
import orderNproducts.entities.Order;
import orderNproducts.entities.Order.OrderStatus;
import orderNproducts.entities.Order.OrderType;
import orderNproducts.entities.Order.PayMethod;
import orderNproducts.entities.Store;
import usersInfo.entities.CreditCard;
import usersInfo.entities.Customer;
import usersInfo.entities.PaymentAccount;
import usersInfo.entities.Subscription;
import usersInfo.entities.Subscription.SubscriptionType;
import usersInfo.entities.User;
import usersInfo.entities.User.UserType;

public class CancelOrderTest {
	
	/** the ArrayList of orders for the check */
	private static ArrayList<Order> ord = new ArrayList<>();
	
	/** the ArrayList of payment accounts with the refunds for the check */
	private static ArrayList<PaymentAccount> pa = new ArrayList<>();
	
	private static Customer cust;
	private static Store store;
	
	/** the 3 possible messages which return from the cancel order function */
	private final static String[] msgExpected = new String[]{"Account is fully refunded for this order","Account refunded for 50% of this order", "Account isn't refunded"};
	
	@BeforeClass
	public static void setUpBeforeClass() {
		/** Stub that mimic the data base/server function which return the cancelable orders*/
		CancelOrderTest cot = new CancelOrderTest();
		StubOrderController ordCtrl = cot.new StubOrderController();
		
		//ask for the orders
		ord=ordCtrl.getCancelableOrdersByCustomerID(BigInteger.ONE);
	}	

	@Test
	public void CancelOrder_FullRefund_Success() throws Exception {
		int i = 0;
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		String msgActual = Context.fac.order.cancelOrder(ord.get(i), cust, arrForBool, pa.get(i));
		Boolean isRefundedActual = arrForBool.get(0);
		assertEquals(msgExpected[0], msgActual);
		assertEquals(true, isRefundedActual);
		assertEquals(100f, pa.get(i).getRefundAmount(),0.01f);
	}
	
	@Test
	public void CancelOrder_PartialRefund_Success() throws Exception {
		int i = 1;
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		String msgActual = Context.fac.order.cancelOrder(ord.get(i), cust, arrForBool, pa.get(i));
		Boolean isRefundedActual = arrForBool.get(0);
		assertEquals(msgExpected[1], msgActual);
		assertEquals(true, isRefundedActual);
		assertEquals(50f, pa.get(i).getRefundAmount(),0.01f);
	}
	
	@Test
	public void CancelOrder_NoRefund_Success() throws Exception {
		int i = 2;
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		String msgActual = Context.fac.order.cancelOrder(ord.get(i), cust, arrForBool, pa.get(i));
		Boolean isRefundedActual = arrForBool.get(0);
		assertEquals(msgExpected[2], msgActual);
		assertEquals(false, isRefundedActual);
		assertEquals(0f, pa.get(i).getRefundAmount(),0.01f);
	}
	
	@Test
	public void CancelOrder_FullRefund_BoundryValue_Success() throws Exception {
		int i = 3;
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		String msgActual = Context.fac.order.cancelOrder(ord.get(i), cust, arrForBool, pa.get(i));
		Boolean isRefundedActual = arrForBool.get(0);
		assertEquals(msgExpected[0], msgActual);
		assertEquals(true, isRefundedActual);
		assertEquals(100f, pa.get(i).getRefundAmount(),0.01f);
	}
	
	@Test
	public void CancelOrder_PartialRefund_BoundryValue_Success() throws Exception {
		int i = 4;
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		String msgActual = Context.fac.order.cancelOrder(ord.get(i), cust, arrForBool, pa.get(i));
		Boolean isRefundedActual = arrForBool.get(0);
		assertEquals(msgExpected[1], msgActual);
		assertEquals(true, isRefundedActual);
		assertEquals(50f, pa.get(i).getRefundAmount(),0.01f);
	}
	
	@Test
	public void CancelOrder_NoRefund_BoundryValue_Success() throws Exception {
		int i = 5;
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		String msgActual = Context.fac.order.cancelOrder(ord.get(i), cust, arrForBool, pa.get(i));
		Boolean isRefundedActual = arrForBool.get(0);
		assertEquals(msgExpected[2], msgActual);
		assertEquals(false, isRefundedActual);
		assertEquals(0f, pa.get(i).getRefundAmount(),0.01f);
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void CancelOrder_OrderNull_Fail() throws Exception {
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		thrown.expect(NullPointerException.class);
	    thrown.expectMessage("All parameters must not be null");
		Context.fac.order.cancelOrder(null, new Customer(), arrForBool, new PaymentAccount());
		fail("Expected exception");
	}
	
	@Test(expected = NullPointerException.class)
	public void CancelOrder_CustomerNull_Fail() throws Exception {
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		Context.fac.order.cancelOrder(new Order(), null, arrForBool, new PaymentAccount());
		fail("Expected exception");
	}
	
	@Test(expected = NullPointerException.class)
	public void CancelOrder_PaymentAccountNull_Fail() throws Exception {
		//Because Boolean is not reference (pointer), we use ArrayList for it
		ArrayList<Boolean> arrForBool = new ArrayList<>();
		Context.fac.order.cancelOrder(new Order(), new Customer(), arrForBool, null);
		fail("Expected exception");
	}
	
	@AfterClass
	public static void tearDownAfterClass(){
		System.out.println("After tearDown of TestClass. Current time is "+LocalTime.now()+":");
		for (int i = 0; i < ord.size(); i++)
			System.out.println("\tOrder: Time = "+ ord.get(i).getDelivery().getDate().toLocalTime() +".\tPrice = "+ord.get(i).getFinalPriceAsString()+".\tRefund amount = " + pa.get(i).getRefundAmount());
	}
	
	/**
	 * We are using stub to mimic the behavior of the OrderController in the Server,
	 * and mimic the Data Base behavior
	 */
	private class StubOrderController{
		
		private ArrayList<Order> ordList = new ArrayList<>();
		private ArrayList<PaymentAccount> paList = new ArrayList<>();
		
		private LocalDateTime[] times = new LocalDateTime[] {
				LocalDateTime.now().plusHours(4),
				LocalDateTime.now().plusHours(2),
				LocalDateTime.now().plusMinutes(30),
				/** Boundry values */
				LocalDateTime.now().plusHours(3),
				LocalDateTime.now().plusHours(1),
				LocalDateTime.now().plusSeconds(1)
		};
		
		/**
		 * Mimic the function in the server which return all orders that can be canceled by CustomerID
		 * @param custID the customer's ID
		 * @return all the orders of the customer which can be canceled
		 */
		public ArrayList<Order> getCancelableOrdersByCustomerID(BigInteger custID) {
			setCustomerUp(custID);
			setPaymentAccountsUp();
			for (int i = 0; i < times.length; i++) {
				paList.add(new PaymentAccount());
				setOrdersUp(i);
			}
			pa=paList;
			return ordList;
		}
		
		/**
		 * creating the customer
		 * @param custID the customer ID
		 */
		private void setCustomerUp(BigInteger custID) {
			//--------------------------------------Dummy Objects--------------------------------------
			User user = new User(BigInteger.ONE, "314785270", "Izhar", "Ananiev", "izharan", "1234", UserType.Customer, true, true);
			cust=new Customer(user,custID);
		}
		
		/**
		 * create the payment accounts of the customer
		 */
		private void setPaymentAccountsUp() {
			ArrayList<PaymentAccount> pas = new ArrayList<>();
			//--------------------------------------Dummy Objects--------------------------------------
			CreditCard cc = new CreditCard(BigInteger.ONE,"4580010327364256", "10/30", "111");
			Subscription sub = new Subscription(BigInteger.ONE, SubscriptionType.Monthly, LocalDate.of(2018, 1, 25));
			store=new Store(BigInteger.ONE, "Karmiel");
			//----------------------------------------------------------------------------------------------
			PaymentAccount p = new PaymentAccount(BigInteger.ONE, cust.getCustomerID(), store, cc, sub, 0f);
			pas.add(p);
			
			cust.setPaymentAccounts(pas);
		}
		
		/**
		 * creating the different orders
		 */
		private void setOrdersUp(int ind) {
			ordList.add(new Order(BigInteger.ONE, cust.getCustomerID(), OrderType.InfoSystem, PayMethod.CreditCard, "", OrderStatus.Paid, LocalDateTime.now(), 100f));
			ordList.get(ordList.size()-1).setDelivery(new DeliveryDetails(BigInteger.ONE, times[ind], false, store));
		}
	}
}