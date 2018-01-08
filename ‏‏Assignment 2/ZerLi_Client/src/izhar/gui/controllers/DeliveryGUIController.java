package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import entities.DeliveryDetails;
import entities.Order.DeliveryType;
import entities.Product;
import entities.ShipmentDetails;
import entities.Stock;
import entities.Store;
import entities.Store.StoreType;
import entities.StoreWorker;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.CssMetaData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

public class DeliveryGUIController implements Initializable {

	private @FXML JFXButton btnSend;
	private @FXML JFXTextField txtStreet, txtCity, txtPostCode, txtName, txtPhoneAreaCode, txtPhonePost;
	private @FXML JFXComboBox<Store> cbStores;
	private @FXML ToggleGroup tGroup;
	private @FXML JFXRadioButton rbShipment, rbPickup;
	private @FXML VBox vboxForm, paneShipment;
	
	private ArrayList<Store> stores;
	private Store selectedStore;
	private @FXML MaterialDesignIconView icnPickup, icnShipment;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		addTextLimiter(txtPhoneAreaCode, 3);
		addTextLimiter(txtPhonePost, 7);
		addTextLimiter(txtPostCode, 7);
		
		tGroup= new ToggleGroup();
		rbPickup.setUserData("Pickup");
		rbPickup.setToggleGroup(tGroup);
		rbShipment.setUserData("Shipment");
		rbShipment.setToggleGroup(tGroup);
		
		try {
			Context.fac.store.getAllStores();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tGroup.selectedToggleProperty().addListener(e-> vboxForm.setVisible(true));
	}
	
	public void setStocks(ArrayList<Stock> stocks) throws IOException {
		Store str = null;
		if(stocks == null || stocks.size()==0) 
			return;
		for (Store store : stores) {
			if(store.getStoreID().equals(stocks.get(0).getStoreID())) {
				str=store;
				break;
			}
		}
		if(str != null)
			str.setStock(stocks);
	}

	public void showPickup(ActionEvent event) {
		Context.mainScene.setMessage("");
		icnPickup.setFill(Color.ORANGE);
		icnShipment.setFill(Color.RED);
		btnSend.setDisable(false);
		this.paneShipment.setVisible(false);
		this.cbStores.setVisible(true);
	}
	
	public void showShipment() {
		Context.mainScene.setMessage("");
		icnShipment.setFill(Color.ORANGE);
		icnPickup.setFill(Color.RED);
		btnSend.setDisable(false);
		this.cbStores.setVisible(false);
		this.paneShipment.setVisible(true);
		this.cbStores.getSelectionModel().clearSelection();
		
		ArrayList<Store> ordOnly = Context.fac.store.getOrdersOnlyStoresFromArrayList(stores);
		if(ordOnly==null || ordOnly.size()<=0) {
			Context.mainScene.setMessage("Shipment not available right now!");
			btnSend.setDisable(true);
			return;
		}
		Product outOfStockProd = null;
		do {
			selectedStore=ordOnly.get(0);
			outOfStockProd=checkStockInStore();
			ordOnly.remove(0);
		} while (outOfStockProd != null && ordOnly!=null && ordOnly.size()>0);
		
		if(outOfStockProd == null)	return;
		
		Product prod = outOfStockProd;
		
		//There is at least one product out of stock
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnSend.setDisable(true);
				Context.mainScene.setMessage("The product "+prod.getName()
				+"is out of stock.\nPlease replace the product.");
			}
		});
	}

	public void addDelivery() {
		Object userData = tGroup.getSelectedToggle().getUserData();
		if(userData.equals("Pickup")==false && userData.equals("Shipment")==false) {
			Context.mainScene.setMessage("Must choose at least one option");
			return;
		}
		if(selectedStore==null) {
			if(userData.equals("Pickup"))
				Context.mainScene.setMessage("Must choose store for pickup");
			else if(userData.equals("Shipment"))
				Context.mainScene.setMessage("Error");
			return;
		}
		
		DeliveryDetails del = new DeliveryDetails(Context.order.getOrderID(),selectedStore);
		if(userData.equals("Pickup")) {
			Context.order.setDelivery(del);
			if(Context.order.getDeliveryType() != null &&
					Context.order.getDeliveryType().equals(DeliveryType.Shipment))
				Context.order.addToFinalPrice(-1*ShipmentDetails.shipmentPrice);
			Context.order.setDeliveryType(DeliveryType.Pickup);
		}
		else{	//Shipment
			ShipmentDetails ship = new ShipmentDetails(del, 
					txtStreet.getText(), txtCity.getText(),
					txtPostCode.getText(), txtName.getText(),
					txtPhoneAreaCode.getText()+"-"+txtPhonePost.getText());
			Context.order.setDelivery(ship);
			Context.order.setDeliveryType(DeliveryType.Shipment);
			try {
				Context.fac.order.updatePriceWithShipment(Context.order);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Context.mainScene.loadGUI("OrderTimeGUI", false);
	}

	public Product checkStockInStore() {
		Object userData = tGroup.getSelectedToggle().getUserData();
		if(userData.equals("Pickup"))
			selectedStore=cbStores.getValue();
		if(selectedStore == null)	return null;
		
		Product outOfStockProd = Context.fac.stock.checkStockByOrder(Context.order, selectedStore);
		if(outOfStockProd==null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Context.mainScene.setMessage("");
					btnSend.setDisable(false);
				}
			});
			return null;
		}
		else { //There is at least one product out of stock
			if(userData.equals("Pickup")) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {					
						btnSend.setDisable(true);
						String show = "The product " 
									+outOfStockProd.getName() +" is out of stock at store "
									+ selectedStore.getName()+".\nYou can replace store or product";
						Context.mainScene.setMessage(show);
					}
				});
			}
			return outOfStockProd;
		}
	}
	
	public void setStores(ArrayList<Store> stores) {
		this.stores=stores;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbStores.setItems(FXCollections.observableArrayList(Context.fac.store.getPhysicalStoresFromArrayList(stores)));
			}
		});
		for (Store store : stores) {
			Context.askingCtrl.add(this);
			try {
				Context.fac.stock.getStockByStore(store.getStoreID());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void addTextLimiter(TextField tf, final int maxLength){
		tf.lengthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				  if (newValue.intValue() > oldValue.intValue()) {
					  char ch = tf.getText().charAt(oldValue.intValue());
					  // Check if the new character is the number or other's
					  if (!(ch >= '0' && ch <= '9' )) {
						   // if it's not number then just setText to previous one
						  tf.setText(tf.getText().substring(0,tf.getText().length()-1)); 
					  }
				 }
			}
		});
		if(maxLength!=-1) {
			tf.textProperty().addListener(new ChangeListener<String>() {
		        @Override
		        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
		            if (tf.getText().length() > maxLength) {
		                String s = tf.getText().substring(0, maxLength);
		                tf.setText(s);
		            }
		        }
		    });
		}
	}

	public void back() {
		Context.mainScene.loadGUI("CartGUI", false);
	}
}