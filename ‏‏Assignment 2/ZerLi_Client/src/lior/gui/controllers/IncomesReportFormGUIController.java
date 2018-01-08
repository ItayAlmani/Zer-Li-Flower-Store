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
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IncomesReportFormGUIController implements Initializable {

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
	@FXML Label lblDataerr;
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/IncomesReportFormGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Incomes Menu");
		stage.setScene(scene);
		stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		lblFrom.setVisible(false);
		lblTo.setVisible(false);
		lblStore.setVisible(false);
		lblTotIncome.setVisible(false);
		lblMsg.setVisible(false);
		lblStartDate.setVisible(false);
		lblEndDate.setVisible(false);
		lblStoreID.setVisible(false);
		lblTotIncomeSum.setVisible(false);
		Duration.seconds(5);
		lblDataerr.setVisible(true);
	}
	
	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if(iReports==null)return;
		IncomesReport rep = iReports.get(0);
		DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
		lblFrom.setVisible(true);
		lblTo.setVisible(true);
		lblStore.setVisible(true);
		lblTotIncome.setVisible(true);
		lblMsg.setVisible(true);
		lblStartDate.setVisible(true);
		lblEndDate.setVisible(true);
		lblStoreID.setVisible(true);
		lblTotIncomeSum.setVisible(true);
		lblDataerr.setVisible(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String s=String.valueOf(rep.getTotIncomes());
				lblStoreID.setText(rep.getStoreID().toString());
				lblEndDate.setText(ReqDate.format(rep.getEnddate()));
				lblStartDate.setText(ReqDate.format(rep.getStartdate()));
				lblTotIncomeSum.setText(s);
			}
		});
	}
	
	public void Backtomainmenuhandler (ActionEvent event) throws Exception
	{
		Context.mainScene.loadMainMenu();
	}

}
