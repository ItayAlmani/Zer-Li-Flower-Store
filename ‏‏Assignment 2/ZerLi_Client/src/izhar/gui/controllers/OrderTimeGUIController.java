package izhar.gui.controllers;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;

/**
 * The Customer will choose between Immediate delivery or Pre-order. If pre-order has been chosen, the GUI will reveal the datepickers
 */
public class OrderTimeGUIController extends ParentGUIController {

	private @FXML RadioButton rbImmediate, rbPreOrder;
	private @FXML Button btnSend, btnBack;
	private @FXML DatePicker dpDate;
	private @FXML GridPane gpDate;
	private @FXML ComboBox<Integer> cbHours, cbMinutes;
	private @FXML HBox hboxTime;
	private @FXML ToggleGroup tGroup;

	@FXML public void addTime() {
		LocalDateTime date = null;
		if(tGroup.getSelectedToggle().getUserData().equals("PreOrder")) {
			date = dpDate.getValue().atStartOfDay();
			date.plusHours(cbHours.getValue());
			date.plusMinutes(cbMinutes.getValue());
		}
		else {
			Context.order.getDelivery().setImmediate(true);
			date = LocalDateTime.now();
			date.plusHours(3);
			date.plusMinutes(-1);
		}
		Context.order.getDelivery().setDate(Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
		try {
			loadGUI("PaymentGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}

	public void back() {
		try {
			loadGUI("DeliveryGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}

	public void selectedImmediate() {
		gpDate.setVisible(false);
	}

	public void selectedPreOrder() {
		gpDate.setVisible(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		tGroup= new ToggleGroup();
		rbImmediate.setUserData("Immediate");
		rbImmediate.setToggleGroup(tGroup);
		rbPreOrder.setUserData("PreOrder");
		rbPreOrder.setToggleGroup(tGroup);
		
		dpDate.setEditable(false);
		
		LocalTime now_time = LocalTime.now();
		if(now_time.plusHours(3).getHour()>21 || 
				LocalDateTime.now().plusHours(3).getDayOfMonth()>LocalDateTime.now().getDayOfMonth()) {
			dpDate.setDayCellFactory(picker -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate date, boolean empty) {
	                super.updateItem(date, empty);
	                setDisable(empty || date.isBefore(LocalDate.now()) || date.equals(LocalDate.now()));
	            }
	        });
			//dpDate.setValue(LocalDate.now().plusDays(1));
		}
		else {
			dpDate.setDayCellFactory(picker -> new DateCell() {
	            @Override
	            public void updateItem(LocalDate date, boolean empty) {
	                super.updateItem(date, empty);
	                setDisable(empty || date.isBefore(LocalDate.now()));
	            }
	        });
			//dpDate.setValue(LocalDate.now());
		}
	}
	
	@FXML public void selectedDate() {
		Context.order.getDelivery().setDate(Date.from(dpDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		LocalTime now_time = LocalTime.now();
		LocalDate reqDate = dpDate.getValue(), now_date = LocalDate.now();
		
		ArrayList<Integer> al = new ArrayList<>();
		
		//The order is for today
		if(now_date.compareTo(reqDate)==0) {
			if(now_time.compareTo(LocalTime.of(21, 00))<=0) { //delivery until 22:00
				for (int i = now_time.getHour()+3+1; i <= 22; i++)
					al.add(i);
			}
		}
		else {
			for (int i = 7; i <= 22; i++)
				al.add(i);
		}
		cbHours.setItems(FXCollections.observableArrayList(al));
		hboxTime.setVisible(true);
		cbHours.setVisible(true);
	}

	@FXML public void selectedHours() {
		LocalTime now_time = LocalTime.now();
		
		ArrayList<Integer> al = new ArrayList<>();
		
		//if now hours+3 chosen
		if(now_time.getHour()+1+3==cbHours.getValue()) {
			for (int i = now_time.getMinute()+1; i <= 59; i++)
				al.add(i);
		}
		else {
			for (int i = 0; i <= 59; i++)
				al.add(i);
		}
		
		cbMinutes.setItems(FXCollections.observableArrayList(al));
		cbMinutes.setVisible(true);
	}

}