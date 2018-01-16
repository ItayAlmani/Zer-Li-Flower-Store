package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.FutureTask;

import org.junit.internal.runners.model.EachTestNotifier;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Order.Refund;
import entities.PaymentAccount;
import entities.ProductInOrder;
import entities.Store;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateTimeStringConverter;

public class CancelOrderGUIController implements Initializable {

	private @FXML JFXComboBox<Order> cbsOrders;
	private @FXML JFXButton btnCancelOrder;
	private ObservableList<Order> paidOrders;

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
				ArrayList<Order> ords = new ArrayList<Order>();
				if (ord != null && ord.isEmpty() == false) {
					for (Order order : ord)
						if (Context.fac.order.isCancelable(order))
							ords.add(order);
					if (ords.isEmpty() == false) {
						paidOrders = FXCollections.observableArrayList(ords);
						cbsOrders.setItems(paidOrders);
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
		cbsOrders.setDisable(true);
		btnCancelOrder.setVisible(false);
		Order ord = cbsOrders.getValue();
		ord.setOrderStatus(OrderStatus.Canceled);
		Context.fac.order.update(ord);
		
		for (ProductInOrder p : ord.getProducts())
			p.setQuantity(p.getQuantity() * (-1));
		Context.fac.stock.update(ord);
		PaymentAccount pa;
		try {
			DeliveryDetails del = ord.getDelivery();
			String msg = "Final order price is 0";
			boolean isRefunded = false;
			pa = Context.fac.paymentAccount.getPaymentAccountOfStore(
					Context.getUserAsCustomer().getPaymentAccounts(),
					del.getStore());
			if (ord.getFinalPrice() != 0) {
				Refund ref = Context.fac.order.differenceDeliveryTimeAndCurrent(del);
				float refAmt = pa.getRefundAmount();
				if (ref.equals(Refund.Full)) {
					pa.setRefundAmount(refAmt + ord.getFinalPrice());
					msg = "Account is fully refunded for this order";
					isRefunded=true;
				} 
				else if (ref.equals(Refund.Partial)){
					pa.setRefundAmount((float) (refAmt + (ord.getFinalPrice()) * 0.5f));
					msg = "Account refunded for 50% of this order";
					isRefunded=true;
				} 
				else
					msg = "Account isn't refunded";
			}
			if(isRefunded)
				Context.fac.paymentAccount.update(pa);
			cbsOrders.setValue(null);
			paidOrders.remove(ord);
			if (paidOrders.isEmpty() == false) {
				cbsOrders.setDisable(false);
				Context.mainScene.setMessage(msg);
			}
			else
				Context.mainScene.setMessage("No orders to cancel");
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().isEmpty()==false)
				System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void orderSelected() {
		Order ord = cbsOrders.getValue();
		if (ord != null)
			btnCancelOrder.setVisible(true);
	}
}