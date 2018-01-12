package kfir.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.CreditCard;
import entities.Customer;
import entities.PaymentAccount;
import entities.Product;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ShowCustomersGUIController implements Initializable  {

	private @FXML ComboBox<Customer> cbCustomers;
	static Customer selcted;
	
	private @FXML TextField txtCardNUM;
	private @FXML TextField txtIdNUM;
	private @FXML TextField txtcardValidity;
	private @FXML TextField txtcardCVV;
	private @FXML Button Psave;
	private @FXML Button Pback;
	private @FXML Label lblMsg;
	private Customer cust;
	private @FXML TextField txtCustName;
	@FXML VBox vboxPA;
	

	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		getCustomerComboBox();
		cbCustomers.setStyle("-fx-font-size:10");
	}
	
	private void getCustomerComboBox() {
		try {
			Context.fac.customer.getAllCustomers();
		} catch (IOException e) {
			System.err.println("ProdForm");
			e.printStackTrace();
		}
	}
	
	public void setCustomers(ArrayList<Customer> customers) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbCustomers.setItems(FXCollections.observableArrayList(customers));
			}
		});
	}

	public void openPaymentAccount() {
		this.cust= cbCustomers.getValue();
		if(this.cust!=null) {
			txtCustName.setText(this.cust.getFullName());
			txtIdNUM.setText(this.cust.getPrivateID());
			if(this.cust.getPaymentAccounts()==null)
				Context.mainScene.setMessage("Customer doesn't have an active Payment Account");
			/*else if(this.cust.getPaymentAccounts().getCreditCard()!=null && 
					this.cust.getPaymentAccounts().getCreditCard().getCcID()!=null) {
				Context.mainScene.setMessage("Customer have already a Credit Card in data base");
			}*/
			else {
				//txtCardNUM.setText(cust.getPaymentAccount().getCreditCard());
				vboxPA.setVisible(true);
			}
		}
	}

	public void savePaymentAccount() 
	{
		try {
			Context.fac.creditCard.add(new CreditCard(txtCardNUM.getText(),txtcardValidity.getText(),txtcardCVV.getText()),
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCredCardID(BigInteger id) {
		for (PaymentAccount pa : this.cust.getPaymentAccounts()) {
			pa.setCreditCard(new CreditCard(id));
			try {
				Context.fac.paymentAccount.update(pa);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML public void back() {
		Context.mainScene.loadMainMenu();
	}

}
