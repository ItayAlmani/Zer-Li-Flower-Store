package orderNproducts.gui.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import common.ClientConsole;
import common.Context;
import common.gui.controllers.ParentGUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

/**
 * The Customer will choose between Immediate delivery or Pre-order. If pre-order has been chosen, the GUI will reveal the datepickers
 */
public class OrderTimeGUIController implements Initializable {

	private @FXML JFXButton btnSend, btnBack;
	private @FXML JFXDatePicker dpDate;
	private @FXML VBox vboxTime, vboxDateTime;
	private @FXML JFXSlider sldMinutes, sldHours;
	private @FXML JFXToggleButton tglPreOrder;
	private LocalDateTime date = null;
	
	private final static String immStr = "You chose immediate order",
			preStr = "You chose pre order";
	
	private boolean now_plus3_is_in_working_hours;
	
	/**current time plus 5 minutes to give to the customer to finish the Order*/
	private LocalDateTime now_dt = LocalDateTime.now().plusMinutes(5)
			, nowDT_plus3 = now_dt.plusHours(3);

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		btnSend.setOnAction(confirmOrderTimeEventHandler);
		selectedHours();
		sldMinutes.valueProperty().addListener((obs,ov,nv)->{
			if(nv != null && date!=null)
				date=date.withMinute(nv.intValue());
		});
		sldMinutes.setValue(0);
		sldHours.setValue(7);
		
		Calendar c = Calendar.getInstance();
		c.setTime(Date.from(now_dt.atZone(ZoneId.systemDefault()).toInstant()));
		int unroundedMinutes = c.get(Calendar.MINUTE);
		int mod = unroundedMinutes % 15;
		c.add(Calendar.MINUTE, 15-mod);
		//next hour is the first option
		if(c.get(Calendar.HOUR_OF_DAY)!=now_dt.getHour())
			nowDT_plus3=nowDT_plus3.plusHours(1);
		
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
		
		if(tglPreOrder.isSelected()) {
			Context.order.getDelivery().setImmediate(false);
		}
		else {
			Context.order.getDelivery().setImmediate(true);
			//if 3 hours from now are before 22:00
			if(nowDT_plus3.isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 00))))
				date=nowDT_plus3;
			else {
				LocalDate new_date = LocalDate.now().plusDays(1);
				LocalTime new_time = LocalTime.of(7, 00);
				date = LocalDateTime.of(new_date, new_time);
			}
			Context.order.getDelivery().setImmediate(true);
		}
	}

	public void back() {
		Context.mainScene.loadDelivery();
	}
	
	public void selectedDate() {
		date=dpDate.getValue().atStartOfDay();
		LocalTime now_time = now_dt.toLocalTime();
		LocalDate now_date = now_dt.toLocalDate(),
				reqDate = dpDate.getValue();
		
		//The order is for today and 3 hours from now store still works
		if(now_date.equals(reqDate) && now_plus3_is_in_working_hours) {
			Calendar c = Calendar.getInstance();
			c.setTime(Date.from(now_dt.atZone(ZoneId.systemDefault()).toInstant()));
			
			int unroundedMinutes = c.get(Calendar.MINUTE);
			int mod = unroundedMinutes % 15;
			c.add(Calendar.MINUTE, mod < 8 ? -mod : (15-mod));
			
			sldHours.setMin((double)now_time.plusHours(3).getHour());
			sldMinutes.setMin((double)c.get(Calendar.MINUTE));
		}
		else {
			sldHours.setMin(7f);
			sldMinutes.setMin(0f);
		}
		sldHours.setValue(sldHours.getMin());
		sldMinutes.setValue(sldMinutes.getMin());
		date=date.withHour((int)sldHours.getValue());
		date=date.withMinute((int)sldMinutes.getValue());
		
		
		sldMinutes.setDisable(false);
		sldMinutes.setVisible(true);	
		sldHours.setDisable(false);
		vboxTime.setVisible(true);
		sldHours.setVisible(true);
	}

	public void selectedHours() {
		sldHours.valueProperty().addListener((obs,ov,nv)->{
			if(nv != null && date!=null) {
				date=date.withHour(nv.intValue());
				LocalTime now_time = now_dt.toLocalTime();
	
				//if now hours+3 chosen
				int val = nv.intValue();
				int hour = now_time.getHour()+3;
				
				if(val==hour) {
					Calendar c = Calendar.getInstance();
					c.setTime(Date.from(now_dt.atZone(ZoneId.systemDefault()).toInstant()));
					
					int unroundedMinutes = c.get(Calendar.MINUTE);
					int mod = unroundedMinutes % 15;
					c.add(Calendar.MINUTE, 15-mod);
					
					sldMinutes.setMin(c.get(Calendar.MINUTE));
					sldMinutes.setValue(c.get(Calendar.MINUTE));
				}
				else {
					sldMinutes.setMin(0);
					sldMinutes.setValue(0);
				}
				sldMinutes.setDisable(false);
				sldMinutes.setVisible(true);	
			}
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
		date=null;
		vboxDateTime.setVisible(tglPreOrder.isSelected());
	}
	
	/**
	 * {@link EventHandler} which pop an {@link Dialog} when {@link #stage} being asked
	 * to close. The {@link Dialog} will confirm that the user want to exit the app.<br>
	 * If confirmed, the app will disconnect the {@link ClientConsole} and call {@link #deleteAllImages()}.
	 */
	private final EventHandler<ActionEvent> confirmOrderTimeEventHandler = actEvent -> {
		addTime();
		if(date==null) {
			Alert confirmation = new Alert(Alert.AlertType.ERROR, "Must choose date");
			confirmation.setHeaderText("Confirm Order Delivery Time");
			confirmation.initModality(Modality.APPLICATION_MODAL);
			confirmation.initOwner(ParentGUIController.primaryStage);
	
			confirmation.setX(ParentGUIController.primaryStage.getX());
			confirmation.setY(ParentGUIController.primaryStage.getY());
			@SuppressWarnings("unused")
			Optional<ButtonType> response = confirmation.showAndWait();
		}
		else {
			Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "The delivery time you selected is: "+date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
			Button changeButton = (Button) confirmation.getDialogPane().lookupButton(ButtonType.OK);
			changeButton.setText("Change Delivery Time");
			confirmation.setHeaderText("Confirm Order Delivery Time");
			confirmation.initModality(Modality.APPLICATION_MODAL);
			confirmation.initOwner(ParentGUIController.primaryStage);
	
			confirmation.setX(ParentGUIController.primaryStage.getX());
			confirmation.setY(ParentGUIController.primaryStage.getY());
	
			Optional<ButtonType> response = confirmation.showAndWait();
			if (!ButtonType.OK.equals(response.get()))
				actEvent.consume();
			else {
				Context.order.getDelivery().setDate(date);
				Context.mainScene.loadPayment();
			}
		}
	};
}