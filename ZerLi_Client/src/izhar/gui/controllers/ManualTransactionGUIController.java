package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jfoenix.controls.JFXComboBox;

import common.ClientController;
import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.PayMethod;
import entities.PaymentAccount;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import entities.User;
import entities.User.UserType;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManualTransactionGUIController implements Initializable {
	
	/** Inner class used to transfer the whole input together:
	 * <ul>
	 * <li>{@link Spinner}     s - contains the quantity</li>
	 * <li>{@link JFXComboBox} cb - contains the selction of the {@link Product}</li>
	 * <li>{@link HBox} hbox - the container of the {@link JFXComboBox} and the {@link Spinner}</li>
	 * </ul>
	 * 
	 */
	public class ProductInComboBox{
		public Spinner<Integer> s;
		public HBox hbox;
		public ComboBox<Product> cb;
		public ProductInComboBox(Spinner<Integer> s, HBox hbox, ComboBox<Product> cb) {
			this.s = s;
			this.hbox = hbox;
			this.cb=cb;
		}
	}
	
	private @FXML Button btnSend;
	private @FXML VBox vbox;
	private ObservableList<Customer> custList;
	private static SortedSet<Product> prdSet;
	private static ArrayList<ComboBox<Product>> comboBoxs = new ArrayList<>();
	private @FXML ComboBox<Customer> cbCustomers;
	private @FXML ComboBox<PayMethod> cbPayMethod;
	private Order order = Context.order;
	
	private boolean cbCust = false, cbPay = false, cbProd = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		if(order == null) {
			Context.order = new Order();
			order=Context.order;
		}
		
		ArrayList<PayMethod> pm = new ArrayList<>();
		pm.add(PayMethod.CreditCard);
		pm.add(PayMethod.Cash);
		cbPayMethod.setItems(FXCollections.observableArrayList(pm));
		
		vbox.setSpacing(5);
		try {
			Store s = Context.getUserAsStoreWorker().getStore();
			if(s==null || s.getStoreID()==null) {
				Context.mainScene.ShowErrorMsg();
				return;
			}
			
			Context.fac.customer.getAllCustomers();
			
			prdSet = new TreeSet<>();
			for (Stock stock : s.getStock())
				if(stock.getProduct() != null && stock.getQuantity()>0)
					prdSet.add(stock.getProduct());
			addNewHBox();
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	/*public void setProducts(ArrayList<Product> prds) {
		prdSet = new TreeSet<>(prds);
		addNewHBox();
	}*/
	
	public void addNewHBox() {
		HBox hbox = new HBox(5);
		TreeSet<Product> newSet = new TreeSet<Product>(prdSet);
		ComboBox<Product> cb = new ComboBox<>(FXCollections.observableArrayList(newSet));
		cb.setUserData(newSet);
		cb.setPrefHeight(Control.USE_COMPUTED_SIZE);
		cb.setPrefWidth(Control.USE_COMPUTED_SIZE);
		comboBoxs.add(cb);
		
		Spinner<Integer> sp = new Spinner<>(0, Integer.MAX_VALUE, 1);
		sp.setDisable(true);
		sp.setPrefWidth(50);
		sp.setPrefHeight(Control.USE_COMPUTED_SIZE);
		sp.setUserData(cb);
		
		hbox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		hbox.setPrefWidth(Control.USE_COMPUTED_SIZE);
		hbox.getChildren().addAll(cb,sp);
		ProductInComboBox picb = new ProductInComboBox(sp, hbox, cb);
		addSpinHanlders(picb);
		addCBEventsHandlers(picb);
		
		if(Platform.isFxApplicationThread())
			vbox.getChildren().add(vbox.getChildren().size()-1,hbox);
		else Platform.runLater(()->vbox.getChildren().add(vbox.getChildren().size()-1,hbox));
	}

	/** will become true at the moment : comBox.getValue().equals(observable.getValue()) == true */
	private	boolean search_started = false;
	private int ind = -1;
	
	private void addCBEventsHandlers(ProductInComboBox picb) {
		picb.cb.setOnAction((event)->{
			if(prdSet.size()==0) return;
			for (ComboBox<Product> comBox : comboBoxs)
				if(comBox.getValue()==null)
					return;
			addNewHBox();
		});
		
		picb.cb.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)-> {
			if(picb.s.isDisable()==false)
				return;
			//if combo box value changed
			if(newValue != null && newValue.equals(oldValue)==false) {
				if(oldValue!=null)	prdSet.add(oldValue);
				if(newValue!=null)	prdSet.remove(newValue);
				//boolean i_is_0 = true;
				for (int i = 0; i < comboBoxs.size() && i!=ind; i=(++i)%comboBoxs.size()) {
					ComboBox<Product> comBox = comboBoxs.get(i);
					//The comboBox itself
					if(observable.getValue().equals(comBox.getValue())) {
						if(ind==-1)
							ind=i;
						else {
							ind=-1;
							break;
						}
						/*if(search_started) {
							search_started = false;
							break;
						}
						else if(i!=0)//if(i_is_0 && i==comboBoxs.size()-2)
							search_started = true;*/
						i=(++i)%comboBoxs.size();
						comBox = comboBoxs.get(i);
						/*do {
							
						}while(comBox.getValue()==null && i!=ind);*/
						
						if(i == ind && observable.getValue().equals(comBox.getValue())) {
							/*search_started = false;
							break;*/
							ind=-1;
							break;
						}
						
						TreeSet<Product> set = new TreeSet<>();
						for (Iterator<Product> iterator = comBox.getItems().iterator(); iterator.hasNext();) {
							Product product = (Product) iterator.next();
							set.add(product);
						}
						if(oldValue!=null)	set.add(oldValue);
						if(newValue!=null)	set.remove(newValue);
						comBox.setUserData(set);
						Product selected = comBox.getValue();
						comBox.setValue(null);
						comBox.setItems(FXCollections.observableArrayList(set));
						comBox.setValue(selected);
						break;
					}
					//i_is_0=false;
				}
				picb.s.setDisable(false);
				cbProd=true;
				if(cbPay && cbCust)
					btnSend.setDisable(false);
				ProductInOrder pio = Context.fac.prodInOrder.getPIOFromArr(
						order.getProducts(), observable.getValue());
				if(pio!=null) {
					if(picb.s.getValue().equals(0)) {
						order.getProducts().remove(pio);
					}
					else
						pio.setQuantity(picb.s.getValue());
				}
				else if(observable.getValue() != null) {
					pio = new ProductInOrder(observable.getValue(),
							picb.s.getValue(), order.getOrderID());
					order.getProducts().add(pio);
				}
			}
        });
	}
	
	private void addSpinHanlders(ProductInComboBox picb) {
		picb.s.valueProperty().addListener((observable,oldValue,newValue)-> {
			if(newValue != null && newValue.equals(oldValue)==false) {
				@SuppressWarnings("unchecked")
				ComboBox<Product> cb = (ComboBox<Product>)picb.s.getUserData();
				ProductInOrder pio = Context.fac.prodInOrder.getPIOFromArr(
						order.getProducts(), cb.getValue());
				if(pio!=null) {
					if(picb.s.getValue().equals(0))
						order.getProducts().remove(pio);
					else
						pio.setQuantity(picb.s.getValue());
				}
				else if(cb.getValue() != null) {
					pio = new ProductInOrder(cb.getValue(),
							picb.s.getValue(), order.getOrderID());
					order.getProducts().add(pio);
				}
				if(newValue.equals(0)) {
					vbox.getChildren().remove(picb.hbox);
					if(prdSet.contains(cb.getValue())==false)
						prdSet.add(cb.getValue());
					Product toAdd = cb.getValue();
					comboBoxs.remove(cb);
					for (ComboBox<Product> comBox : comboBoxs) {
						@SuppressWarnings("unchecked")
						TreeSet<Product> set = (TreeSet<Product>)comBox.getUserData();
						if(toAdd!=null) set.add(toAdd);
						comBox.setUserData(set);
						if(Platform.isFxApplicationThread())
							comBox.setItems(FXCollections.observableArrayList(set));
						else Platform.runLater(()->comBox.setItems(FXCollections.observableArrayList(set)));
					}
					if(comboBoxs.size()==1 && comboBoxs.get(0).getValue()==null) {
						cbProd=false;
						btnSend.setDisable(true);							
					}
				}
			}
		});
	}

	public void setCustomers(ArrayList<Customer> customers) {
		custList = FXCollections.observableArrayList(customers);
		if(Platform.isFxApplicationThread()) {
			cbCustomers.setItems(custList);
			cbCustomers.setPromptText("Choose customer");
		}
		else {
			Platform.runLater(()->{
				cbCustomers.setItems(custList);
				cbCustomers.setPromptText("Choose customer");
			});
		}
	}

	/** the onAction handler of {@link #cbCustomers}*/
	public void customerSelected() {
		cbCust=true;
		if(cbPay && cbProd)
			btnSend.setDisable(false);
		if(cbCustomers.getValue()!=null && cbCustomers.getValue().getCustomerID()!=null)
			order.setCustomerID(cbCustomers.getValue().getCustomerID());
	}

	/** the onAction handler of {@link #cbPayMethod}*/
	public void paymentSelected() {
		cbPay=true;
		if(cbCust && cbProd)
			btnSend.setDisable(false);
		if(cbPayMethod.getValue()!=null &&order!=null)
			order.setPaymentMethod(PayMethod.valueOf(cbPayMethod.getValue().toString()));
	}

	/** the onAction handler of {@link #btnSend}*/
	public void sendNewOrder() {
		order.setType(OrderType.Manual);
		order.setOrderStatus(OrderStatus.Paid);
		try {
			order.setDelivery(new DeliveryDetails(order.getOrderID(), LocalDateTime.now(), true, Context.getUserAsStoreWorker().getStore()));
			order.setDeliveryType(DeliveryType.Pickup);
			Context.fac.order.add(order,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setOrderID(BigInteger id) {
		try {
			if(Context.order!=null && Context.order.getOrderID()!=null)
				Context.fac.stock.update(Context.order);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Context.mainScene.loadGUI("OrderGUI", false);
	}
}