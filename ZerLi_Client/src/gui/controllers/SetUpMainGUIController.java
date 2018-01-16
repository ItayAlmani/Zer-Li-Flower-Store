package gui.controllers;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import common.MainClient;
import entities.Customer;
import entities.PaymentAccount;
import entities.Store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public class SetUpMainGUIController extends LoadGUIController {
	protected void setUpInit() throws IOException {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		imgLogo1.setImage(MainClient.getLogoAsImage());
		imgLogo2.setImage(MainClient.getLogoAsImage());
	}
	
	protected void setUpCustomerMenu() {
		ArrayList<Store> stores = new ArrayList<>();
    	Customer cust;
		try {
			cust = Context.getUserAsCustomer();
			for (PaymentAccount pa : cust.getPaymentAccounts()) {
				stores.add(pa.getStore());
			}
			cbStores.setItems(FXCollections.observableArrayList(stores));
			cbStores.getSelectionModel().selectFirst();
			ObservableList<Node> child = hbChangingIcons.getChildren();
			child.clear();
			child.add(hbCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cbStores.setVisible(true);
	}
	
	protected void setUpStoreWorkerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(hbCustServiceData,hbOrders);
	}
	
	protected void setUpStoreManagerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(hbCustServiceData,hbOrders,hbCustomersInfo);
	}
	
	protected void setUpServiceExpertMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(hbCustServiceData,hbCustomersInfo);
	}
	
	protected void setUpCustomerServiceWorkerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(hbCustServiceData,hbCustomersInfo);
	}
	
	protected void setUpChainStoreWorkerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(hbCustServiceData,hbOrders,hbCustomersInfo);
	}
	
	protected void setUpChainStoreManagerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(hbCustServiceData,hbOrders,hbCustomersInfo);
	}

	protected void setUpToolTips() {
		Tooltip.install(icnCart, new Tooltip("Show my cart"));
		Tooltip.install(icnLogOut, new Tooltip("Log Out"));
		Tooltip.install(icnCatalog, new Tooltip("Show catalog"));
		Tooltip.install(icnReportSelector, new Tooltip("Show Report Selector"));
		Tooltip.install(icnAssemble, new Tooltip("Assemble product"));
		Tooltip.install(icnCancelOrder, new Tooltip("Cancel order"));
		Tooltip.install(icnSurvey, new Tooltip("Add survey answers"));
		Tooltip.install(icnSurveyReport, new Tooltip("Add survey report"));
		Tooltip.install(icnComplaints, new Tooltip("Add complaint"));
		Tooltip.install(icnProductsForm, new Tooltip("Show products form"));
		Tooltip.install(icnUpdateOrder, new Tooltip("Update order status"));
		Tooltip.install(icnManualTransaction, new Tooltip("Manual transaction"));
		Tooltip.install(icnUpdatePaymentAccount, new Tooltip("Update payment account"));
		
	}
}
