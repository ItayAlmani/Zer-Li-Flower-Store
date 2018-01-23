package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import entities.Customer;
import entities.Order;
import entities.Order.OrderStatus;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class UpdateOrderStatusGUIController implements Initializable {

	private @FXML JFXComboBox<OrderStatus> cbOrderStatus;
	private @FXML JFXComboBox<BigInteger> cbOrderIDs;
	private @FXML JFXTextField txtPrivateID;
	private @FXML VBox vboxComboBox;
	private ArrayList<Order> orders = null;
	private Order selctedOrd = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		ArrayList<OrderStatus> al = new ArrayList<>();
		for(OrderStatus os : OrderStatus.values())
			al.add(os);
		cbOrderStatus.setItems(FXCollections.observableArrayList(al));
	}

	public void searchCustomer() {
		//=========NEEDED!!!========
		/*try {
			Context.mainScene.setMenuPaneDisable(true);
			Context.fac.customer.getCustomerByPrivateID(txtPrivateID.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		Context.mainScene.setMenuPaneDisable(false);
		if(customers!=null && customers.isEmpty()==false) {
			try {
				Context.mainScene.setMenuPaneDisable(false);
				Context.fac.order.getOrdersByCustomerID(customers.get(0).getCustomerID());
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				e.printStackTrace();
			}
		}
	}
	
	public void setOrders(ArrayList<Order> orders) {
		Context.mainScene.setMenuPaneDisable(false);
		if(orders != null && orders.isEmpty()==false) {
			this.orders=orders;
			ArrayList<BigInteger> al = new ArrayList<>();
			for (Order order : orders) 
				al.add(order.getOrderID());
			cbOrderIDs.setItems(FXCollections.observableArrayList(al));
		}
		vboxComboBox.setVisible(true);
	}

	public void loadOrderStatus() {
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

	public void updateOrderStatus() {
		if(cbOrderStatus.getValue()==null)
			return;
		selctedOrd.setOrderStatus(OrderStatus.valueOf(cbOrderStatus.getValue().toString()));
		try {
			if(Context.getUserAsStoreWorker()!=null)
				selctedOrd.getDelivery().setStore(Context.getUserAsStoreWorker().getStore());
			Context.fac.order.update(selctedOrd);
		} catch (Exception e) {
			e.printStackTrace();
			Context.mainScene.ShowErrorMsg();
		}
	}
}