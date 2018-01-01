package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import common.ClientController;
import common.Context;
import entities.Customer;
import entities.Order;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManualTransactionGUIController extends ParentGUIController {
	
	private @FXML Button btnSend;
	private @FXML VBox vbox;
	private ObservableList<Customer> custList;
	//private ArrayList<Product> products;
	private SortedSet<Product> prdSet;
	private ArrayList<ProductInOrder> pios = new ArrayList<>();
	private ArrayList<ComboBox<Product>> comboBoxs = new ArrayList<>();
	private @FXML ComboBox<Customer> cbCustomers;
	private @FXML ComboBox<PayMethod> cbPayMethod;
	
	private boolean cbCust = false, cbPay = false, cbProd = false;
	
	public void sendNewOrder() {
		Context.order.setType(OrderType.Manual);
		Context.order.setOrderStatus(OrderStatus.Paid);
		try {
			Context.fac.orderProcess.updateFinilizeOrder(Context.order);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setUP() {
		ArrayList<Customer> cust = new ArrayList<>();
		cust.add(new Customer(new User(BigInteger.ONE, "314785270",
				"Izhar", "Ananiev", "izharAn", "1234",
				UserType.Customer), BigInteger.ONE, 
				new PaymentAccount(BigInteger.ONE)));
		setCustomers(cust);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		if(Context.order == null)
			Context.order = new Order();
		
		ArrayList<PayMethod> pm = new ArrayList<>();
		pm.add(PayMethod.Cash);
		pm.add(PayMethod.CreditCard);
		cbPayMethod.setItems(FXCollections.observableArrayList(pm));
		
		try {
			ClientController.getLastAutoIncrenment(Order.class);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		vbox.setSpacing(5);
		try {
			Context.fac.stock.getStockByStore(BigInteger.ONE);
			
			//Context.fac.customer.getAllCustomers();
			setUP();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setProducts(ArrayList<Product> prds) {
		//products=prds;
		prdSet = new TreeSet<>(prds);
		addNewHBox();
	}
	
	public void setStocks(ArrayList<Stock> stocks) {
		//products = new ArrayList<>();
		prdSet = new TreeSet<>();
		for (Stock stock : stocks)
			if(stock.getProduct() != null)
				//products.add(stock.getProduct());
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
		//cb.setPromptText("Enter another product id");
		//cb.getSelectionModel().selectFirst();
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
							Context.order.getProducts(), observable.getValue());
					if(pio!=null) {
						if(picb.s.getValue().equals(0)) {
							Context.order.getProducts().remove(pio);
							/*try {
								Context.fac.prodInOrder.deletePIO(pio);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
						}
						else
							pio.setQuantity(picb.s.getValue());
					}
					else if(observable.getValue() != null) {
						pio = new ProductInOrder(observable.getValue(),
								picb.s.getValue(), Context.order.getOrderID());
						Context.order.getProducts().add(pio);
						/*try {
							Context.fac.prodInOrder.addPIO(pio);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
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
							Context.order.getProducts(), cb.getValue());
					if(pio!=null) {
						if(picb.s.getValue().equals(0))
							Context.order.getProducts().remove(pio);
						else
							pio.setQuantity(picb.s.getValue());
					}
					else if(cb.getValue() != null) {
						pio = new ProductInOrder(cb.getValue(),
								picb.s.getValue(), Context.order.getOrderID());
						Context.order.getProducts().add(pio);
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

	@FXML public void customerSelected() {
		cbCust=true;
		if(cbPay && cbProd)
			btnSend.setDisable(false);
		if(cbCustomers.getValue()!=null && cbCustomers.getValue().getCustomerID()!=null)
			Context.order.setCustomerID(cbCustomers.getValue().getCustomerID());
	}

	@FXML public void paymentSelected() {
		cbPay=true;
		if(cbCust && cbProd)
			btnSend.setDisable(false);
		if(cbPayMethod.getValue()!=null &&Context.order!=null)
			Context.order.setPaymentMethod(PayMethod.valueOf(cbPayMethod.getValue().toString()));
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