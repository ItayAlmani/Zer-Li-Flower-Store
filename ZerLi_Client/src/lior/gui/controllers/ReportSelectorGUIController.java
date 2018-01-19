package lior.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import entities.HistogramOfCustomerComplaintsReport;
import entities.IncomesReport;
import entities.OrderReport;
import entities.QuarterlyReport.ReportType;
import entities.SatisfactionReport;
import entities.Store;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReportSelectorGUIController implements Initializable {

	private ArrayList<Store> stores;
	private BigInteger n, n1;
	int err;
	final static String isTreated = "is Treated";
    final static String isNotTreated = "is Not Treated";
    final static String isRefunded = "is Refunded";
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    

	// Report1Pick variables
	private @FXML JFXComboBox<ReportType> cbTypePick1, cbTypePick2;
	private @FXML JFXComboBox<Store> cbStorePick1, cbStorePick2;
	private @FXML JFXDatePicker DatePicker1;
	private @FXML JFXToggleButton ToggleReport2;
	private @FXML JFXButton btConfirm1;
	// Report1Pick variables
	private @FXML Label lblReport2;
	private @FXML JFXDatePicker DatePicker2;
	private @FXML AnchorPane paneReport2;
	// Order Report General Variables
	private @FXML AnchorPane paneOrderReport1, paneOrderReport2;
	// Order Report 1 Variables
	private @FXML Label lblFlowerArrcnt1, lblFlowerClusum1, lblBridalBousum1, lblFlowerPlasum1, lblFlowerArrsum1,
			lblFlowerPlacnt1, lblBridalBoucnt1, lblFlowerClucnt1;
	// Order Report 2 Variables
	private @FXML Label lblFlowerArrcnt2, lblFlowerClusum2, lblBridalBousum2, lblFlowerPlasum2, lblFlowerArrsum2,
			lblFlowerPlacnt2, lblBridalBoucnt2, lblFlowerClucnt2;
	// Income Report General Variables
	private @FXML AnchorPane paneIncomeReport2, paneIncomeReport1;
	// Income Report 1 Variables
	private @FXML Label lblStartDate1, lblEndDate1, lblStoreID1, lblTotIncome1;
	// Income Report 2 Variables
	private @FXML Label lblStartDate2, lblEndDate2, lblStoreID2, lblTotIncome2;
	// Histogram Report General Variables
	private @FXML AnchorPane paneHistogram1, paneHistogram2;
	@SuppressWarnings("rawtypes")
	private @FXML StackedBarChart Barchart1,Barchart2;
	// Satisfaction Report General Variables
	private @FXML AnchorPane paneSatisfactionReport1, paneSatisfactionReport2;
	// Satisfaction Report 1 Variables
	private @FXML Label lblQ1Ans1, lblQ3Ans1, lblQ4Ans1, lblQ5Ans1, lblQ6Ans1, lblQ7Ans1, lblQ8Ans1, lblQ2Ans1,
			lblQ10Ans1, lblQ9Ans1, lblTotans1, lblSatisfactionStartdate1, lblSatisfactionEnddate1;
	// Satisfaction Report 2 Variables
	private @FXML Label lblQ1Ans2, lblQ3Ans2, lblQ4Ans2, lblQ5Ans2, lblQ6Ans2, lblQ7Ans2, lblQ8Ans2, lblQ2Ans2,
			lblQ10Ans2, lblQ9Ans2, lblTotans2, lblSatisfactionStartdate2, lblSatisfactionEnddate2;

	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/ReportMainGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Report Menu");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		DatePicker1.setValue(LocalDate.now().minusDays(1));
		DatePicker2.setValue(LocalDate.now().minusDays(1));

		paneOrderReport1.setBorder(new Border(
				new BorderStroke(Color.FORESTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneOrderReport2.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneIncomeReport1.setBorder(new Border(
				new BorderStroke(Color.FORESTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneIncomeReport2.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneSatisfactionReport1.setBorder(new Border(
				new BorderStroke(Color.FORESTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneSatisfactionReport2.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneHistogram1.setBorder(new Border(
				new BorderStroke(Color.FORESTGREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		paneHistogram2.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		cbTypePick1.setItems(FXCollections.observableArrayList(ReportType.values()));
		try {
			Context.fac.store.getAllStores();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			cbTypePick2.setItems(FXCollections.observableArrayList(ReportType.values()));
			cbTypePick2.setVisible(true);
			ToggleReport2.setVisible(true);
		}

	}

	public void askforreportHandler(ActionEvent event) throws Exception {
		Context.mainScene.setMessage("");
		resetall();
		err = 0;
		if (cbTypePick1.getValue() != null && cbStorePick1.getValue() != null && DatePicker1.getValue() != null) {
			LocalDate date = DatePicker1.getValue();
			if (date.isBefore(LocalDate.now()) && cbStorePick1.getValue()!=null) {
				n = cbStorePick1.getValue().getStoreID();
				if (this.cbTypePick1.getValue().equals(ReportType.Order))
					Context.fac.orderReport.initproduceOrderReport(date, n);

				else if (cbTypePick1.getValue().equals(ReportType.Incomes))
					Context.fac.incomesReport.initProduceIncomesReport(date, n);

				else if (cbTypePick1.getValue().equals(ReportType.CustomerComplaints)) {
				    Context.fac.histogramReport.initproduceHistogramOfCustomerComplaintsReport(date, cbStorePick1.getValue());
				} else if (cbTypePick1.getValue().equals(ReportType.Satisfaction)) {
					Context.fac.satisfactionReport.initProduceSatisfactionReport(date, n);
				}
			} else
				err = 2;
			if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
				if (this.cbStorePick2.getValue() != null && this.cbTypePick2.getValue() != null
						&& DatePicker2 != null) {
					LocalDate date1 = DatePicker2.getValue();
					if (date1.isBefore(LocalDate.now()) && cbStorePick2.getValue()!=null) {
						n1 = cbStorePick2.getValue().getStoreID();
						if (this.cbTypePick2.getValue().equals(ReportType.Order)) {
							if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.orderReport.produceOrderReport(date1, n1);
								} else
									Context.fac.orderReport.initproduceOrderReport(date1, n1);
							}

						}

						else if (cbTypePick2.getValue().equals(ReportType.Incomes)) {
							if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.incomesReport.ProduceIncomesReport(date1, n1);
								} else
									Context.fac.incomesReport.initProduceIncomesReport(date1, n1);
							}
						} else if (cbTypePick2.getValue().equals(ReportType.CustomerComplaints)) {
							if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.histogramReport.produceHistogramOfCustomerComplaintsReport(date1, cbStorePick2.getValue());
								} else
									Context.fac.histogramReport.initproduceHistogramOfCustomerComplaintsReport(date1, cbStorePick2.getValue());
							}
						}

						else if (cbTypePick2.getValue().equals(ReportType.Satisfaction)) {
							if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.satisfactionReport.ProduceSatisfactionReport(date1, n1);
								} else
									Context.fac.satisfactionReport.initProduceSatisfactionReport(date1, n1);
							}
						}
					}
					else
					{
							err++;
					}	
				} 
				else
				{
					if(this.ToggleReport2.isSelected()==true)
						err++;
				}	
			}
			if (err == 1)
				Context.mainScene.setMessage("Report 2 data incorrect");
			else if (err == 2)
				Context.mainScene.setMessage("Report 1 data incorrect");
			else if (err == 3)
				Context.mainScene.setMessage("Report 1 and 2 data incorrect");
		}
	}

	public void setStores(ArrayList<Store> stores) {
		if (Context.getUser().getPermissions().equals(User.UserType.StoreManager)) {
			stores.clear();
			try {
				stores.add(Context.getUserAsStoreWorker().getStore());
			} catch (Exception e) {
				Context.mainScene.setMessage(e.getMessage());
				return;
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cbStorePick1.setItems(FXCollections.observableArrayList(stores));
				}
			});
		}
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					cbStorePick1.setItems(FXCollections.observableArrayList(stores));
					cbStorePick2.setItems(FXCollections.observableArrayList(stores));
					cbStorePick2.setVisible(true);
				}
			});
		}
		this.stores = stores;
	}

	public void setOrderReports(ArrayList<OrderReport> oReports) {
		if (oReports == null)
			return;
		OrderReport rep = oReports.get(0);
		LocalDate date = DatePicker1.getValue();
		if (rep.getStoreID().equals(n) && cbTypePick1.getValue().equals(ReportType.Order)
				&& date.equals(rep.getEnddate())) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblFlowerArrcnt1.setText(Integer.toString(rep.getCounterPerType().get(0)));
					lblFlowerPlacnt1.setText(Integer.toString(rep.getCounterPerType().get(1)));
					lblBridalBoucnt1.setText(Integer.toString(rep.getCounterPerType().get(2)));
					lblFlowerClucnt1.setText(rep.getCounterPerType().get(3).toString());
					lblFlowerArrsum1.setText(Float.toString(rep.getSumPerType().get(0)));
					lblFlowerPlasum1.setText(Float.toString(rep.getSumPerType().get(1)));
					lblBridalBousum1.setText(Float.toString(rep.getSumPerType().get(2)));
					lblFlowerClusum1.setText(rep.getSumPerType().get(3).toString());
				}
			});
			paneOrderReport1.setVisible(true);
		} else {
			Platform.runLater(new Runnable() {
				public void run() {
					lblFlowerArrcnt2.setText(Integer.toString(rep.getCounterPerType().get(0)));
					lblFlowerPlacnt2.setText(Integer.toString(rep.getCounterPerType().get(1)));
					lblBridalBoucnt2.setText(Integer.toString(rep.getCounterPerType().get(2)));
					lblFlowerClucnt2.setText(rep.getCounterPerType().get(3).toString());

					lblFlowerArrsum2.setText(Float.toString(rep.getSumPerType().get(0)));
					lblFlowerPlasum2.setText(Float.toString(rep.getSumPerType().get(1)));
					lblBridalBousum2.setText(Float.toString(rep.getSumPerType().get(2)));
					lblFlowerClusum2.setText(rep.getSumPerType().get(3).toString());
				}
			});
			paneOrderReport2.setVisible(true);
		}
	}

	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if (iReports == null)
			return;
		IncomesReport rep = iReports.get(0);
		LocalDate date = DatePicker1.getValue();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		if (rep.getStoreID().equals(n) && cbTypePick1.getValue().equals(ReportType.Incomes)
				&& date.equals(rep.getEnddate())) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					String s = String.valueOf(rep.getTotIncomes());
					lblStoreID1.setText(rep.getStoreID().toString());
					lblEndDate1.setText(rep.getEnddate().format(formatters));
					lblStartDate1.setText(rep.getStartdate().format(formatters));
					lblTotIncome1.setText(s);
				}
			});

			paneIncomeReport1.setVisible(true);
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					String s = String.valueOf(rep.getTotIncomes());
					lblStoreID2.setText(rep.getStoreID().toString());
					lblEndDate2.setText(rep.getEnddate().format(formatters));
					lblStartDate2.setText(rep.getStartdate().format(formatters));
					lblTotIncome2.setText(s);
				}
			});
			paneIncomeReport2.setVisible(true);
		}

	}

	public void setSatisfactionReports(ArrayList<SatisfactionReport> sReports) {
		if (sReports == null)
			return;
		SatisfactionReport rep = sReports.get(0);

		LocalDate date = DatePicker1.getValue();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		if (rep.getStoreID().equals(n) && cbTypePick1.getValue().equals(ReportType.Satisfaction)
				&& date.equals(rep.getEnddate())) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblQ1Ans1.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[0]));
					lblQ2Ans1.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[1]));
					lblQ3Ans1.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[2]));
					lblQ4Ans1.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[3]));
					lblQ5Ans1.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[4]));
					lblQ6Ans1.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[5]));
					lblTotans1.setText(new DecimalFormat("##.###").format(rep.getAverageTotanswer()));
					lblSatisfactionStartdate1.setText(rep.getStartdate().format(formatters));
					lblSatisfactionEnddate1.setText(rep.getEnddate().format(formatters));
				}
			});
			paneSatisfactionReport1.setVisible(true);
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblQ1Ans2.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[0]));
					lblQ2Ans2.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[1]));
					lblQ3Ans2.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[2]));
					lblQ4Ans2.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[3]));
					lblQ5Ans2.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[4]));
					lblQ6Ans2.setText(new DecimalFormat("##.##").format(rep.getFinalanswers()[5]));
					lblTotans2.setText(new DecimalFormat("##.###").format(rep.getAverageTotanswer()));
					lblSatisfactionStartdate2.setText(rep.getStartdate().format(formatters));
					lblSatisfactionEnddate2.setText(rep.getEnddate().format(formatters));
				}
			});
			paneSatisfactionReport2.setVisible(true);
		}

	}

	
	public void setHistogramOfCustomerComplaintsReports(ArrayList<HistogramOfCustomerComplaintsReport> ccReports) {
		if (ccReports == null)
			return;
		HistogramOfCustomerComplaintsReport rep = ccReports.get(0);
		LocalDate date = DatePicker1.getValue();
		if (rep.getStoreID().equals(n) && cbTypePick1.getValue().equals(ReportType.CustomerComplaints)
				&& date.equals(rep.getEnddate())) {
			Platform.runLater(new Runnable() {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				public void run() {
					Barchart1.getXAxis().setLabel("Status");       
					Barchart1.getYAxis().setLabel("Count");
				    XYChart.Series series1 = new XYChart.Series();
				    XYChart.Series series2 = new XYChart.Series();
				    XYChart.Series series3 = new XYChart.Series();
				    series1.setName("Is Treated");
				    series1.getData().add(new XYChart.Data(isTreated, rep.getTreatedCnt()));
				    series2.setName("Is Not Treated");
				    series2.getData().add(new XYChart.Data(isNotTreated, rep.getNotTreatedCnt()));
				    series3.setName("Is Refunded");  
				    series3.getData().add(new XYChart.Data(isRefunded, rep.getRefundedCnt()));
				    Barchart1.getData().clear();
				    Barchart1.getData().addAll(series1, series2, series3);
				}
			});
			paneHistogram1.setVisible(true);
		} else {
			Platform.runLater(new Runnable() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				@Override
				public void run() {
					Barchart2.getXAxis().setLabel("Status");       
					Barchart2.getYAxis().setLabel("Count");
				    XYChart.Series series1 = new XYChart.Series();
				    XYChart.Series series2 = new XYChart.Series();
				    XYChart.Series series3 = new XYChart.Series();
				    series1.setName("Is Treated");
				    series1.getData().add(new XYChart.Data(isTreated, rep.getTreatedCnt()));
				    series2.setName("Is Not Treated");
				    series2.getData().add(new XYChart.Data(isNotTreated, rep.getNotTreatedCnt()));
				    series3.setName("Is Refunded");  
				    series3.getData().add(new XYChart.Data(isRefunded, rep.getRefundedCnt()));
				    Barchart2.getData().clear();
				    Barchart2.getData().addAll(series1, series2, series3);
				}
			});
			paneHistogram2.setVisible(true);
		}

	}
	
	public void hideReport2(ActionEvent event) {
		if (!this.ToggleReport2.isSelected())
		{
			paneReport2.setVisible(false);
			cbTypePick2.setValue(null);
			cbStorePick2.setValue(null);
			err = 0;
		}
		else
			paneReport2.setVisible(true);

	}
	
	public void Backtomainmenuhandler(ActionEvent event) throws Exception {
		Context.mainScene.loadMainMenu();
	}
	
	public void resetall()
	{
		paneOrderReport1.setVisible(false);
		paneOrderReport2.setVisible(false);
		paneIncomeReport1.setVisible(false);
		paneIncomeReport2.setVisible(false);
		paneSatisfactionReport1.setVisible(false);
		paneSatisfactionReport2.setVisible(false);
		paneHistogram1.setVisible(false);
		paneHistogram2.setVisible(false);
	}
}