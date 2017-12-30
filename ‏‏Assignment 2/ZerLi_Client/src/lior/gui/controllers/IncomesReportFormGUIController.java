package lior.gui.controllers;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class IncomesReportFormGUIController extends ParentGUIController {

	@FXML Label lblFrom;
	@FXML Label lblTo;
	@FXML Label lblStore;
	@FXML Label lblTotIncome;
	@FXML Label lblMsg;
	@FXML Label lbltitle;
	@FXML Button btBackToMainMenu;
	@FXML Label lblStartDate;
	@FXML Label lblEndDate;
	@FXML Label lblStoreID;
	@FXML Label lblTotIncomeSum;
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/IncomesReportFormGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Incomes Menu");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
	}
	
	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if(iReports==null)return;
		IncomesReport rep = iReports.get(0);
		DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("Get To run inside setIncomeReports");
				String s=String.valueOf(rep.getTotIncomes());
				lblStoreID.setText("1");
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
