package lior.gui.controllers;
import java.net.URL;
import java.util.ResourceBundle;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class HistogramOfCustomerComplaintsFormGUIController extends ParentGUIController {
	
	private @FXML BarChart bar;
	@FXML BarChart HistogramChart;
	@FXML NumberAxis yax;
	@FXML CategoryAxis xax;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
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
		
		data.addAll(data1,data2);
		this.HistogramChart.getData().addAll(data);
			
	}
}

