package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Customer;
import entities.Order;
import entities.Order.OrderStatus;
import entities.User.UserType;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UpdateOrderStatusGUIController extends ParentGUIController {

	private @FXML Button btnSend, btnUpdate;
	private @FXML ComboBox<OrderStatus> cbOrderStatus;
	private @FXML ComboBox<BigInteger> cbOrderIDs;
	private @FXML TextField txtPrivateID;
	private @FXML VBox vboxComboBox;
	private ArrayList<Order> orders = null;
	private Order selctedOrd = null;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		ArrayList<OrderStatus> al = new ArrayList<>();
		for(OrderStatus os : OrderStatus.values())
			al.add(os);
		cbOrderStatus.setItems(FXCollections.observableArrayList(al));
	}

	@FXML public void searchCustomer() {
		/*try {
			Context.askingCtrl.add(this);
			Context.fac.customer.getCustomerByPrivateID(txtPrivateID.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//TEST -TAKE DOWN
		Customer cust = new Customer(BigInteger.ONE,"314785270", 
				"Izhar", "Ananiev", "izharAn", "1234", UserType.Customer);
		ArrayList<Customer> customers = new ArrayList<>();
		customers.add(cust);
		setCustomers(customers);
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		if(customers!=null && customers.isEmpty()==false) {
			try {
				Context.askingCtrl.add(this);
				Context.fac.order.getOrdersByCustomerID(customers.get(0).getCustomerID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setOrders(ArrayList<Order> orders) {
		if(orders != null && orders.isEmpty()==false) {
			this.orders=orders;
			ArrayList<BigInteger> al = new ArrayList<>();
			for (Order order : orders) 
				al.add(order.getOrderID());
			cbOrderIDs.setItems(FXCollections.observableArrayList(al));
		}
		vboxComboBox.setVisible(true);
	}

	@FXML public void loadOrderStatus() {
		BigInteger ordID = cbOrderIDs.getValue();
		selctedOrd = null;
		for (Order order : orders) {
			if(order.getOrderID().equals(ordID)) {
				selctedOrd=order;
				break;
			}
		}
		if(selctedOrd!=null) {
			cbOrderStatus.setValue(selctedOrd.getOrderStatus());
			cbOrderStatus.setVisible(true);
		}
	}

	@FXML public void updateOrderStatus() {
		selctedOrd.setOrderStatus(OrderStatus.valueOf(cbOrderStatus.getValue().toString()));
		try {
			Context.fac.order.updateOrder(selctedOrd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML public void back() {
		loadMainMenu();
	}
}