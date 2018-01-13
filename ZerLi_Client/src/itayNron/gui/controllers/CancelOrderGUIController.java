package itayNron.gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.junit.internal.runners.model.EachTestNotifier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.Context;
import entities.Customer;
import entities.Order;
import entities.Order.OrderStatus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CancelOrderGUIController implements Initializable {

	private @FXML JFXComboBox<Order> cbsOrders;
	private @FXML JFXButton btnCancelOrder;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Context.fac.order.getOrdersByCustomerID(Context.getUserAsCustomer().getCustomerID());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Context.mainScene.loadMainMenu("You don't have permission to this section");
		}
		
	}
	
	public void setOrders(ArrayList<Order> ord) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (ord != null && ord.isEmpty() == false) {
					for (Order order : ord) 
						if(order.getOrderStatus().equals(OrderStatus.Paid)==false)
							ord.remove(order);
					if(ord.isEmpty()==false) {
						cbsOrders.setItems(FXCollections.observableArrayList(ord));
						cbsOrders.setDisable(false);
					}
					else
						Context.mainScene.setMessage("No orders to cancel");
				}
				else
					Context.mainScene.setMessage("No orders to cancel");
			}
		});
		
	}

	public void cancelOrder() {
		btnCancelOrder.setVisible(true);
		
		
	}

	
}