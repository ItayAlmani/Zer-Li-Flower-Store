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
import entities.Customer;
import entities.DataBase;
import entities.PaymentAccount;
import entities.Subscription;
import entities.Subscription.SubscriptionType;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import orderNproducts.entities.Store;

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
		ParentGUIController.currentGUI=this;
		cbSub.setItems(FXCollections.observableArrayList(SubscriptionType.values()));
		try {
			Context.mainScene.setMenuPaneDisable(true);
			Context.fac.customer.getAllCustomers();
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	/**
	 * Function get {@link ArrayList} of {@link Customer}s from {@link DataBase} and
	 * set them in the {@link ComboBox}
	 * @param customers - {@link ArrayList} from {@link DataBase}
	 */
	public void setCustomers(ArrayList<Customer> customers) {
		Context.mainScene.setMenuPaneDisable(false);
		if(Platform.isFxApplicationThread())
			cbCustomers.setItems(FXCollections.observableArrayList(customers));
		else
			Platform.runLater(()->cbCustomers.setItems(FXCollections.observableArrayList(customers)));
	}
	
	/**
	 * Function check if the selected {@link Customer} have an exist {@link PaymentAccount}
	 * <p>
	 * if NO - show an appropriate message
	 * <p>
	 * if YES - check if the {@link Customer} have {@link Subscription}
	 * <p>
	 * if YES - show his details
	 */
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
	
	/**
	 * Function {@link Subscription} type from {@link ComboBox} 
	 * and create new {@link Subscription} for the selected {@link Customer}
	 */
	public void createSubscription() {
		try {
			SubscriptionType type = cbSub.getValue();
			if(type == null) {
				Context.mainScene.setMessage("Must select the subscription period");
				return;
			}
			if(this.sub == null) {
				this.sub = new Subscription(type);
				Context.mainScene.setMenuPaneDisable(true);
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
	
	/**
	 *Function get the new {@link Subscription} id and attach it to the {@link Customer}'s {@link PaymentAccount}
	 * @param id - new {@link Subscription} ID
	 */
	public void setSubID(BigInteger id) {
		Context.mainScene.setMenuPaneDisable(false);
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