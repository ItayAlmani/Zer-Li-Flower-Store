package izhar.gui.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * The Customer will choose between Immediate delivery or Pre-order. If pre-order has been chosen, the GUI will reveal the datepickers
 */
public class OrderTimeGUIController implements Initializable {

	private @FXML JFXButton btnSend, btnBack;
	private @FXML JFXDatePicker dpDate;
	private @FXML VBox vboxTime, vboxDateTime;
	private @FXML JFXSlider sldMinutes, sldHours;
	private @FXML JFXToggleButton tglPreOrder;
	private LocalDateTime date;
	
	private final static String immStr = "You chose immediate order",
			preStr = "You chose pre order";
	
	private boolean now_plus3_is_in_working_hours;

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		LocalDateTime now_dt = LocalDateTime.now(), nowDT_plus3 = now_dt.plusHours(3);
		selectedHours();
		sldMinutes.valueProperty().addListener((obs,ov,nv)->{
			if(nv != null)
				date=date.withMinute(nv.intValue());
		});
		sldMinutes.setValue(0);
		sldHours.setValue(7);
		//if now is not working hours - between 22:00 to 07:00 day after that
		if(nowDT_plus3.isAfter(LocalDateTime.of(LocalDate.now(),LocalTime.of(22, 00))) ||
				nowDT_plus3.isBefore(LocalDateTime.of(LocalDate.now(),LocalTime.of(7, 00)))) {
			now_plus3_is_in_working_hours=false;
			dpDate.setDayCellFactory(picker -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate date, boolean empty) {
	                super.updateItem(date, empty);
	                //disable today and every date before
	                setDisable(empty || date.isBefore(LocalDate.now()) || date.equals(LocalDate.now()));
	            }
	        });
		}
		else {
			now_plus3_is_in_working_hours=true;
			dpDate.setDayCellFactory(picker -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate date, boolean empty) {
	                super.updateItem(date, empty);
	                //disable today
	                setDisable(empty || date.isBefore(LocalDate.now()));
	            }
	        });
		}
	}
	
	public void addTime() {
		LocalDateTime dt = null, 
				nowDT = LocalDateTime.now(), 
				nowDT_plus3 = nowDT.plusHours(3);
		
		if(tglPreOrder.isSelected()) {
			dt = date;
			Context.order.getDelivery().setImmediate(false);
		}
		else {
			Context.order.getDelivery().setImmediate(true);
			//if 3 hours from now are before 22:00
			if(nowDT_plus3.isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 00))))
				dt=nowDT_plus3;
			else {
				LocalDate new_date = LocalDate.now().plusDays(1);
				LocalTime new_time = LocalTime.of(7, 00);
				dt = LocalDateTime.of(new_date, new_time);
			}
			Context.order.getDelivery().setImmediate(true);
		}
		Context.order.getDelivery().setDate(dt);
		Context.mainScene.loadPayment();
	}

	public void back() {
		Context.mainScene.loadDelivery();
	}
	
	public void selectedDate() {
		date=dpDate.getValue().atStartOfDay();
		LocalTime now_time = LocalTime.now();
		LocalDate now_date = LocalDate.now(),
				reqDate = dpDate.getValue();
		
		//The order is for today and 3 hours from now store still works
		if(now_date.equals(reqDate) && now_plus3_is_in_working_hours)
			sldHours.setMin((double)now_time.plusHours(3).getHour());
		else
			sldHours.setMin(7f);
		sldHours.setValue(sldHours.getMin());
		vboxTime.setVisible(true);
		sldHours.setVisible(true);
	}

	public void selectedHours() {
		sldHours.valueProperty().addListener((obs,ov,nv)->{
			if(nv != null)
				date=date.withHour(nv.intValue());
			LocalTime now_time = LocalTime.now();

			//if now hours+3 chosen
			int val = nv.intValue();
			int hour = now_time.getHour()+3;
			if(val==hour) {
				sldMinutes.setMin(now_time.getMinute());
				sldMinutes.setValue(now_time.getMinute());
			}
			else
				sldMinutes.setMin(0);
			sldMinutes.setVisible(true);	
		});
	}

	public void toggledPreOrder() {
		if(tglPreOrder.isSelected()) {
			tglPreOrder.setText(preStr);
			tglPreOrder.setTextFill(Color.ORANGE);
		}
		else {
			tglPreOrder.setText(immStr);
			tglPreOrder.setTextFill(Color.RED);
		}
		vboxDateTime.setVisible(tglPreOrder.isSelected());
	}
}