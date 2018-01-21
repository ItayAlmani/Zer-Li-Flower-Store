package kfir.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class PaymentAccountManagmentGUIController implements Initializable  {

	private @FXML JFXComboBox<Customer> cbCustomers;
	
	private @FXML JFXTextField txtCardNUM, txtMonth ,txtYear, txtcardCVV;
	private @FXML JFXTextField lblIdNUM;
	private @FXML JFXButton btnSave;
	private Customer cust = null;
	private PaymentAccount pa = null;
	private CreditCard cc = null;
	private Store store = null;
	
	private static String EMPTY_STR = "";
	
	/** false <=> {@link PaymentAccount} need to be updated.<br>
	 * true <=> {@link PaymentAccount} need to be created.*/
	private boolean pa_need_to_be_updated = true;
	
	
	private @FXML VBox vboxPA;
	

	public void initialize(URL location, ResourceBundle resources) {
		setTextFieldsListeners(txtCardNUM, 16);
		setTextFieldsListeners(txtMonth, 2);
		setTextFieldsListeners(txtYear, 2);
		setTextFieldsListeners(txtcardCVV, 3);
		
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

	public void openPaymentAccount() {
		try {
			btnSave.setDisable(false);
			Context.mainScene.clearMsg();
			this.cust= cbCustomers.getValue();
			if(this.cust!=null) {
				if(this.cust.getPrivateID() != null && !this.cust.getPrivateID().isEmpty()) {
					lblIdNUM.setText(this.cust.getPrivateID());
					ArrayList<PaymentAccount> pas = cust.getPaymentAccounts();
					this.store=Context.getUserAsStoreWorker().getStore();
					if(pas==null || pas.isEmpty()) {
						Context.mainScene.setMessage("Customer doesn't have an active Payment Account at all");
						setTextInTF(EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, false, "Add");
					}
					else {
						pa = Context.fac.paymentAccount.getPaymentAccountOfStore(cust.getPaymentAccounts(),store);
						CreditCard cc = pa.getCreditCard();
						if(pa!=null && cc!=null) {
							String numStr = cc.getCcNumber(), monthStr = cc.getCcValidity().split("/")[0], 
									yearStr = cc.getCcValidity().split("/")[1],
									cvvStr = cc.getCcCVV();
							//Have payment account at specific store
							if(cc.getCcID()!=null && 
									numStr !=null && !numStr.isEmpty() &&
									monthStr !=null && !monthStr.isEmpty() &&
									yearStr !=null && !yearStr.isEmpty() &&
									cvvStr!=null && !cvvStr.isEmpty())
								setTextInTF(numStr, monthStr, yearStr, cvvStr, true, "Update");
							//ELSE
							else
								setTextInTF(EMPTY_STR, EMPTY_STR, EMPTY_STR, EMPTY_STR, false, "Add");
						}
					}
					btnSave.setVisible(true);
					vboxPA.setVisible(true);
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
	
	private void setTextInTF(String num, String month, String year, String cvv, boolean pa_to_update, String btnText) {
		txtCardNUM.setText(num);
		txtMonth.setText(month);
		txtYear.setText(year);
		txtcardCVV.setText(cvv);
		pa_need_to_be_updated = pa_to_update;
		btnSave.setText(btnText);
	}
	
	public void savePaymentAccount() {
		try {
			btnSave.setDisable(true);
			String numStr = txtCardNUM.getText(), monthStr = txtMonth.getText(), 
					yearStr = txtYear.getText(), cvvStr = txtcardCVV.getText();
			if(numStr == null || numStr.isEmpty() || monthStr == null || monthStr.isEmpty() ||
					yearStr == null || yearStr.isEmpty() || cvvStr == null || cvvStr.isEmpty()) {
				Context.mainScene.setMessage("Some fields are emtpy!");
				return;
			}
			if(monthStr.length()==1)	monthStr = "0" + monthStr;
			if(yearStr.length()==1)		yearStr =  "0" + yearStr;
			if(this.pa == null)
				this.pa = new PaymentAccount(this.cust.getCustomerID(),this.store);
			this.cc=new CreditCard(numStr,monthStr+"/"+yearStr,cvvStr);
			Context.fac.creditCard.getCreditCardByNumber(numStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setCards(ArrayList<CreditCard> ccs) {
		//this credit card doesn't exist
		if(ccs==null || ccs.isEmpty()) {
			try {
				Context.fac.creditCard.add(this.cc, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(ccs.size() == 1) {
			try {
				//Update the existing credit card with new validity and cvv if changed
				this.cc.setCcID(ccs.get(0).getCcID());
				if(this.cc.getCcValidity().equals(ccs.get(0).getCcValidity()) == false ||
						this.cc.getCcCVV().equals(ccs.get(0).getCcCVV()) == false)
					Context.fac.creditCard.update(this.cc);
				setCredCardID(ccs.get(0).getCcID());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			Context.mainScene.setMessage("Error with credit card");
	}
	
	public void setCredCardID(BigInteger id){
		if(this.pa == null) {
			Context.mainScene.ShowErrorMsg();
			return;
		}
		this.cc.setCcID(id);
		this.pa.setCreditCard(cc);
		try {
			if(pa_need_to_be_updated == false)
				Context.fac.paymentAccount.add(pa, false);
			else
				Context.fac.paymentAccount.update(pa);
			if(Platform.isFxApplicationThread())
				updateView();
			else Platform.runLater(()->updateView());
		} catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	private void updateView() {
		cbCustomers.setValue(null);
		if(pa_need_to_be_updated==false)
			cust.addPaymentAccount(pa);		
	}
	
	/**
	 * 
	 * @param tf
	 * @param maxLength the maximum length of value. if equals -1, no limit.
	 */
	private void setTextFieldsListeners(JFXTextField tf, int maxLength) {
		//Listener only digits
		tf.lengthProperty().addListener((observable,oldValue,newValue)-> {
			if (newValue.intValue() > oldValue.intValue()) {
				String txt = tf.getText();
				char ch = txt.charAt(oldValue.intValue());
				// Check if the new character is the number or other's
				if (!(ch >= '0' && ch <= '9'))
					// if it's not number then just setText to previous one
					tf.setText(txt.substring(0, txt.length() - 1));
			}
		});
		
		tf.textProperty().addListener((observable,oldValue,newValue)-> {
			if (tf.getText().length() > maxLength)
				tf.setText(tf.getText().substring(0, maxLength));
		});
	}
}
