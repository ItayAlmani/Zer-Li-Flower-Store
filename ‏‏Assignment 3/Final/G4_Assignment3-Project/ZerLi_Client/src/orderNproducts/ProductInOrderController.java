package orderNproducts;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.CSMessage;
import common.CSMessage.MessageType;
import common.gui.controllers.ParentGUIController;
import common.Context;
import common.ParentController;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Store;
import orderNproducts.interfaces.IProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrder {	
//------------------------------------------------IN CLIENT--------------------------------------------------------------------		
	@Override
	public float calcFinalPrice(ProductInOrder p) {
		return p.getQuantity()*p.getProduct().getPrice();
	}
	
	@Override
	public void updatePricesByStock(ArrayList<ProductInOrder> prds, Store store) throws Exception {
		for (ProductInOrder pio : prds) {
			Stock stk = Context.fac.stock.getStockByProductFromStore(store, pio.getProduct());
			if(stk == null) {
				System.err.println("Stock is null");
				throw new Exception(pio.getProduct().toString());
			}
			float priceBeforeChange = pio.getProduct().getPrice();
			Float newPrice = Context.fac.product.getPriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
			pio.getProduct().setPrice(newPrice==null ? stk.getPriceAfterSale() : newPrice);
			//if the price changed, recalculate the final price of the pio
			if(priceBeforeChange!=stk.getPriceAfterSale().floatValue())
				pio.setFinalPrice();				
		}
	}
	
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
	@Override
	public void getPIOsByOrder(BigInteger orderID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(orderID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT, myMsgArr, ProductInOrder.class));
	}

	@Override
	public void update(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,ProductInOrder.class));
	}
	
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
	
	@Override
	public void updatePriceOfPIO(ProductInOrder p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE, myMsgArr, ProductInOrder.class));
	}
//-------------------------------------------------------------------------------------------------------------------------
	
}
