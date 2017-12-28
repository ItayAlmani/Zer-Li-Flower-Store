package izhar.gui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DeliveryGUIController extends ParentGUIController {

	private @FXML Button btnSelect, btnSend;
	private @FXML TextField txtStreet, txtCity, txtPostCode, txtName, txtPhoneAreaCode, txtPhonePost;
	private @FXML Label lblMsg;
	private @FXML ComboBox<String> cbStores;
	private @FXML GridPane gdShipment, gpPickup;
	private @FXML ToggleGroup tGroup;
	private @FXML RadioButton rbShipment, rbPickup;
	private @FXML VBox vboxForm;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		tGroup= new ToggleGroup();
		rbPickup.setToggleGroup(tGroup);
		rbShipment.setToggleGroup(tGroup);
		
		//Context.fac.store.getAllStores();
		
		/*tGroup.selectedToggleProperty().addListener(
				(observable, oldVal, newVal) -> vboxForm.setVisible(true));*/
		tGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				vboxForm.setVisible(true);
				cbStores.setValue(1);
			}
		});
	}

	public void showPickup(ActionEvent event) {
		this.gdShipment.setVisible(false);
	}
	
	public void showShipment(ActionEvent event) {
		this.gdShipment.setVisible(true);
	}

	@FXML public void addDelivery(ActionEvent event) {
		
	}
}