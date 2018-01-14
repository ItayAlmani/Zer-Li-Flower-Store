package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.junit.internal.runners.model.EachTestNotifier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.Context;
import entities.Customer;
import entities.Order;
import entities.Order.OrderStatus;
import entities.PaymentAccount;
import entities.Store;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateTimeStringConverter;

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
				ArrayList<Order> paidOrders = new ArrayList<Order>();
				if (ord != null && ord.isEmpty() == false) {
					for (Order order : ord) 
						if(order.getOrderStatus().equals(OrderStatus.Paid)==false)
							paidOrders.add(order);
					if(paidOrders.isEmpty()==false) {
						cbsOrders.setItems(FXCollections.observableArrayList(paidOrders));
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

	public void cancelOrder() throws IOException {
		
		Context.fac.order.cancelOrder(cbsOrders.getValue());
		Context.fac.stock.updateAfterCancellation(cbsOrders.getValue());
		PaymentAccount pa = Context.fac.paymentAccount.getPaymentAccountOfStore(
				new Customer(cbsOrders.getValue().getCustomerID()).getPaymentAccounts(),
				cbsOrders.getValue().getDelivery().getStore());
		if (Context.fac.order.differenceDeliveryTimeAndCurrent(cbsOrders.getValue().getDelivery())==Order.Refund.Full)
		{
			pa.setRefundAmount(pa.getRefundAmount()+cbsOrders.getValue().getFinalPrice());
			Context.mainScene.setMessage("Account is fully refunded for this order");
		}
		else if (Context.fac.order.differenceDeliveryTimeAndCurrent(cbsOrders.getValue().getDelivery())==Order.Refund.Partial)
		{
			pa.setRefundAmount((float) (pa.getRefundAmount()+(cbsOrders.getValue().getFinalPrice())*0.5));
			Context.mainScene.setMessage("Account refunded for 50% of this order");
		}
		else 
			Context.mainScene.setMessage("Account isn't refunded");
	}

	
}