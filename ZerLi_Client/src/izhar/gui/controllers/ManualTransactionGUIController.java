package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.PayMethod;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
		public JFXComboBox<Stock> cb;
		public ProductInComboBox(Spinner<Integer> s, HBox hbox, JFXComboBox<Stock> cb) {
			this.s = s;
			this.hbox = hbox;
			this.cb=cb;
		}
	}
	
	private @FXML JFXButton btnSend;
	private @FXML VBox vbox;
	private ObservableList<Customer> custList;
	private static SortedSet<Stock> prdSet;
	private static ArrayList<JFXComboBox<Stock>> comboBoxs = new ArrayList<>();
	private @FXML JFXComboBox<Customer> cbCustomers;
	private @FXML JFXComboBox<PayMethod> cbPayMethod;
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
			
			//set disable until answer
			Context.mainScene.setMenuPaneDisable(true);
			Context.fac.customer.getAllCustomers();
			
			prdSet = new TreeSet<>();
			for (Stock stock : s.getStock()) {
				if(stock.getProduct() != null && stock.getQuantity()>0) {
					prdSet.add(stock);
				}
			}
			addNewHBox();
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	public void addNewHBox() {
		HBox hbox = new HBox(5);
		TreeSet<Stock> newSet = new TreeSet<Stock>(prdSet);
		JFXComboBox<Stock> cb = new JFXComboBox<>(FXCollections.observableArrayList(newSet));
		cb.setPrefHeight(Control.USE_COMPUTED_SIZE);
		cb.setPrefWidth(Control.USE_COMPUTED_SIZE);
		cb.setId(String.valueOf(comboBoxs.size()));
		comboBoxs.add(cb);
		for (int i = comboBoxs.size()-1; i >=1 ; i--)
			comboBoxs.get(i).setUserData(comboBoxs.get(i-1));
		comboBoxs.get(0).setUserData(cb);

		/*if(comboBoxs.size() == 2)
			cb.setUserData(comboBoxs.get(0));
		else if(comboBoxs.size() > 2)
			cb.setUserData(comboBoxs.get(comboBoxs.size()-2));*/
		
		Spinner<Integer> sp = null;
		
		hbox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		hbox.setPrefWidth(Control.USE_COMPUTED_SIZE);
		hbox.getChildren().addAll(cb);
		ProductInComboBox picb = new ProductInComboBox(sp, hbox, cb);
		addCBEventsHandlers(picb);
		if(Platform.isFxApplicationThread()) {
			vbox.getChildren().add(vbox.getChildren().size()-1,hbox);
			vbox.setSpacing(15);
		}
		else Platform.runLater(()->{vbox.getChildren().add(vbox.getChildren().size()-1,hbox);vbox.setSpacing(15);});
	}

	/** will become true at the moment : comBox.getValue().equals(observable.getValue()) == true */
	private	boolean search_started = false;
	private int ind = -1;
	private static int changeCnt = 0;
	
	private void addCBEventsHandlers(ProductInComboBox picb) {		
		picb.cb.valueProperty().addListener((observable,oldValue,newValue)-> {
			if(newValue != null && picb.s == null) {
				picb.s = new Spinner<>(0, newValue.getQuantity(), 1);
				picb.s.setId(picb.cb.getId());
				picb.s.setPrefWidth(50);
				picb.s.setPrefHeight(Control.USE_COMPUTED_SIZE);
				picb.s.setUserData(picb.cb);
				picb.hbox.getChildren().addAll(picb.s);
				addSpinHanlders(picb);
			}
		});
		
		
		picb.cb.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)-> {
			//if combo box value changed
			if(newValue != null && newValue.equals(oldValue)==false) {
				JFXComboBox<Stock> comBox = (JFXComboBox<Stock>) picb.cb.getUserData();
				if(comBox == null || picb.cb.equals(comBox) || picb.cb.getId().equals(comBox.getId())) {
					setNewData(oldValue, newValue);
					return;
				}
				ObservableList<Stock> newArr = FXCollections.observableArrayList(comBox.getItems());
				if(oldValue!=null && !newArr.contains(oldValue))
					newArr.add(oldValue);
				if(newValue!=null && newArr.contains(newValue))		
					newArr.remove(newValue);
				FXCollections.sort(newArr);
				changeCnt=0;
				comBox.setItems(newArr);
				cbProd=true;
				if(cbPay && cbCust)
					btnSend.setDisable(false);
				ProductInOrder pio = Context.fac.prodInOrder.getPIOFromArr(
						order.getProducts(), observable.getValue().getProduct());
				if(pio!=null) {
					if(picb.s.getValue().equals(0))
						order.getProducts().remove(pio);
					else
						pio.setQuantity(picb.s.getValue());
				}
				else if(observable.getValue() != null) {
					pio = new ProductInOrder(observable.getValue().getProduct(),
							picb.s.getValue(), order.getOrderID());
					order.getProducts().add(pio);
				}
				setNewData(oldValue, newValue);
			}
        });
		
		
		picb.cb.itemsProperty().addListener(new ChangeListener<ObservableList<Stock>>() {
			@Override
			public void changed(ObservableValue<? extends ObservableList<Stock>> observable,
					ObservableList<Stock> oldValue, ObservableList<Stock> newValue) {
				if(changeCnt >= comboBoxs.size()) {
					changeCnt=0;
					return;
				}
				changeCnt++;
				if(oldValue.equals(newValue))
					return;
				JFXComboBox<Stock> cb = (JFXComboBox<Stock>) picb.cb.getUserData();
				if(cb == null || picb.cb.equals(cb) || picb.cb.getId().equals(cb.getId()))	return;
				ObservableList<Stock> newArr = FXCollections.observableArrayList((ArrayList<Stock>)(new ArrayList<Stock>(newValue).clone()));
				if(cb.getValue()!=null)
					newArr.add(cb.getValue());
				if(picb.cb.getValue()!=null)
					newArr.remove(picb.cb.getValue());
				FXCollections.sort(newArr);
				if(cb.getItems().equals(newArr)==false)
					cb.setItems(newArr);
			}
		});
	}
	
	private void setNewData(Stock oldValue, Stock newValue) {
		if(oldValue!=null)	
			prdSet.add(oldValue);
		if(newValue!=null)	prdSet.remove(newValue);
		if(prdSet.isEmpty()==false) {
			boolean to_add = true;
			for (JFXComboBox<Stock> cb : comboBoxs)
				if(cb.getValue()==null) {
					to_add=false;
					break;
				}
			if(to_add)	
				addNewHBox();
		}
	}
	
	private void addSpinHanlders(ProductInComboBox picb) {
		picb.s.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if(newValue != null && newValue.equals(oldValue)==false) {
					JFXComboBox<Stock> cb = (JFXComboBox<Stock>)picb.cb.getUserData();
					ProductInOrder pio = Context.fac.prodInOrder.getPIOFromArr(
							order.getProducts(), picb.cb.getValue().getProduct());
					if(pio!=null) {
						if(picb.s.getValue().equals(0))
							order.getProducts().remove(pio);
						else
							pio.setQuantity(picb.s.getValue());
					}
					else if(picb.cb.getValue() != null) {
						pio = new ProductInOrder(picb.cb.getValue().getProduct(),
								picb.s.getValue(), order.getOrderID());
						order.getProducts().add(pio);
					}
					if(newValue.equals(0)) {
						vbox.getChildren().remove(picb.hbox);
						if(prdSet.contains(picb.cb.getValue())==false)
							prdSet.add(picb.cb.getValue());
						Stock toAdd = picb.cb.getValue();
						comboBoxs.remove(picb.cb);
						if(cb == null /*|| picb.cb.equals(cb)*/)	return;
						ObservableList<Stock> newArr = FXCollections.observableArrayList(cb.getItems());
						if(toAdd!=null && !newArr.contains(toAdd))	
							newArr.add(toAdd);
						FXCollections.sort(newArr);
						changeCnt=0;
						cb.setItems(newArr);
						if(comboBoxs.size()==1 && comboBoxs.get(0).getValue()==null) {
							cbProd=false;
							btnSend.setDisable(true);				
						}
					}
				}
			}
		});
	}

	public void setCustomers(ArrayList<Customer> customers) {
		custList = FXCollections.observableArrayList(customers);
		Context.mainScene.setMenuPaneDisable(false);
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