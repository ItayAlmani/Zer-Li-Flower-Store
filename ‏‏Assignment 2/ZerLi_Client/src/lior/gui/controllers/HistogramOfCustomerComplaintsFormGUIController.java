package lior.gui.controllers;
import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;

public class HistogramOfCustomerComplaintsFormGUIController implements Initializable {
	
	private @FXML BarChart bar;
	@FXML BarChart HistogramChart;
	@FXML NumberAxis yax;
	@FXML CategoryAxis xax;
	@FXML Label lblFrom;
	@FXML Label lblStartDate;
	@FXML Label lblTo;
	@FXML Label lblEndDate;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		xax=new CategoryAxis();
		yax=new NumberAxis();
		this.bar=new BarChart<>(xax, yax);
		ObservableList<XYChart.Series<String,Integer>> data=FXCollections.observableArrayList();
		Series<String,Integer> data1=new Series<String, Integer>();
		Series<String,Integer> data2=new Series<String, Integer>();
		data1.setName("Open complaints");
		data2.setName("Complaints treated");
		data1.getData().add(new XYChart.Data("Open complaints",15));
		data2.getData().add(new XYChart.Data("Complaints treated",20));
		this.lblStartDate.setText("1");
		this.lblEndDate.setText("2");
		data.addAll(data1,data2);
		this.HistogramChart.getData().addAll(data);
			
	}
}

