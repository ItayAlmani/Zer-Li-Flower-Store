package lior.gui.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.OrderReport;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class OrderReportFormGUIController extends ParentGUIController {

	@FXML Label lblfArrncnt;
	@FXML Label lblfPlntcnt;
	@FXML Label lblfArrnsum;
	@FXML Label lblBBoucnt;
	@FXML Label lblfPlntsum;
	@FXML Label lblBBousum;
	@FXML Label lblFClucnt;
	@FXML Label lblFClusum;
	@FXML Button OutOrderReport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
	}
	
	public void setOrderReports(ArrayList<OrderReport> oReports) {
		if(oReports==null)return;
		OrderReport rep = oReports.get(0);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				lblfArrncnt.setText(rep.getCounterPerType().get(0).toString());
				lblfPlntcnt.setText(rep.getCounterPerType().get(1).toString());
				lblBBoucnt.setText(rep.getCounterPerType().get(2).toString());
				lblFClucnt.setText(rep.getCounterPerType().get(3).toString());
				
				lblfArrnsum.setText(rep.getSumPerType().get(0).toString());
				lblfPlntsum.setText(rep.getSumPerType().get(1).toString());
				lblBBousum.setText(rep.getSumPerType().get(2).toString());
				lblFClusum.setText(rep.getSumPerType().get(3).toString());
			}
		});
	}
}