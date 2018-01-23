package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.omg.Messaging.SyncScopeHelper;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import entities.Complaint;
import entities.Customer;
import entities.PaymentAccount;
import entities.Store;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class ComplaintGUIController implements Initializable {

	private @FXML JFXComboBox<Customer> cbsCustomer;
	private @FXML JFXComboBox<Store> cbsStore;
	private @FXML JFXComboBox<Complaint> cbsComplaints;
	private @FXML JFXTextArea complaintReason;
	private @FXML JFXButton btnSend, btnAddComplaint;
	private @FXML VBox vbxAddComplaint, vbxComplaint, mainPane;
	private @FXML Label lblCustomerName, lblStoreName, lblComplaintDate, lblCompReason;
	private @FXML JFXToggleButton tglRefund;
	private @FXML JFXTextField txtRefundAmount;
	private ObservableList<Node> mainCh;
	
	/**if all setXXX functions called counters*/
	private int sets_invoked_cnt = 0,
			sets_needed_cnt = 0;
	
	/**
	 * if {@code true}, {@code btnSend} clicked for adding new complaint.<br>
	 * else ({@code false}), {@code btnSend} clicked for  answering about a complaint
	 */
	private Boolean isNewComplaint = null;

	/**
	 * <p>
	 * Function to able adding complaint in GUI
	 * </p>
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	public void showAddComplaint() throws IOException {
		isNewComplaint=true;
		Context.mainScene.clearMsg();
		if(mainCh.contains(vbxComplaint))
			mainCh.remove(vbxComplaint);
		int ind = mainCh.size();
		if(mainCh.contains(btnSend)==false)
			mainCh.add(btnSend);
		else
			ind--;
		cbsComplaints.setValue(null);
		mainCh.add(ind,vbxAddComplaint);
	}
/**
 * <p>
 * 
 * </p>
 * @throws IOException
 */
	public void showComplaint() throws IOException {
		if(cbsComplaints.getValue()!=null) {
			Context.mainScene.setMessage("");
			isNewComplaint=false;
			if(mainCh.contains(vbxAddComplaint))
				mainCh.remove(vbxAddComplaint);
			int ind = mainCh.size();
			if(mainCh.contains(btnSend)==false)
				mainCh.add(btnSend);
			else
				ind--;
			if(mainCh.contains(vbxComplaint)==false)
				mainCh.add(ind,vbxComplaint);
			String pattern = "dd.MM.yyyy";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			LocalDateStringConverter conv = new LocalDateStringConverter(formatter, null);
			Complaint c = cbsComplaints.getValue();
			lblCustomerName.setText(c.getCustomer().toString());
			lblStoreName.setText(c.getStoreID().toString());
			lblComplaintDate.setText(conv.toString(c.getDate().toLocalDate()));
			lblCompReason.setText(c.getComplaintReason());
		}
	}


	/**
	 * <p>
	 * This function checks if this complaint is new or we tread old complaint: <br>
	 * 1. The first case, we created new complaint,the function will send it to the DB.<br>
	 * 2. The second case, we treating old complaint and want to update it in DB.
	 * </p>
	 * @throws IOException
	 */
	@FXML
	public void send() throws IOException {
		Context.mainScene.clearMsg();
		if(isNewComplaint) {
			boolean isMissing = false;
			if(complaintReason.getText()==null || complaintReason.getText().isEmpty()) {
				System.err.println("complaintReason is null\n");
				isMissing=true;
			}
			if (cbsCustomer.getValue()==null)
			{
				System.err.println("customer is null\n");
				isMissing=true;
			}
			if (cbsStore.getValue()==null)
			{
				System.err.println("store is null\n");
				isMissing=true;
			}
			if(isMissing)
			{
				Context.mainScene.setMessage("One or more fields are missing");
				return;
			}
			Complaint comp = new Complaint(complaintReason.getText(),
					cbsCustomer.getValue(),
					cbsStore.getValue().getStoreID(),
					LocalDateTime.now());
			Context.fac.complaint.add(comp, false);
		}
		else {
			boolean isUpdateComp=true;
			Complaint comp = cbsComplaints.getValue();
			if (comp==null)
			{
				Context.mainScene.ShowErrorMsg();
				return;
			}
			comp.setIsTreated(true);
			if(tglRefund.isSelected()&&
					txtRefundAmount.getText().isEmpty()==false)
			{
				try {
					float refAmount = Float.parseFloat(txtRefundAmount.getText());
					if(refAmount<=0)
						throw new NumberFormatException("amount must be positive value");
					comp.setIsRefunded(true);
					PaymentAccount pa = null;
					if(comp.getCustomer() !=null && 
							comp.getCustomer().getPaymentAccounts()!=null &&
							comp.getStoreID() != null)
						pa = Context.fac.paymentAccount.getPaymentAccountOfStore(
								comp.getCustomer().getPaymentAccounts(), 
								new Store(comp.getStoreID()));
					else {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					if(pa == null) {
						Context.mainScene.ShowErrorMsg();
						return;
					}
					pa.setRefundAmount(pa.getRefundAmount()+refAmount);
					Context.fac.paymentAccount.update(pa);
				}catch (NumberFormatException e){
					isUpdateComp=false;
					if(e.getMessage()!=null && e.getMessage().isEmpty()==false)
						Context.mainScene.setMessage(e.getMessage());
					else
						Context.mainScene.setMessage("Amount must be float value");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(tglRefund.isSelected() && txtRefundAmount.getText().isEmpty()) {
				isUpdateComp=false;
				Context.mainScene.setMessage("When refund toggle is on, must specify refund amount");
			}
			if(isUpdateComp)
				Context.fac.complaint.update(comp);
			
			
			
		}
	}

	/**
	 * <p>
	 * Function to able option of refund,depends upon customer service worker decision.
	 * </p>
	 */
	public void toggledRefund() {
		txtRefundAmount.setVisible(tglRefund.isSelected());
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mainCh=mainPane.getChildren();
		try {
			Context.mainScene.setMenuPaneDisable(true);
			sets_needed_cnt++;
			Context.fac.customer.getAllCustomers();
			sets_needed_cnt++;
			Context.fac.store.getAllStores();
			sets_needed_cnt++;	
			Context.fac.complaint.getNotTreatedComplaints();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainCh.removeAll(vbxAddComplaint,vbxComplaint,btnSend);
	}
	
	private void checkIfNeedDisableFalse() {
		sets_invoked_cnt++;
		if(sets_needed_cnt==sets_invoked_cnt)
			Context.mainScene.setMenuPaneDisable(false);
	}
	
/**
 * <p>
 * Function to set customers into comboBox 
 * </p>
 * @param cus - arrayList of customers to be added to comboBox from DB
 */
	public void setCustomers(ArrayList<Customer> cus) {
		checkIfNeedDisableFalse();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (cus != null && cus.isEmpty() == false)
					cbsCustomer.setItems(FXCollections.observableArrayList(cus));
				else
					Context.mainScene.ShowErrorMsg();
			}
		});
	}
	/**
	 * <p>
	 * Function to set stores into comboBox 
	 * </p>
	 * @param cus - arrayList of stores to be added to comboBox from DB
	 */
	public void setStores(ArrayList<Store> stores) {
		checkIfNeedDisableFalse();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (stores != null && stores.isEmpty() == false) {
					ObservableList<Store> sol = FXCollections.observableArrayList(stores);
					cbsStore.setItems(sol);
				}
				else
					Context.mainScene.ShowErrorMsg();
			}
		});
	}
	/**
	 * <p>
	 * Function to set complaints into comboBox 
	 * </p>
	 * @param cus - arrayList of complaints to be added to comboBox from DB
	 */
	public void setComplaints(ArrayList<Complaint> complaints) {
		checkIfNeedDisableFalse();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (complaints != null && complaints.isEmpty() == false)
					cbsComplaints.setItems(FXCollections.observableArrayList(complaints));
				else {
					Context.mainScene.setMessage("No pending complaints");
					cbsComplaints.setDisable(true);
				}
			}
		});
	}
	 /**
	  * <p>
	  * Function to show all details of selected customer from comboBox, from here we can treat the compalint.
	  * </p>
	  */
	public void customerSelected() {
		Customer cust = cbsCustomer.getValue();
		if(cust!=null) {
			ObservableList<Store> stores = cbsStore.getItems();
			stores.clear();
			for (PaymentAccount pa : cust.getPaymentAccounts()) {
				if(pa.getStore()!=null)
					stores.add(pa.getStore());
				else {
					Context.mainScene.ShowErrorMsg();
					stores.clear();
					return;
				}
			}
			if(stores.isEmpty()==false)
				cbsStore.setDisable(false);
			else {
				Context.mainScene.ShowErrorMsg();
				stores.clear();
			}
		}
	}
}