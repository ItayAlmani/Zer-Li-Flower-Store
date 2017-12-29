package izhar.gui.controllers;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Order.OrderStatus;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class UpdateOrderStatusGUIController extends ParentGUIController {

	private @FXML TextField txtCustomerName;
	private @FXML Button btnSend, btnUpdate;
	private @FXML ComboBox<OrderStatus> cbOrderStatus;
	private @FXML ComboBox<BigInteger> cbOrderIDs;
	private @FXML TextField txtPrivateID;

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
		
	}

	@FXML public void loadOrderStatus() {}

	@FXML public void updateOrderStatus() {}

	@FXML public void back() {}
}