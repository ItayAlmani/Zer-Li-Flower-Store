package izhar.gui.controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import common.Context;
import entities.Order;
import entities.Order.DeliveryType;
import entities.ShipmentDetails;
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
		Context.order=null;
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		String formatDateTime = null;
		if(ord.getDelivery()!=null)
			formatDateTime = ord.getDelivery().getDate().format(formatter);
		else
			formatDateTime = "TBD";

		
		lblOrderID.setText(ord.getOrderID().toString());
		lblDelMethod.setText(ord.getDeliveryType().toString());
		lblDelTime.setText(formatDateTime);
		lblPayment.setText(ord.getFinalPriceAsString());
	}
	
	public void toMainMenu() {
		Context.askOrder();
		loadMainMenu();
	}
}
