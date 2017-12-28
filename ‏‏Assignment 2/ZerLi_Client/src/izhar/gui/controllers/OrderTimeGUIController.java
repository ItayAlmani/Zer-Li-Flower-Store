package izhar.gui.controllers;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import jfxtras.scene.control.CalendarTimeTextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

/**
 * The Customer will choose between Immediate delivery or Pre-order. If pre-order has been chosen, the GUI will reveal the datepickers
 */
public class OrderTimeGUIController extends ParentGUIController {

	private @FXML RadioButton rbImmediate, rbPreOrder;
	private @FXML Button btnSend;
	private @FXML JFXDatePicker dpDate, dpTime;
	@FXML VBox vboxForm;
	@FXML GridPane gpDate;
	private @FXML CalendarTimeTextField timer = new CalendarTimeTextField();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		hbox.getChildren().add(timer);
	}

	/**
	 * Will ask OrderController to load the correct GUI by selected option
	 * @param rbOption
	 */
	public void selectDeliveryType(RadioButton rbOption) {
		// TODO - implement SelectDeliveryTimeGUI.selectDeliveryType
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param dpDate
	 * @param dpTime
	 */
	public void selectDeliveryDateTime(DatePicker dpDate, DatePicker dpTime) {
		// TODO - implement SelectDeliveryTimeGUI.selectDeliveryDateTime
		throw new UnsupportedOperationException();
	}

	public void loadTimePickers() {
		// TODO - implement SelectDeliveryTimeGUI.loadTimePickers
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param time
	 */
	public boolean checkTimeUnder3Hours(Date time) {
		// TODO - implement SelectDeliveryTimeGUI.checkTimeUnder3Hours
		throw new UnsupportedOperationException();
	}

	public void showUnder3HoursErrMsg() {
		// TODO - implement SelectDeliveryTimeGUI.showUnder3HoursErrMsg
		throw new UnsupportedOperationException();
	}

	@FXML public void addTime() {}

	@FXML public void back() {}

	@FXML public void selectedImmediate() {}

	@FXML public void selectedPreOrder() {}

}