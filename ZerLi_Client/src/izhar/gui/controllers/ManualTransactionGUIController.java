package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

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
	
	private @FXML Button btnSend;
	private @FXML VBox vbox;
	private ObservableList<Customer> custList;
	private SortedSet<Product> prdSet;
	private ArrayList<ProductInOrder> pios = new ArrayList<>();
	private ArrayList<ComboBox<Product>> comboBoxs = new ArrayList<>();
	private @FXML ComboBox<Customer> cbCustomers;
	private @FXML ComboBox<PayMethod> cbPayMethod;
	private Order order = new Order();
	
	private boolean cbCust = false, cbPay = false, cbProd = false;
	
	public void sendNewOrder() {
		order.setType(OrderType.Manual);
		order.setOrderStatus(OrderStatus.Paid);
		try {
			order.setDelivery(new DeliveryDetails(order.getOrderID(), LocalDateTime.now(), true, Context.getUserAsStoreWorker().getStore()));
			order.setDeliveryType(DeliveryType.Pickup);
			Context.fac.order.add(order,false);
			Context.fac.stock.update(Context.order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		if(order == null)
			order = new Order();
		
		ArrayList<PayMethod> pm = new ArrayList<>();
		pm.add(PayMethod.CreditCard);
		pm.add(PayMethod.Cash);
		cbPayMethod.setItems(FXCollections.observableArrayList(pm));
		
		vbox.setSpacing(5);
		try {
			Context.fac.stock.getStockByStore(BigInteger.ONE);
			Context.fac.customer.getAllCustomers();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setProducts(ArrayList<Product> prds) {
		prdSet = new TreeSet<>(prds);
		addNewHBox();
	}
	
	public void setStocks(ArrayList<Stock> stocks) {
		prdSet = new TreeSet<>();
		for (Stock stock : stocks)
			if(stock.getProduct() != null)
				prdSet.add(stock.getProduct());
		addNewHBox();
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		custList = FXCollections.observableArrayList(customers);	
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbCustomers.setItems(custList);
				cbCustomers.setPromptText("Choose customer");
			}
		});
	}
	
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
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbox.getChildren().add(vbox.getChildren().size()-1,hbox);
			}
		});
	}
	
	private void addCBEventsHandlers(ProductInComboBox picb) {
		picb.cb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(prdSet.size()==0) return;
				for (ComboBox<Product> comBox : comboBoxs)
					if(comBox.getValue()==null)
						return;
				addNewHBox();
			}
		});
		
		picb.cb.valueProperty().addListener(new ChangeListener<Product>() {
			@Override
			public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
				if(picb.s.isDisable()==false)
					return;
				//if combo box value changed
				if(newValue != null && newValue.equals(oldValue)==false) {
					if(oldValue!=null)	prdSet.add(oldValue);
					if(newValue!=null)	prdSet.remove(newValue);
					for (ComboBox<Product> comBox : comboBoxs) {
						if(comBox.getValue().equals(observable.getValue())==false) {
							TreeSet<Product> set = (TreeSet<Product>)comBox.getUserData();
							if(oldValue!=null)	set.add(oldValue);
							if(newValue!=null)	set.remove(newValue);
							comBox.setUserData(set);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									comBox.setItems(FXCollections.observableArrayList(set));
								}
							});
						}
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
			}    
        });
	}
	
	private void addSpinHanlders(ProductInComboBox picb) {
		picb.s.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if(newValue != null && newValue.equals(oldValue)==false) {
					ComboBox<Product> cb = (ComboBox<Product>) picb.s.getUserData();
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
							TreeSet<Product> set = (TreeSet<Product>)comBox.getUserData();
							if(toAdd!=null) set.add(toAdd);
							comBox.setUserData(set);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									comBox.setItems(FXCollections.observableArrayList(set));
								}
							});
						}
						if(comboBoxs.size()==1 && comboBoxs.get(0).getValue()==null) {
							cbProd=false;
							btnSend.setDisable(true);							
						}
					}
				}
				
			}
		});
	}

	public void customerSelected() {
		cbCust=true;
		if(cbPay && cbProd)
			btnSend.setDisable(false);
		if(cbCustomers.getValue()!=null && cbCustomers.getValue().getCustomerID()!=null)
			order.setCustomerID(cbCustomers.getValue().getCustomerID());
	}

	public void paymentSelected() {
		cbPay=true;
		if(cbCust && cbProd)
			btnSend.setDisable(false);
		if(cbPayMethod.getValue()!=null &&order!=null)
			order.setPaymentMethod(PayMethod.valueOf(cbPayMethod.getValue().toString()));
	}
	
	public class ProductInComboBox{
		public Spinner<Integer> s;
		public HBox hbox;
		public ComboBox<Product> cb;
		public ProductInComboBox(Spinner<Integer> s, HBox hbox, ComboBox<Product> cb) {
			super();
			this.s = s;
			this.hbox = hbox;
			this.cb=cb;
		}
	}
}