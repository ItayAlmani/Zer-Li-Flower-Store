package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.ClientConsole;
import common.Context;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.OrderStatus;
import entities.Order.Refund;
import entities.PaymentAccount;
import entities.ProductInOrder;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;

public class CancelOrderGUIController implements Initializable {

	private @FXML JFXComboBox<Order> cbsOrders;
	private @FXML JFXButton btnCancelOrder;
	private ObservableList<Order> paidOrders;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnCancelOrder.setOnAction(confirmCancelOrderEventHandler);
		try {
			Context.mainScene.setMenuPaneDisable(true);
			Context.fac.order.getCancelableOrdersByCustomerID(Context.getUserAsCustomer().getCustomerID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Context.mainScene.loadMainMenu("You don't have permission to this section");
		}

	}
/**
 * Function to set untreated orders from DB into comboBox

 * @param ord - arrayList of orders to check if they can be canceled. in case they are, add them to comboBox
 */
	public void setOrders(ArrayList<Order> ord) {
		Context.mainScene.setMenuPaneDisable(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (ord.isEmpty() == false) {
					paidOrders = FXCollections.observableArrayList(ord);
					cbsOrders.setItems(paidOrders);
					cbsOrders.setDisable(false);
				} 
				else
					Context.mainScene.setMessage("No orders to cancel");
			}
		});
	}
	
	/**
	 * {@link EventHandler} which pop an {@link Dialog} when {@link #btnCancelOrder} clicked
	 * and will confirm if want to cancel it
	 */
	private final EventHandler<ActionEvent> confirmCancelOrderEventHandler = actEvent -> {
		Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel this order?");
		Button exitButton = (Button) confirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("Cancel Order");
		confirmation.setHeaderText("Confirm Order Cancellation");
		confirmation.initModality(Modality.APPLICATION_MODAL);
		confirmation.initOwner(ParentGUIController.primaryStage);

		confirmation.setX(ParentGUIController.primaryStage.getX());
		confirmation.setY(ParentGUIController.primaryStage.getY());

		Optional<ButtonType> closeResponse = confirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get()))
			actEvent.consume();
		else {
			try {
				cancelOrder();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Function to able {@link Customer} to cancel {@link Order}
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
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
			Context.mainScene.setMessage(msg);
			if (paidOrders.isEmpty() == false) {
				cbsOrders.setDisable(false);
			}
			else
				Context.mainScene.setMessage("No orders to cancel");
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().isEmpty()==false)
				System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Function to enable the <b>cancel</b> button. <br>
	 * If {@link Customer} doesn't have {@link Order}s to cancel or {@link Order} hasn't been selected from {@link comboBox} ,</br>don't enable the cancel button.
	 */
	public void orderSelected() {
		if (cbsOrders.getValue() != null)
			btnCancelOrder.setVisible(true);
	}
}