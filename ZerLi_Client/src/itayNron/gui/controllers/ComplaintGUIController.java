package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import entities.Complaint;
import entities.Customer;
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
	private @FXML TextArea complaintReason;
	private @FXML JFXButton btnSend, btnAddComplaint;
	private @FXML VBox vbxAddComplaint, vbxComplaint, mainPane;
	private @FXML Label lblCustomerName, lblStoreName, lblComplaintDate, lblCompReason;
	private @FXML HBox paneRefund;
	private @FXML JFXToggleButton tglRefund;
	private ObservableList<Node> mainCh;
	
	/**
	 * if {@code true}, {@code btnSend} clicked for adding new complaint.<br>
	 * else ({@code false}), {@code btnSend} clicked for  answering about a complaint
	 */
	private Boolean isNewComplaint = null;

	public void showAddComplaint() throws IOException {
		isNewComplaint=true;
		if(mainCh.contains(vbxComplaint))
			mainCh.remove(vbxComplaint);
		int ind = mainCh.size();
		if(mainCh.contains(btnSend)==false)
			mainCh.add(btnSend);
		else
			ind--;
		cbsComplaints.setValue(null);
		mainCh.add(ind,vbxAddComplaint);
		ParentGUIController.primaryStage.getScene().getWindow().sizeToScene();
	}

	public void showComplaint() throws IOException {
		if(cbsComplaints.getValue()!=null) {
			isNewComplaint=false;
			if(mainCh.contains(vbxAddComplaint))
				mainCh.remove(vbxAddComplaint);
			int ind = mainCh.size();
			if(mainCh.contains(btnSend)==false)
				mainCh.add(btnSend);
			else
				ind--;
			mainCh.add(ind,vbxComplaint);
			String pattern = "dd.MM.yyyy";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			LocalDateStringConverter conv = new LocalDateStringConverter(formatter, null);
			Complaint c = cbsComplaints.getValue();
			lblCustomerName.setText(c.getCustomerID().toString());
			lblStoreName.setText(c.getStoreID().toString());
			lblComplaintDate.setText(conv.toString(c.getDate().toLocalDate()));
			lblCompReason.setText(c.getComplaintReason());
			ParentGUIController.primaryStage.getScene().getWindow().sizeToScene();
		}
	}

	@FXML
	public void send() throws IOException {
		if(isNewComplaint) {
			if(complaintReason.getText()==null) {
				System.err.println("complaintReason is null\n");
				return;
			}
			Complaint comp = new Complaint(complaintReason.getText(),
					cbsCustomer.getValue().getCustomerID(),
					cbsStore.getValue().getStoreID(),
					LocalDateTime.now());
			Context.fac.complaint.add(comp, false);
		}
		else {
			
		}
	}

	/**
	 * 
	 * @param txtComplaint
	 */
	public void attachComplaint(TextField txtComplaint) {
		// TODO - implement ComplaintGUI.attachComplaint
		throw new UnsupportedOperationException();
	}

	public void toggledRefund() {
		paneRefund.setVisible(tglRefund.isSelected());
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		mainCh=mainPane.getChildren();
		try {
			Context.fac.customer.getAllCustomers();
			Context.fac.store.getAllStores();
			Context.fac.complaint.getNotTreatedComplaints();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainCh.removeAll(vbxAddComplaint,vbxComplaint,btnSend);
	}

	public void setCustomers(ArrayList<Customer> cus) {
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

	public void setStores(ArrayList<Store> stores) {
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

	public void setComplaints(ArrayList<Complaint> complaints) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (complaints != null && complaints.isEmpty() == false)
					cbsComplaints.setItems(FXCollections.observableArrayList(complaints));
				else
					Context.mainScene.ShowErrorMsg();
			}
		});
	}
}