package kfir.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import entities.CreditCard;
import entities.Customer;
import entities.PaymentAccount;
import entities.Product;
import entities.Store;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	
	private @FXML JFXTextField txtCardNUM, txtcardValidity, txtcardCVV;
	private @FXML JFXTextField lblCustName, lblIdNUM;
	private @FXML JFXButton btnSave;
	private Customer cust = null;
	private PaymentAccount pa = null;
	private CreditCard cc = null;
	private Store store = null;
	
	/** false <=> {@link PaymentAccount} need to be updated.<br>
	 * true <=> {@link PaymentAccount} need to be created.*/
	private boolean pa_need_to_be_updated = true;
	
	
	private @FXML VBox vboxPA;
	

	public void initialize(URL location, ResourceBundle resources) {
		//Listener only digits
		txtcardCVV.lengthProperty().addListener((observable,oldValue,newValue)-> {
			if (newValue.intValue() > oldValue.intValue()) {
				String ccvStr = txtcardCVV.getText();
				char ch = ccvStr.charAt(oldValue.intValue());
				// Check if the new character is the number or other's
				if (!(ch >= '0' && ch <= '9')) {
					// if it's not number then just setText to previous one
					txtcardCVV.setText(ccvStr.substring(0, ccvStr.length() - 1));
				}
			}
		});
		
		txtcardCVV.textProperty().addListener((observable,oldValue,newValue)-> {
			if (txtcardCVV.getText().length() > 3) {
				String s = txtcardCVV.getText().substring(0, 3);
				txtcardCVV.setText(s);
			}
		});
		getCustomerComboBox();
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
		if(Platform.isFxApplicationThread())
			cbCustomers.setItems(FXCollections.observableArrayList(customers));
		else
			Platform.runLater(()->cbCustomers.setItems(FXCollections.observableArrayList(customers)));
	}

	public void openPaymentAccount() {
		try {
			Context.mainScene.clearMsg();
			this.cust= cbCustomers.getValue();
			if(this.cust!=null) {
				if(this.cust.getFullName() != null && !this.cust.getFullName().isEmpty()
						&& this.cust.getPrivateID() != null && !this.cust.getPrivateID().isEmpty()) {
					lblCustName.setText(this.cust.getFullName());
					lblIdNUM.setText(this.cust.getPrivateID());
					ArrayList<PaymentAccount> pas = cust.getPaymentAccounts();
					this.store=Context.getUserAsStoreWorker().getStore();
					if(pas==null || pas.isEmpty()) {
						Context.mainScene.setMessage("Customer doesn't have an active Payment Account at all");
						btnSave.setText("Add");
						txtCardNUM.setText("");
						txtcardValidity.setText("");
						txtcardCVV.setText("");
						btnSave.setVisible(true);
						vboxPA.setVisible(true);
						pa_need_to_be_updated = false;
					}
					else {
						pa = Context.fac.paymentAccount.getPaymentAccountOfStore(cust.getPaymentAccounts(),store);
						CreditCard cc = pa.getCreditCard();
						if(pa!=null && cc!=null) {
							//Have payment account at specific store
							if(cc.getCcID()!=null && 
									cc.getCcNumber()!=null && !cc.getCcNumber().isEmpty() &&
									cc.getCcValidity()!=null && !cc.getCcValidity().isEmpty() &&
									cc.getCcCVV()!=null && !cc.getCcCVV().isEmpty()) {
								Context.mainScene.setMessage("Customer have already a Credit Card in data base");
								txtCardNUM.setText(cc.getCcNumber());
								txtcardValidity.setText(cc.getCcValidity());
								txtcardCVV.setText(cc.getCcCVV());
								pa_need_to_be_updated = true;
								btnSave.setText("Update");
							}
							//ELSE
							else {
								txtCardNUM.setText("");
								txtcardValidity.setText("");
								txtcardCVV.setText("");
								btnSave.setText("Add");
								pa_need_to_be_updated = false;
							}
						}
						btnSave.setVisible(true);
						vboxPA.setVisible(true);
					}
				}
				else
					Context.mainScene.ShowErrorMsg();
			}
			else
				vboxPA.setVisible(false);
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}

	public void savePaymentAccount() 
	{
		try {
			//============ADD INPUT CHECK : if(getText!=null && getText.isEmpty == false)
			if(this.pa == null) {
				this.pa = new PaymentAccount(this.cust.getCustomerID(),this.store);
				
				//=========CHECK CREDIT CARD EXISTANCE
			}
			this.cc=new CreditCard(txtCardNUM.getText(),txtcardValidity.getText(),txtcardCVV.getText());
			Context.fac.creditCard.add(this.cc, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCredCardID(BigInteger id) throws IOException {
		if(this.pa == null) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		this.cc.setCcID(id);
		this.pa.setCreditCard(cc);
		if(pa_need_to_be_updated == false)
			Context.fac.paymentAccount.add(pa, false);
		else
			Context.fac.paymentAccount.update(pa);
		if(Platform.isFxApplicationThread())
			updateView();
		else Platform.runLater(()->updateView());
	}
	
	private void updateView() {
		cbCustomers.setValue(null);
		if(pa_need_to_be_updated==false)
			cust.addPaymentAccount(pa);		
	}
}
