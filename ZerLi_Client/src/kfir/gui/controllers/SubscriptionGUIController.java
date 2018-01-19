package kfir.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import entities.CreditCard;
import entities.Customer;
import entities.PaymentAccount;
import entities.Store;
import entities.Subscription;
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
	private Subscription sub = null;

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
			cbSub.setValue(null);
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
							this.sub=pa.getSub();
							btnSave.setText("Add");
							if(this.sub != null) {
								if(this.sub.getSubType() != null)
									cbSub.setValue(this.sub.getSubType());
								else
									Context.mainScene.ShowErrorMsg();
								
								//sub is valid
								if(Context.fac.sub.isSubValid(this.sub))
									btnSave.setText("Update");
							}
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
		try {
			SubscriptionType type = cbSub.getValue();
			if(type == null) {
				Context.mainScene.setMessage("Must select the subscription period");
				return;
			}
			if(this.sub == null) {
				this.sub = new Subscription(type);
				Context.fac.sub.add(this.sub, true);
			}
			else {
				//Nothing changed
				if(this.sub.getSubDate().equals(LocalDate.now()) && this.sub.getSubType().equals(type))
					return;
				this.sub.setSubDate(LocalDate.now());
				this.sub.setSubType(type);
				Context.fac.sub.update(this.sub);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setSubID(BigInteger id) {
		this.sub.setSubID(id);
		this.pa.setSub(this.sub);
		try {
			Context.fac.paymentAccount.update(pa);
			if(Platform.isFxApplicationThread())
				cbCustomers.setValue(null);
			else Platform.runLater(()->cbCustomers.setValue(null));
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
}