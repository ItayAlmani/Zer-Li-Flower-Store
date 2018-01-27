package orderNproducts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import common.CSMessage;
import common.Context;
import common.ParentController;
import common.CSMessage.MessageType;
import common.gui.controllers.ParentGUIController;
import orderNproducts.entities.Order;
import orderNproducts.entities.Product;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Product.Color;
import orderNproducts.entities.Product.ProductType;
import orderNproducts.interfaces.IProduct;
import usersInfo.entities.Customer;
import usersInfo.entities.PaymentAccount;
import usersInfo.entities.Subscription;

public class ProductController extends ParentController implements IProduct {	
//------------------------------------------------IN CLIENT--------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see izhar.IProduct#insertImageToByteArr(java.io.File)
	 */
	@Override
	public byte[] insertImageToByteArr(File imgFile) throws Exception{
		try {
			byte[] mybytearray  = new byte [(int)imgFile.length()];
			FileInputStream fis = new FileInputStream(imgFile);
			BufferedInputStream bis = new BufferedInputStream(fis);			    
			bis.read(mybytearray,0,mybytearray.length);
			bis.close();
			return mybytearray;
		}catch (Exception e) {
			System.err.println(imgFile.getName()+"\n");
			e.printStackTrace();
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#getPriceWithSubscription(entities.Order, entities.Product, java.lang.Float, entities.Customer)
	 */
	@Override
	public Float getPriceWithSubscription(Order order, Product p, Float price, Customer customer) throws Exception {
		PaymentAccount pa = null;
		if(order.getDelivery() != null && order.getDelivery().getStore() != null)
			pa = Context.fac.paymentAccount.getPaymentAccountOfStore(
					customer.getPaymentAccounts(), order.getDelivery().getStore());
		if(pa!= null && pa.getSub() != null && pa.getSub().getSubType() != null)
			return Context.fac.sub.getPriceBySubscription(pa.getSub(), price);
		return price;
	} 
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#assembleProduct(entities.Product.ProductType, java.lang.Float, java.lang.Float, entities.Product.Color, java.util.ArrayList, entities.Subscription)
	 */
	@Override
	public ArrayList<Stock> assembleProduct(ProductType type, Float priceStart, Float priceEnd, Color color, ArrayList<Stock> stocks, Subscription sub) {
		ArrayList<Stock> inConditionProds = new ArrayList<>();
		if(type!=null && priceStart!=null && priceEnd!=null) {
			if(color==null) {
				for (Stock s : stocks) {
					Float price = sub!=null?Context.fac.sub.getPriceBySubscription(sub, s.getPriceAfterSale()):s.getPriceAfterSale();
					Product p = s.getProduct();
					if(p.getType().equals(type) && price>=priceStart && price<=priceEnd)
						inConditionProds.add(s);
				}
			}
			else {
				for (Stock s : stocks) {
					Product p = s.getProduct();
					Float price = sub!=null?Context.fac.sub.getPriceBySubscription(sub, s.getPriceAfterSale()):s.getPriceAfterSale();
					if(p.getType().equals(type) && price>=priceStart && price<=priceEnd
						&& p.getColor().equals(color))
							inConditionProds.add(s);
				}
			}
		}
		return inConditionProds;
	}
	
//------------------------------------------------IN SERVER--------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see izhar.IProduct#getProductByID(java.math.BigInteger)
	 */
	@Override
	public void getProductByID(BigInteger prdID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(prdID);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#update(entities.Product)
	 */
	@Override
	public void update(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		myMsgArr.add(p);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr,Product.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#handleGet(java.util.ArrayList)
	 */
	@Override
	public void handleGet(ArrayList<Product> prds) {
		String methodName = "setProducts";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				Object obj = Context.askingCtrl.get(0);
				Context.askingCtrl.remove(0);
				m.invoke(obj, prds);
			}
			else {
				m = ParentGUIController.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(ParentGUIController.currentGUI, prds);
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
	 * @see izhar.IProduct#handleInsert(java.math.BigInteger)
	 */
	@Override
	public void handleInsert(BigInteger id) {
		String methodName = "setProductID";
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
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#add(entities.Product, boolean)
	 */
	@Override
	public void add(Product p, boolean getID) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		ArrayList<Object> arr = new ArrayList<>();
		arr.add(p);
		arr.add(getID);
		myMsgArr.add(arr);
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.INSERT, myMsgArr,Product.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#getAllProducts()
	 */
	@Override
	public void getAllProducts() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#getProductsInCatalog()
	 */
	@Override
	public void getProductsInCatalog() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProduct#getAllProductsNotInCatalog()
	 */
	@Override
	public void getAllProductsNotInCatalog() throws IOException {
		myMsgArr.clear();
		myMsgArr.add(Thread.currentThread().getStackTrace()[1].getMethodName());
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,Product.class));		
	}
//-------------------------------------------------------------------------------------------------------------------------
}
