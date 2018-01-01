package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import common.ClientController;
import common.Context;
import entities.Customer;
import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import entities.Order.PayMethod;
import entities.User;
import entities.User.UserType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.PaymentAccount;
import gui.controllers.ParentGUIController;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManualTransactionGUIController extends ParentGUIController {
	
	private @FXML Button btnSend;
	private @FXML VBox vbox;
	private ObservableList<Customer> custList;
	private ArrayList<Product> products;
	private ArrayList<ProductInOrder> pios = new ArrayList<>();
	private ArrayList<ComboBox<Product>> comboBoxs = new ArrayList<>();
	private @FXML ComboBox<Customer> cbCustomers;
	private @FXML ComboBox<PayMethod> cbPayMethod;
	
	private boolean cbCust = false, cbPay = false, cbProd = false;
	
	public void sendNewOrder() {
		Context.order.setType(OrderType.Manual);
		Context.order.setOrderStatus(OrderStatus.Paid);
		Context.fac.orderProcess.updateFinilizeOrder(Context.order);
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
		products=prds;
		addNewIDInputNodes();
	}
	
	public void setStocks(ArrayList<Stock> stocks) {
		products = new ArrayList<>();
		for (Stock stock : stocks)
			if(stock.getProduct() != null)
				products.add(stock.getProduct());
		addNewIDInputNodes();
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
	
	public void addNewIDInputNodes() {
		HBox hbox = new HBox(5);
		
		ComboBox<Product> cb = new ComboBox<>(FXCollections.observableArrayList((ArrayList<Product>)products.clone()));
		cb.setPrefHeight(Control.USE_COMPUTED_SIZE);
		cb.setPrefWidth(Control.USE_COMPUTED_SIZE);
		//cb.setPromptText("Enter another product id");
		//cb.getSelectionModel().selectFirst();
		comboBoxs.add(cb);
		
		Spinner<Integer> sp = new Spinner<>(0, Integer.MAX_VALUE, 1);
		sp.setDisable(true);
		sp.setPrefWidth(50);
		sp.setPrefHeight(Control.USE_COMPUTED_SIZE);
		
		hbox.setPrefHeight(Control.USE_COMPUTED_SIZE);
		hbox.setPrefWidth(Control.USE_COMPUTED_SIZE);
		hbox.getChildren().addAll(cb,sp);
		addChangeToSpinner(cb,sp,hbox);
		addNewIDAfterAction(cb,sp,hbox);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vbox.getChildren().add(vbox.getChildren().size()-1,hbox);
			}
		});
	}
	
	private void addNewIDAfterAction(ComboBox<Product> cb,Spinner<Integer> s, HBox hbox) {
		cb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				products.remove(cb.getValue());
				s.setDisable(false);
				cbProd=true;
				if(cbPay && cbCust)
					btnSend.setDisable(false);
				ProductInOrder pio = Context.fac.prodInOrder.getPIOFromArr(Context.order.getProducts(), cb.getValue());
				if(pio!=null) {
					if(s.getValue().equals(0))
						Context.order.getProducts().remove(pio);
					else
						pio.setQuantity(s.getValue());
				}
				else if(cb.getValue() != null) {
					pio = new ProductInOrder(cb.getValue(), s.getValue(), Context.order.getOrderID());
					Context.order.getProducts().add(pio);
				}
				if(cb.getItems().size()!=0)
					addNewIDInputNodes();
			}
		});
	}
	
	private void addChangeToSpinner(ComboBox<Product> cb,Spinner<Integer> s, HBox hbox) {
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {				
				ProductInOrder pio = Context.fac.prodInOrder.getPIOFromArr(Context.order.getProducts(), cb.getValue());
				if(pio!=null) {
					if(s.getValue().equals(0))
						Context.order.getProducts().remove(pio);
					else
						pio.setQuantity(s.getValue());
				}
				else if(cb.getValue() != null) {
					pio = new ProductInOrder(cb.getValue(), s.getValue(), Context.order.getOrderID());
					Context.order.getProducts().add(pio);
				}
				if(s.getValue().equals(0)) {
					vbox.getChildren().remove(hbox);
					if(products.contains(cb.getValue())==false)
						products.add(cb.getValue());
					comboBoxs.remove(cb);
					for (ComboBox<Product> comBox : comboBoxs) {
						if(comBox.getItems().contains(cb.getValue())==false)
							comBox.getItems().add(cb.getValue());
					}
					if(comboBoxs.size()==1 && comboBoxs.get(0).getValue()==null) {
						cbProd=false;
						btnSend.setDisable(true);							
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
}