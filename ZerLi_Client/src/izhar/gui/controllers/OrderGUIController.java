package izhar.gui.controllers;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import common.Context;
import entities.Order;
import gui.controllers.ParentGUIController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class OrderGUIController implements Initializable {
	private @FXML Label lblOrderID, lblDelMethod, lblDelTime, lblPayment;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
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
	
	/*public void toMainMenu() {
		Context.askOrder();
		Context.mainScene.getNewOrderByStore();
		Context.mainScene.loadMainMenu();
	}*/
}
