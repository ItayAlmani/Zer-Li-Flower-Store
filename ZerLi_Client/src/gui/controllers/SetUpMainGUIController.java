package gui.controllers;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import common.MainClient;
import entities.Customer;
import entities.PaymentAccount;
import entities.Store;
import entities.User.UserType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public class SetUpMainGUIController extends LoadGUIController {
	
	/** the first String will be the permission, and the second is the full name*/
	private final static String welcome_message = "Welcome %s %s"; 
	
	protected void setUpInit() throws IOException {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		imgLogo1.setImage(MainClient.getLogoAsImage());
		imgLogo2.setImage(MainClient.getLogoAsImage());
	}
	
	protected void setWelcomeLabel(UserType perm, String user_full_name) {
		if(Platform.isFxApplicationThread())
			lblUserWelcome.setText(String.format(welcome_message, perm.toString(),user_full_name));
		else Platform.runLater(()->lblUserWelcome.setText(String.format(welcome_message, perm.toString(),user_full_name)));
	}
	
	protected void setUpCustomerMenu() {
		ArrayList<Store> stores = new ArrayList<>();
    	Customer cust;
		try {
			cust = Context.getUserAsCustomer();
			for (PaymentAccount pa : cust.getPaymentAccounts())
				stores.add(pa.getStore());
			ObservableList<Node> hbc = hbCustomer.getChildren();
			//Customer with no payment accounts
			if(stores.isEmpty()) {
				cbStores.setVisible(false);
				hbc.clear();
				if(!hbc.add(paneNoPA))
					System.err.println("Can't add icons");
			}
			else {
				if(hbc.contains(panePA)==false)
					hbc.add(panePA);
				cbStores.setItems(FXCollections.observableArrayList(stores));
				cbStores.getSelectionModel().selectFirst();
				cbStores.setVisible(true);
			}
			ObservableList<Node> child = hbChangingIcons.getChildren();
			child.clear();
			child.add(hbCustomer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void setUpStoreWorkerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(paneStore);
	}
	
	protected void setUpStoreManagerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(paneStore, paneStrManager, btnReportSelector, btnUpdateCatalog);
	}
	
	protected void setUpServiceExpertMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(btnSurveyReport);
	}
	
	protected void setUpCustomerServiceWorkerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(btnComplaints);
	}
	
	protected void setUpChainStoreWorkerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(btnUpdateCatalog, btnUpdateUsers);
	}
	
	protected void setUpChainStoreManagerMenu() {
		ObservableList<Node> child = hbChangingIcons.getChildren();
		child.clear();
		child.addAll(btnReportSelector, btnUpdateCatalog, btnUpdateUsers);
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
		Tooltip.install(icnUpdatePaymentAccount, new Tooltip("Manage payment account"));
		Tooltip.install(icnSub, new Tooltip("Manage subscriptions"));
		Tooltip.install(icnAccounts, new Tooltip("Manage User's accounts"));
	}
	
	public void setMenuPaneDisable(boolean disable) {
		if(Platform.isFxApplicationThread()) {
			menu.setDisable(disable);
		}
		else Platform.runLater(()->{
			menu.setDisable(disable);
		});
	}
}
