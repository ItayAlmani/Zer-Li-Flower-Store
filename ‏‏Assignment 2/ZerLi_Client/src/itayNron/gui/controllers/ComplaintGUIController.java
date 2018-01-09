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
import entities.User;
import entities.Survey.SurveyType;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
	
	
	
public class ComplaintGUIController implements Initializable {
	
	private @FXML ComboBox<Customer> cbsCustomer;
	private @FXML ComboBox<Store> cbsStore;
	private @FXML ComboBox<Complaint> cbsComplaints;
	private @FXML TextArea complaintReason;
	private @FXML Button btnSendNewComplaint;
	private @FXML VBox vbxAddComplaint;
	private @FXML VBox vbxComplaint;
	private Complaint comp;
	
	@FXML public void showAddComplaint(ActionEvent event) throws IOException
	{
		vbxAddComplaint.setVisible(true);
	}
	
	@FXML public void showComplaint(ActionEvent event) throws IOException
	{
		vbxComplaint.setVisible(true);
	}
	
	
	@FXML public void sendComplaint (ActionEvent event) throws IOException
	{
		comp.setComplaintReason(complaintReason.getText());
		comp.setCustomerID(cbsCustomer.getValue().getCustomerID().toString());
		comp.setStoreID(cbsStore.getValue().getStoreID().toString());
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