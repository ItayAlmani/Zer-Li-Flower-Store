package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.CSMessage.MessageType;
import entities.Customer;
import entities.Product;
import entities.ProductInOrder;
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import izhar.interfaces.IProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrder {	
	
	public void updatePriceWithSubscription(ProductInOrder pio, Customer customer) {
		if(customer.getPaymentAccount()!= null && customer.getPaymentAccount().getSub() != null) {
			LocalDate date = customer.getPaymentAccount().getSub().getSubDate();
			SubscriptionType type = customer.getPaymentAccount().getSub().getSubType();
			if(type.equals(SubscriptionType.Monthly)) {
				if(date.plusMonths(1).isBefore(LocalDate.now()))
					pio.setFinalPrice(pio.getFinalPrice()*Subscription.getDiscountInPercent());
			}
			else if(type.equals(SubscriptionType.Yearly)) {
				if(date.plusYears(1).isBefore(LocalDate.now()))
					pio.setFinalPrice(pio.getFinalPrice()*Subscription.getDiscountInPercent());
			}
		}
	}
	
	@Override
	public void updatePIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE cart " + 
				" SET orderID=%d, productID=%d,quantity=%d,totalprice=%f" + 
				" WHERE productInOrderID=%d",
				p.getOrderID(),p.getProduct().getPrdID(),p.getQuantity(),p.getFinalPrice(),p.getId()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));
	}
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		if(obj==null) return;
		ArrayList<ProductInOrder> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 11) {
			try {
				prds.add(parse(BigInteger.valueOf(Long.valueOf((int)obj.get(i))),
						(Context.fac.product.parse(BigInteger.valueOf(Long.valueOf((int) obj.get(i+1))), 
						(String) obj.get(i + 2), 
						(String) obj.get(i + 3),
						(float) obj.get(i + 4),
						(String) obj.get(i + 5),
						((int)obj.get(i + 6))!= 0,
						(String)obj.get(i+7))
						),BigInteger.valueOf(Long.valueOf((int)obj.get(i+8)))
						,(int)obj.get(i+9),
						(float)obj.get(i+10)
						));
			} catch (FileNotFoundException e) {
				System.err.println("Couldn't find Image named "+ (String)obj.get(i+6) +".");
				e.printStackTrace();
			}
		}
		sendPIOs(prds);
	}
	
	@Override
	public ProductInOrder parse(BigInteger id, Product prod, BigInteger orderID, int quantity, float finalPrice) {
		return new ProductInOrder(id, prod,orderID,quantity,finalPrice);
	}
	
	@Override
	public void sendPIOs(ArrayList<ProductInOrder> prds) {
		String methodName = "setPIOs";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl.get(0), prds);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, prds);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	@Override
	public void addPIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,ProductInOrder.class));
	}
	
	public void handleInsert(BigInteger id) {
		String methodName = "setPIOID";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,BigInteger.class);
				m.invoke(Context.askingCtrl.get(0), id);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,BigInteger.class);
				m.invoke(Context.currentGUI, id);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
	
	public void deletePIO(ProductInOrder pio) throws IOException{
		myMsgArr.clear();
		String query = "delete from cart" + 
						" where orderID='"+pio.getOrderID()
						+"' AND productID='"+pio.getProduct().getPrdID()+"'";
		query += "SELECT Max(productInOrderID) from cart;";
		myMsgArr.add(query);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr,ProductInOrder.class));
	}

	@Override
	public void getPIOsByOrder(BigInteger orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT productInOrderID, prd.*,orderID, quantity, totalprice FROM" + 
				"(" + 
				"	SELECT ordCart.* FROM" + 
				"	(" + 
				"		SELECT crt.*" + 
				"		FROM cart AS crt" + 
				"		JOIN orders ON crt.orderID=orders.orderID" + 
				"		where crt.orderID = '"+orderID+"'" + 
				"	) AS ordCart" + 
				"	JOIN product ON ordCart.productID=product.productID" + 
				") AS prodInOrd, product prd" + 
				" WHERE prodInOrd.productID = prd.productID AND prodInOrd.quantity>0");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, ProductInOrder.class));
	}
	
	public void updatePriceOfPIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("update cart" + 
				" set quantity='"+p.getQuantity()+"', totalprice='"+p.getFinalPrice()+"'" + 
				" where orderID='"+p.getOrderID()+"' and productID='"+p.getProduct().getPrdID()+"'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr, ProductInOrder.class));
	}

	public float calcFinalPrice(ProductInOrder p) {
		return p.getQuantity()*p.getProduct().getPrice();
	}
	
	public boolean isAllPIOsFromSameOrder(ArrayList<ProductInOrder> products) {
		BigInteger ordID = products.get(0).getOrderID();
		for (ProductInOrder pio : products) {
			if(pio.getOrderID().equals(ordID)==false)
				return false;
		}
		return true;
	}
	
	public ProductInOrder getPIOFromArr(ArrayList<ProductInOrder> prods, Product prod) {
		if(prod == null) return null;
		for (ProductInOrder pio : prods) {
			if(pio.getProduct()!=null &&
					pio.getProduct().getPrdID().equals(prod.getPrdID()))
				return pio;
		}
		return null;
	}
}
