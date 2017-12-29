package lior.gui.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import common.Context;
import entities.IncomesReport;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
		lblEndDate.setText("-1");
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.MONTH, -3);
		lblStartDate=new Label("-1"/*ReqDate.format(c.getTime())*/);
		//lblStartDate.setText();
		lblTotIncomeSum.setText(ReqDate.format(c.getTime()));
		lblTotIncome.setText("sadaad");
	}
	
	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if(iReports==null)return;
		IncomesReport rep = iReports.get(0);
		DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
			String s=String.valueOf(rep.getTotIncomes());
			lblEndDate.setText(ReqDate.format(rep.getEnddate()));
			lblStartDate.setText(ReqDate.format(rep.getStartdate()));
			lblTotIncomeSum.setText(s);
			}
		});
	}
	
	public void Backtomainmenuhandler (ActionEvent event) throws Exception
	{
		super.loadMainMenu();
	}

}
