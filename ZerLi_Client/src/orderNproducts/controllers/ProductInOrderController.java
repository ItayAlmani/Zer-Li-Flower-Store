package orderNproducts.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import common.Context;
import controllers.ParentController;
import entities.*;
import entities.CSMessage.MessageType;
import entities.Subscription.SubscriptionType;
import gui.controllers.ParentGUIController;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Store;
import orderNproducts.interfaces.IProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrder {	
//------------------------------------------------IN CLIENT--------------------------------------------------------------------		
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#calcFinalPrice(entities.ProductInOrder)
	 */
	@Override
	public float calcFinalPrice(ProductInOrder p) {
		return p.getQuantity()*p.getProduct().getPrice();
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#updatePricesByStock(java.util.ArrayList, entities.Store)
	 */
	@Override
	public void updatePricesByStock(ArrayList<ProductInOrder> prds, Store store) throws Exception {
		for (ProductInOrder pio : prds) {
			boolean foundMatch = false;
			for (Stock stk : store.getStock()) {
				if(pio.getProduct().getPrdID().equals(stk.getProduct().getPrdID())) {
					float priceBeforeChange = pio.getProduct().getPrice();
					Float newPrice = Context.fac.product.getPriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
					pio.getProduct().setPrice(
							newPrice==null ? stk.getPriceAfterSale() : newPrice*(1-stk.getSalePercetage()));
					//if the price changed, recalculate the final price of the pio
					if(priceBeforeChange!=stk.getPriceAfterSale().floatValue())
						pio.setFinalPrice();
					foundMatch=true;
					break;
				}
			}
			if(!foundMatch)
				throw new Exception(pio.getProduct().toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#getPIOFromArr(java.util.ArrayList, entities.Product)
	 */
	@Override
	public ProductInOrder getPIOFromArr(ArrayList<ProductInOrder> prods, Product prod) {
		if(prod == null || prods == null) return null;
		for (ProductInOrder pio : prods) {
			if(pio.getProduct()!=null &&
					pio.getProduct().getPrdID().equals(prod.getPrdID()))
				return pio;
		}
		return null;
	}
		
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#getPIOsNot0Quantity(java.util.ArrayList)
	 */
	@Override
	public ArrayList<ProductInOrder> getPIOsNot0Quantity(ArrayList<ProductInOrder> prds) {
		ArrayList<ProductInOrder> pios = new ArrayList<>();
		if(prds==null || prds.isEmpty())
			return pios;
		for (ProductInOrder pio : prds)
			if(pio.getQuantity()>0)
				pios.add(pio);
		return pios;
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#handleGet(java.util.ArrayList)
	 */
	@Override
	public void handleGet(ArrayList<ProductInOrder> pios) {
		String methodName = "setPIOs";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, pios);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, pios);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#handleInsert(java.math.BigInteger)
	 */
	@Override
	public void handleInsert(BigInteger id) {
		String methodName = "setPIOID";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,BigInteger.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, id);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,BigInteger.class);
				m.invoke(ParentGUIController.currentGUI, id);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}
//------------------------------------------------IN SERVER--------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#getPIOsByOrder(java.math.BigInteger)
	 */
	@Override
	public void getPIOsByOrder(BigInteger orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(orderID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, ProductInOrder.class));
	}

	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#update(entities.ProductInOrder)
	 */
	@Override
	public void update(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,ProductInOrder.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#add(entities.ProductInOrder, boolean)
	 */
	@Override
	public void add(ProductInOrder p, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(p);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,ProductInOrder.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrder#updatePriceOfPIO(entities.ProductInOrder)
	 */
	@Override
	public void updatePriceOfPIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr, ProductInOrder.class));
	}
//-------------------------------------------------------------------------------------------------------------------------
	
}
