package izhar.gui.controllers;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.behavior.JFXTimePickerBehavior;
import com.jfoenix.skins.JFXTimePickerContent;
import com.jfoenix.skins.JFXTimePickerSkin;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.DateCell;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

/**
 * The Customer will choose between Immediate delivery or Pre-order. If pre-order has been chosen, the GUI will reveal the datepickers
 */
public class OrderTimeGUIController implements Initializable {

	private @FXML JFXButton btnSend, btnBack;
	private @FXML JFXDatePicker dpDate;
	private @FXML JFXTimePicker tpTime;
	private @FXML VBox vboxTime, vboxDateTime;
	private @FXML JFXSlider sldMinutes, sldHours;
	private @FXML JFXToggleButton tglPreOrder;

	public void addTime() {
		LocalDateTime date = null;
		
		if(tglPreOrder.isSelected()) {
			date = dpDate.getValue().atStartOfDay();
			date=date.plusHours(((Double)sldHours.getValue()).longValue());
			date=date.plusMinutes(((Double)sldMinutes.getValue()).longValue());
			Context.order.getDelivery().setImmediate(false);
		}
		else {
			Context.order.getDelivery().setImmediate(true);
			date = LocalDateTime.now();
			//if 3 hours from now are before 22:00
			if(date.plusHours(3).minusMinutes(1).toLocalDate().isAfter(date.toLocalDate())==false
					&& date.toLocalTime().plusHours(3).plusMinutes(-1).isBefore(LocalTime.of(21, 59)))
				date=date.plusHours(3).plusMinutes(-1);
			else {
				LocalDate new_date = date.toLocalDate().plusDays(1);
				LocalTime new_time = LocalTime.of(7, 00);
				date = LocalDateTime.of(new_date, new_time);
			}
			Context.order.getDelivery().setImmediate(true);
		}
		Context.order.getDelivery().setDate(date);
		Context.mainScene.loadGUI("PaymentGUI", false);
	}

	public void back() {
		Context.mainScene.loadGUI("DeliveryGUI", false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		LocalTime now_time = LocalTime.now();
		if(now_time.plusHours(3).getHour()>22 || 
				LocalDateTime.now().plusHours(3).getDayOfMonth()>LocalDateTime.now().getDayOfMonth()) {
			dpDate.setDayCellFactory(picker -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate date, boolean empty) {
	                super.updateItem(date, empty);
	                setDisable(empty || date.isBefore(LocalDate.now()) || date.equals(LocalDate.now()));
	            }
	        });
		}
		else {
			dpDate.setDayCellFactory(picker -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate date, boolean empty) {
	                super.updateItem(date, empty);
	                setDisable(empty || date.isBefore(LocalDate.now()));
	            }
	        });
		}
	}
	
	public void selectedDate() {
		Context.order.getDelivery().setDate(dpDate.getValue().atStartOfDay());
		LocalTime now_time = LocalTime.now();
		LocalDate reqDate = dpDate.getValue(), now_date = LocalDate.now();
		
		ArrayList<LocalTime> al = new ArrayList<>();
		
		//The order is for today
		if(now_date.compareTo(reqDate)==0) {
			if(now_time.compareTo(LocalTime.of(22, 00))<=0) { //delivery until 22:00
				sldHours.setMin(now_time.plusHours(3).getHour());
				for (LocalTime i = LocalTime.of(now_time.plusHours(3).getHour(), now_time.truncatedTo(ChronoUnit.MINUTES).getMinute()); i.isBefore(LocalTime.of(21, 45)); i=i.plusMinutes(15))
					al.add(i);
			}
		}
		else {
			sldHours.setMin(7);
			for (LocalTime i = LocalTime.of(7, 00); i.isBefore(LocalTime.of(22, 00)); i=i.plusHours(1))
				al.add(i);
		}
		sldHours.setValue(sldHours.getMin());
		vboxTime.setVisible(true);
		sldHours.setVisible(true);
	}

	public void selectedHours() {
		LocalTime now_time = LocalTime.now();

		//if now hours+3 chosen
		Integer val = ((Double)sldHours.getValue()).intValue();
		Integer hour = now_time.getHour()+3;
		if(val.equals(hour))
			sldMinutes.setMin(now_time.getMinute()+1);
		else
			sldMinutes.setMin(0);
		sldMinutes.setValue(sldMinutes.getMin());
		sldMinutes.setVisible(true);
	}

	public void toggledPreOrder() {
		if(tglPreOrder.isSelected()) {
			tglPreOrder.setText("Switch for immediate order");
			tglPreOrder.setTextFill(Color.ORANGE);
		}
		else {
			tglPreOrder.setText("Switch for pre order");
			tglPreOrder.setTextFill(Color.RED);
		}
		vboxDateTime.setVisible(tglPreOrder.isSelected());
	}
}