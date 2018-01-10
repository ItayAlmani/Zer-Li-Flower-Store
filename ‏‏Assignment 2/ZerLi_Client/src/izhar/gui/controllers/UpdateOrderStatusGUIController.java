package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import common.Context;
import entities.Customer;
import entities.Order;
import entities.Order.OrderStatus;
import entities.Order.PayMethod;
import entities.User.UserType;
import gui.controllers.ParentGUIController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UpdateOrderStatusGUIController implements Initializable {

	private @FXML Button btnSend, btnUpdate;
	private @FXML ComboBox<OrderStatus> cbOrderStatus;
	private @FXML ComboBox<BigInteger> cbOrderIDs;
	private @FXML TextField txtPrivateID;
	private @FXML VBox vboxComboBox;
	private ArrayList<Order> orders = null;
	private Order selctedOrd = null;
	private boolean originStatusWaitForCash = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
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
			if(selctedOrd.getOrderStatus().equals(OrderStatus.WaitingForCashPayment))
				originStatusWaitForCash=true;
			cbOrderStatus.setValue(selctedOrd.getOrderStatus());
			cbOrderStatus.setVisible(true);
		}
	}

	@FXML public void updateOrderStatus() {
		if(cbOrderStatus.getValue()==null)
			return;
		if(originStatusWaitForCash==true && cbOrderStatus.getValue().equals(OrderStatus.Paid))
			selctedOrd.setPaymentMethod(PayMethod.Cash);
		selctedOrd.setOrderStatus(OrderStatus.valueOf(cbOrderStatus.getValue().toString()));
		try {
			if(Context.getUserAsStoreWorker()!=null)
				selctedOrd.getDelivery().setStore(Context.getUserAsStoreWorker().getStore());
			Context.fac.order.update(selctedOrd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void back() {
		Context.mainScene.loadMainMenu();
	}
}