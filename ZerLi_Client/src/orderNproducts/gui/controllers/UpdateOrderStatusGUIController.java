package orderNproducts.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import common.gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import orderNproducts.entities.Order;
import orderNproducts.entities.Order.OrderStatus;
import usersInfo.entities.Customer;

public class UpdateOrderStatusGUIController implements Initializable {

	private @FXML JFXComboBox<OrderStatus> cbOrderStatus;
	private @FXML JFXComboBox<BigInteger> cbOrderIDs;
	private @FXML JFXTextField txtPrivateID;
	private @FXML VBox vboxComboBox;
	private ArrayList<Order> orders = null;
	private Order selctedOrd = null;
	private @FXML JFXComboBox<Customer> cbCustomers;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		try {
			Context.mainScene.setMenuPaneDisable(true);
			Context.fac.customer.getAllCustomersOfStore(Context.getUserAsStoreWorker().getStore().getStoreID());
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
		cbOrderStatus.setItems(FXCollections.observableArrayList(OrderStatus.values()));
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		Context.mainScene.setMenuPaneDisable(false);
		if(customers!=null && customers.isEmpty()==false) {
			if(Platform.isFxApplicationThread())
				cbCustomers.setItems(FXCollections.observableArrayList(customers));
			else Platform.runLater(()->cbCustomers.setItems(FXCollections.observableArrayList(customers)));
		}
	}
	
	public void customerSelected() {
		if(cbCustomers.getValue()!=null) {
			try {
				Context.mainScene.setMenuPaneDisable(false);
				Context.fac.order.getOrdersByCustomerID(cbCustomers.getValue().getCustomerID());
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