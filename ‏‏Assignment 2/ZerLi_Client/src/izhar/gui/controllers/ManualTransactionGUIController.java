package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.ClientController;
import common.Context;
import entities.Customer;
import entities.Order;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManualTransactionGUIController extends ParentGUIController {
	
	private Button btnSend  = new Button("Add new order");
	private @FXML VBox vbox;
	private ArrayList<Product> products = new ArrayList<>();
	private ArrayList<String> prodsName = new ArrayList<>();
	private ArrayList<ProductInOrder> pios = new ArrayList<>();
	private ArrayList<ComboBox<String>> comboBoxs = new ArrayList<>();
	private TextField txtCustomerID = new TextField();
	private ComboBox<String> cbCustomers = new ComboBox<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		//ADD PRICE AND CREATE MANUAL ORDER AND TRANSACTION!!
		btnSend.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
								
			}
		});
		
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
			ArrayList<Customer> cust = new ArrayList<>();
			cust.add(new Customer(new User(BigInteger.ONE, "314785270",
					"Izhar", "Ananiev", "izharAn", "1234",
					UserType.Customer), BigInteger.ONE, 
					new PaymentAccount(BigInteger.ONE)));
			setCustomers(cust);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setProducts(ArrayList<Product> prds) {
		products=prds;
		for (Product product : prds)
			prodsName.add(product.getName());
		if(Context.order!=null) {
			Context.order.setType(OrderType.Manual);
			Context.order.setOrderStatus(OrderStatus.Paid);
		}
		addNewIDInputNodes();
	}
	
	public void setStocks(ArrayList<Stock> stocks) {
		for (Stock stock : stocks) {
			products.add(stock.getProduct());
			prodsName.add(stock.getProduct().getName());
		}
		addNewIDInputNodes();
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		ArrayList<String> cusNames = new ArrayList<>();
		for (Customer customer : customers)
			cusNames.add(customer.getFullName());
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbCustomers.setItems(FXCollections.observableArrayList(cusNames));
				cbCustomers.setPromptText("Choose customer");
				vbox.getChildren().addAll(cbCustomers, btnSend);
			}
		});
	}
	
	public void addNewIDInputNodes() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				HBox hbox = new HBox(5);
				
				ComboBox<String> cb = new ComboBox<>(FXCollections.observableArrayList(prodsName));
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
				vbox.getChildren().add(vbox.getChildren().size()-1,hbox);
			}
		});
	}
	
	private void addNewIDAfterAction(ComboBox<String> cb,Spinner<Integer> s, HBox hbox) {
		cb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				prodsName.remove(cb.getValue());
				s.setDisable(false);
				spinnerHandler(cb,s,hbox);
				if(prodsName.size()!=0)
					addNewIDInputNodes();
			}
		});
	}
	
	private void addChangeToSpinner(ComboBox<String> cb,Spinner<Integer> s, HBox hbox) {
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				spinnerHandler(cb,s,hbox);
			}
		});
	}
	private void spinnerHandler(ComboBox<String> cb,Spinner<Integer> s, HBox hbox) {
		ProductInOrder pio = null;
		if(Context.order == null) {
			try {
				throw new Exception("Order not exists");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		boolean pioInOrder = false;
		for (Product prd : products) {
			if(prd.getName().equals(cb.getValue())) {
				for(ProductInOrder pio2: Context.order.getProducts()) {
					if(pio2.getProduct().getPrdID().equals(prd.getPrdID())) {
						pio2.setQuantity(s.getValue());
						pioInOrder=true;
						break;
					}
				}
				if(pioInOrder==false) {
					pio = new ProductInOrder(prd, s.getValue(), Context.order.getOrderID());
					Context.order.getProducts().add(pio);
				}
				break;
			}
		}
		if(s.getValue().equals(0)) {
			vbox.getChildren().remove(hbox);
			prodsName.add(cb.getValue());
			comboBoxs.remove(cb);
			for (ComboBox<String> comBox : comboBoxs)
				comBox.setItems(FXCollections.observableArrayList(prodsName));
		}
	}

	/**
	 * 
	 * @param txtItemsIDs
	 */
	public void createTransaction(TextField[] txtItemsIDs) {
		// TODO - implement ManualTransactionGUI.createTransaction
		throw new UnsupportedOperationException();
	}
}