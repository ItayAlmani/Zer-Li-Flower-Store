package kfir.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import entities.Customer;
import entities.PaymentAccount;
import entities.Store;
import entities.Subscription.SubscriptionType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class SubscriptionGUIController implements Initializable {

	private @FXML JFXComboBox<Customer> cbCustomers;
	private @FXML VBox vboxPA;
	private @FXML JFXTextField txtCustID;
	private @FXML JFXComboBox<SubscriptionType> cbSub;
	private @FXML JFXButton btnSave;
	private Customer cust = null;
	private PaymentAccount pa = null;
	private Store store = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbSub.setItems(FXCollections.observableArrayList(SubscriptionType.values()));
		try {
			Context.fac.customer.getAllCustomers();
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		if(Platform.isFxApplicationThread())
			cbCustomers.setItems(FXCollections.observableArrayList(customers));
		else
			Platform.runLater(()->cbCustomers.setItems(FXCollections.observableArrayList(customers)));
	}
	
	public void customerSelected() {
		try {
			vboxPA.setVisible(false);
			btnSave.setVisible(false);
			Context.mainScene.clearMsg();
			this.cust= cbCustomers.getValue();
			if(this.cust!=null) {
				if(this.cust.getPrivateID() != null && !this.cust.getPrivateID().isEmpty()) {
					txtCustID.setText(this.cust.getPrivateID());
					ArrayList<PaymentAccount> pas = cust.getPaymentAccounts();
					this.store=Context.getUserAsStoreWorker().getStore();
					if(pas==null || pas.isEmpty()) {
						Context.mainScene.setMessage("Customer doesn't have an active Payment Account at all");
						return;
					}
					else {
						pa = Context.fac.paymentAccount.getPaymentAccountOfStore(cust.getPaymentAccounts(),store);
						if(pa==null) {
							Context.mainScene.setMessage("Customer doesn't have an active Payment Account at " + store.getName());
							return;
						}
						else {
							if(pa.getSub().getSubType())
							vboxPA.setVisible(true);
							btnSave.setVisible(true);
						}
					}
				}
			}
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	public void createSubscription() {
		
	}
}