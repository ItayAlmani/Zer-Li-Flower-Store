package itayNron.gui.controllers;

import java.awt.TextArea;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Complaint;
import entities.Customer;
import entities.Store;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
	
	
	
public class ComplaintGUIController implements Initializable {
	
	private @FXML ComboBox<Customer> cbsCustomer;
	private @FXML ComboBox<Store> cbsStore;
	private @FXML ComboBox<Complaint> cbsComplaints;
	private @FXML ComboBox<String> cbsRefund;
	private @FXML TextArea complaintReason;
	private @FXML Button btnSendNewComplaint;
	private @FXML VBox vbxAddComplaint;
	private @FXML VBox vbxComplaint;
	private @FXML Label lblCustomerName,lblStoreName,lblComplaintDate,lblReason;
	private Complaint comp;
	
	private ObservableList<String> list;
	
	@FXML public void showAddComplaint(ActionEvent event) throws IOException
	{
		vbxAddComplaint.setVisible(true);
	}
	
	@FXML public void showComplaint(ActionEvent event) throws IOException
	{
		vbxComplaint.setVisible(true);
		lblCustomerName.setText(cbsComplaints.getValue().getCustomerID().toString());
		lblStoreName.setText(cbsComplaints.getValue().getStoreID().toString());
		lblComplaintDate.setText(cbsComplaints.getValue().getDate().toString());
		lblReason.setText(cbsComplaints.getValue().getComplaintReason());
		
	}
	
	
	@FXML public void sendComplaint (ActionEvent event) throws IOException
	{
		comp.setComplaintReason(complaintReason.getText());
		comp.setCustomerID(cbsCustomer.getValue().getCustomerID());
		comp.setStoreID(cbsStore.getValue().getStoreID());
		comp.setDate(LocalDateTime.now());
		Context.fac.complaint.add(comp, false);
	}
	/**
	 * 
	 * @param txtComplaint
	 */
	public void attachComplaint(TextField txtComplaint) {
		// TODO - implement ComplaintGUI.attachComplaint
		throw new UnsupportedOperationException();
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ParentGUIController.currentGUI = this;
		complaintReason.setText(null);
		try {
			Context.fac.customer.getAllCustomers();
			Context.fac.store.getAllStores();
			Context.fac.complaint.getNotTreatedComplaints();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> ar = new ArrayList<>();
		ar.add("Yes");
		ar.add("No");
		list =FXCollections.observableArrayList(ar);
		cbsRefund.setItems(list);
		
	}
	
	public void setCustomers(ArrayList<Customer> cus) {
		ArrayList<Customer> cusArr = new ArrayList<>();
		for (Customer customer : cusArr) {
			cusArr.add(customer);
		}
		cbsCustomer.setItems(FXCollections.observableArrayList(cusArr));}
	
	public void setStores(ArrayList<Store> stores) {
		ArrayList<Store> stoArr= new ArrayList<>();
		for (Store store : stores) {
			stoArr.add(store);
		}
		cbsStore.setItems(FXCollections.observableArrayList(stoArr));
	}
	
	public void setComplaints(ArrayList<Complaint> complaints) {
		ArrayList<Complaint> compArr= new ArrayList<>();
		for (Complaint complaint : complaints) {
			compArr.add(complaint);
		}
		cbsComplaints.setItems(FXCollections.observableArrayList(compArr));
	}
}