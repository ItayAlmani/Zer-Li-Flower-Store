package lior.gui.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class IncomesReportFormGUIController extends ParentGUIController {

	@FXML Label lblFrom;
	@FXML Label lblStartDate;
	@FXML Label lblTo;
	@FXML Label lblEndDate;
	@FXML Label lblStore;
	@FXML Label lblStoreName;
	@FXML Label lblTotIncome;
	@FXML Label lblTotIncomeSum;
	@FXML Label lblMsg;
	@FXML Label lbltitle;
	@FXML Button btBackToMainMenu;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		Date date=new Date();
		DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
		this.lblEndDate.setText(ReqDate.format(date));
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.MONTH, -3);
		lblStartDate.setText(ReqDate.format(c.getTime()));
	}
	
	public void Backtomainmenuhandler (ActionEvent event) throws Exception
	{
		super.loadMainMenu();
	}

}
