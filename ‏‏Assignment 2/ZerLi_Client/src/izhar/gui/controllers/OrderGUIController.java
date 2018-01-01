package izhar.gui.controllers;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

import common.Context;
import entities.Order;
import gui.controllers.ParentGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderGUIController extends ParentGUIController {
	private @FXML Label lblOrderID, lblDelMethod, lblDelTime, lblPayment;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		Order ord = Context.order;
		
		lblOrderID.setText(ord.getOrderID().toString());
		lblDelMethod.setText(ord.getDeliveryType().toString());
		lblDelTime.setText(ord.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
		lblPayment.setText(((Float)ord.getFinalPrice()).toString());
	}
	
	public void toMainMenu() {
		Context.askOrder();
		loadMainMenu();
	}
}
