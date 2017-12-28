package izhar.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import common.Context;
import entities.DeliveryDetails;
import entities.Product;
import entities.ShipmentDetails;
import entities.Stock;
import entities.Store;
import entities.Store.StoreType;
import entities.StoreWorker;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DeliveryGUIController extends ParentGUIController {

	private @FXML Button btnSend;
	private @FXML TextField txtStreet, txtCity, txtPostCode, txtName, txtPhoneAreaCode, txtPhonePost;
	private @FXML ComboBox<String> cbStores;
	private @FXML GridPane gpShipment, gpPickup;
	private @FXML ToggleGroup tGroup;
	private @FXML RadioButton rbShipment, rbPickup;
	private @FXML VBox vboxForm;
	
	private ArrayList<Store> stores;
	private Store selectedStore;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		addTextLimiter(txtPhoneAreaCode, 3);
		addTextLimiter(txtPhonePost, 7);
		addTextLimiter(txtPostCode, 7);

		
		tGroup= new ToggleGroup();
		rbPickup.setUserData("Pickup");
		rbPickup.setToggleGroup(tGroup);
		rbShipment.setUserData("Shipment");
		rbShipment.setToggleGroup(tGroup);
		
		//FOR TEST!!!! TAKE DOWN!
		autoAddForTest();
		
		/*tGroup.selectedToggleProperty().addListener(
				(observable, oldVal, newVal) -> vboxForm.setVisible(true));*/
		tGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				vboxForm.setVisible(true);
			}
		});
		
		//Context.fac.store.getAllStores();
	}

	public void showPickup(ActionEvent event) {
		this.gpShipment.setVisible(false);
		this.gpPickup.setVisible(true);
	}
	
	public void showShipment(ActionEvent event) {
		this.gpPickup.setVisible(false);
		this.gpShipment.setVisible(true);
		this.cbStores.getSelectionModel().clearSelection();
		
		ArrayList<Store> ordOnly = Context.fac.store.getOrdersOnlyStoresFromArrayList(stores);
		if(ordOnly==null || ordOnly.size()<=0) return;
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
				lblMsg.setText("The product "+prod.getName()
				+"is out of stock.\nPlease replace the product.");
			}
		});
	}

	public void addDelivery(ActionEvent event) {
		Object userData = tGroup.getSelectedToggle().getUserData();
		if(userData.equals("Pickup")==false && userData.equals("Shipment")==false)
			return;
		
		DeliveryDetails del = new DeliveryDetails(Context.order.getOrderID(),selectedStore);
		if(userData.equals("Pickup")) 
			Context.order.setDelivery(del);
		else{	//Shipment
			ShipmentDetails ship = new ShipmentDetails(del, 
					txtStreet.getText(), txtCity.getText(),
					txtPostCode.getText(), txtName.getText(),
					txtPhoneAreaCode.getText()+"-"+txtPhonePost.getText());
			Context.order.setDelivery(ship);
			try {
				Context.fac.order.updatePriceWithShipment(Context.order);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			loadGUI("OrderTimeGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}

	public Product checkStockInStore() {
		Object userData = tGroup.getSelectedToggle().getUserData();
		if(userData.equals("Pickup")) {
			String stName = cbStores.getValue();
			for (Store store : stores) {
				if(store.getName().equals(stName)) {
					selectedStore = store;
					break;
				}
			}
		}
		if(selectedStore == null)	return null;
		
		Product outOfStockProd = Context.fac.store.checkStockByOrder(Context.order, selectedStore);
		if(outOfStockProd==null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblMsg.setText("In Stock at store "+selectedStore.getName());
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
						
						lblMsg.setText(show);
					}
				});
			}
			return outOfStockProd;
		}
	}
	
	
	private void autoAddForTest() {
		txtStreet.setText("Dafna 5");
		txtCity.setText("Migdal HaEmek");
		txtPostCode.setText("23508");
		txtName.setText("Izhar Ananiev");
		txtPhoneAreaCode.setText("054");
		txtPhonePost.setText("3088241");
		
		stores = new ArrayList<>();
		Store[] stArr = new Store[4];
		
		StoreWorker sw = new StoreWorker(1,3);
		ArrayList<Stock> stock = new ArrayList<>();
		stock.add(new Stock(1,new Product(1, "Red Anemone Bouquet"),5,1));
		stArr[0] = addNewStore(1,"Karmiel",StoreType.Physical,sw,stock);
		
		stock = new ArrayList<>();
		stock.add(new Stock(1,new Product(1, "Red Anemone Bouquet"),5,2));
		stock.add(new Stock(2,new Product(2, "Lilies Bouquet"),5,2));
		stock.add(new Stock(3,new Product(3, "Sunflower Bouqute"),5,2));
		stArr[1] = addNewStore(2,"Migdal HaEmek",StoreType.Physical,sw,stock);
		
		stock = new ArrayList<>();
		stock.add(new Stock(5,new Product(1, "Red Anemone Bouquet"),5,2));
		stock.add(new Stock(6,new Product(2, "Lilies Bouquet"),5,2));
		stock.add(new Stock(7,new Product(3, "Sunflower Bouqute"),1,2));
		stArr[2] = addNewStore(3,"Orders1",StoreType.OrdersOnly,sw,stock);
		
		stock = new ArrayList<>();
		stock.add(new Stock(5,new Product(1, "Red Anemone Bouquet"),2,2));
		stock.add(new Stock(6,new Product(2, "Lilies Bouquet"),3,2));
		stock.add(new Stock(7,new Product(3, "Sunflower Bouqute"),3,2));
		stArr[3] = addNewStore(4,"Orders2",StoreType.OrdersOnly,sw,stock);
		
		setStores(stores);
	}
	
	private Store addNewStore(int id, String name, StoreType type, StoreWorker sw,
			ArrayList<Stock> stocks) {
		Store store = new Store(id,name,type,sw);
		sw.setStore(store);
		stores.add(store);
		store.setStock(stocks);
		return store;
		
	}
	
	public void setStores(ArrayList<Store> stores) {
		this.stores=stores;
		ArrayList<String> ar = new ArrayList<>();
		for (Store store : stores) {
			if(store.getType().equals(StoreType.Physical))
				ar.add(store.getName());
		}
		cbStores.setItems(FXCollections.observableArrayList(ar));
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
		try {
			loadGUI("CartGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}
}