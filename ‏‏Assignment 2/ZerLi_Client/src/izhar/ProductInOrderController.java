package izhar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Product;
import entities.CSMessage.MessageType;
import entities.Product.Color;
import entities.Product.ProductType;
import izhar.interfaces.IProductInOrder;
import entities.ProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrder {	
	@Override
	public void updatePIO(ProductInOrder p) throws IOException {
		/*myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productName='%s' WHERE productID=%d;",p.getName(),p.getPrdID()));
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr));*/
	}
	
	@Override
	public void handleGet(ArrayList<Object> obj) {
		if(obj==null) return;
		ArrayList<ProductInOrder> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 10) {
			try {
				prds.add(parse(
						(Context.fac.product.parse((int) obj.get(i), 
						(String) obj.get(i + 1), 
						(String) obj.get(i + 2),
						(float) obj.get(i + 3),
						(String) obj.get(i + 4),
						((int)obj.get(i + 5))!= 0,
						(String)obj.get(i+6))
						),BigInteger.valueOf(Long.valueOf((int)obj.get(i+7)))
						,(int)obj.get(i+8),
						(float)obj.get(i+9)
						));
			} catch (FileNotFoundException e) {
				System.err.println("Couldn't find Image named "+ (String)obj.get(i+6) +".");
				e.printStackTrace();
			}
		}
		sendPIOs(prds);
	}
	
	@Override
	public ProductInOrder parse(Product prod, BigInteger orderID, int quantity, float finalPrice) {
		return new ProductInOrder(prod,orderID,quantity,finalPrice);
	}
	
	@Override
	public void sendPIOs(ArrayList<ProductInOrder> prds) {
		String productsToGUI_MethodName = "productsInOrderToGUI";
		Method m = null;
		try {
			m = Context.currentGUI.getClass().getMethod(productsToGUI_MethodName,ArrayList.class);
			m.invoke(Context.currentGUI, prds);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+productsToGUI_MethodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+productsToGUI_MethodName+"'");
			e2.printStackTrace();
		}
	}

	@Override
	public void addPIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		String res = "0";
		if(p.isInCatalog())
			res="1";
		myMsgArr.add(
				"INSERT INTO orders (productName, productType, price, color, inCatalog)"
						+ "VALUES ('" + p.getName() + "', '" 
						+ p.getType().toString() + "', '"
						+ p.getPrice() + "', '"
						+ p.getColor().toString() + "', '"
						+ res + "');"
								);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr));
	}

	@Override
	public void getPIOsByOrder(BigInteger orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT prd.*,orderID, quantity, totalprice FROM" + 
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
}
