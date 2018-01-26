package izhar.gui.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import entities.DeliveryDetails;
import entities.Order.DeliveryType;
import entities.ShipmentDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DeliveryGUIController implements Initializable {

	private @FXML JFXButton btnSend;
	private @FXML JFXTextField txtStreet, txtCity, txtPostCode, txtName, txtPhoneAreaCode, txtPhonePost;
	private @FXML ToggleGroup tGroup;
	private @FXML JFXRadioButton rbShipment, rbPickup;
	private @FXML VBox vboxForm, paneShipment;
	private @FXML MaterialDesignIconView icnPickup, icnShipment;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		addTextLimiter(txtPhoneAreaCode, 3);
		addTextLimiter(txtPhonePost, 7);
		addTextLimiter(txtPostCode, 7);

		tGroup = new ToggleGroup();
		rbPickup.setUserData("Pickup");
		rbPickup.setToggleGroup(tGroup);
		rbShipment.setUserData("Shipment");
		rbShipment.setToggleGroup(tGroup);

		tGroup.selectedToggleProperty().addListener(e -> vboxForm.setVisible(true));
	}

	/** {@link EventHandler} of {@link #rbPickup} */
	public void showPickup() {
		Context.mainScene.setMessage("");
		icnPickup.setFill(Color.ORANGE);
		icnShipment.setFill(Color.RED);
		btnSend.setDisable(false);
		this.paneShipment.setVisible(false);
	}

	/** {@link EventHandler} of {@link #rbShipment} */
	public void showShipment() {
		Context.mainScene.setMessage("");
		icnShipment.setFill(Color.ORANGE);
		icnPickup.setFill(Color.RED);
		btnSend.setDisable(false);
		this.paneShipment.setVisible(true);
	}

	/** {@link EventHandler} of {@link #rbShipment} */
	public void addDelivery() {
		Object userData = tGroup.getSelectedToggle().getUserData();
		if (userData.equals("Pickup") == false && userData.equals("Shipment") == false) {
			Context.mainScene.setMessage("Must choose at least one option");
			return;
		}

		DeliveryDetails del = Context.order.getDelivery();
		if (userData.equals("Pickup")) {
			if (Context.order.getDeliveryType() != null
					&& Context.order.getDeliveryType().equals(DeliveryType.Shipment))
				Context.order.addToFinalPrice(-1 * ShipmentDetails.shipmentPrice);
			Context.order.setDeliveryType(DeliveryType.Pickup);
		} else { // Shipment
			//Check if all correct
			ShipmentDetails ship = new ShipmentDetails(del, txtStreet.getText(), txtCity.getText(),
					txtPostCode.getText(), txtName.getText(),
					txtPhoneAreaCode.getText() + "-" + txtPhonePost.getText());
			Context.order.setDelivery(ship);
			Context.order.setDeliveryType(DeliveryType.Shipment);
		}
		Context.mainScene.loadOrderTime();
	}

	private void addTextLimiter(JFXTextField tf, final int maxLength) {
		tf.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					char ch = tf.getText().charAt(oldValue.intValue());
					// Check if the new character is the number or other's
					if (!(ch >= '0' && ch <= '9')) {
						// if it's not number then just setText to previous one
						tf.setText(tf.getText().substring(0, tf.getText().length() - 1));
					}
				}
			}
		});
		if (maxLength != -1) {
			tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue,
						final String newValue) {
					if (tf.getText().length() > maxLength) {
						String s = tf.getText().substring(0, maxLength);
						tf.setText(s);
					}
				}
			});
		}
	}

	public void back() {
		Context.mainScene.loadCart();
	}
}