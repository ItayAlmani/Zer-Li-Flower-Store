package lior.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import entities.HistogramOfCustomerComplaintsReport;
import entities.IncomesReport;
import entities.OrderReport;
import entities.Product.ProductType;
import entities.QuarterlyReport.ReportType;
import entities.SatisfactionReport;
import entities.Store;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	@FXML JFXComboBox<Integer> cbQuarter1,cbQuarter2,cbYear1,cbYear2;
	private @FXML JFXToggleButton ToggleReport2;
	private @FXML JFXButton btConfirm1;
	// Report1Pick variables
	private @FXML Label lblReport2;
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
	@FXML JFXSpinner Spinner1;
	@FXML JFXSpinner Spinner2;
	
	/**
	 * This function runs the process of displaying the report selector screen
	 * @param stage - the stage that represents this screen
	 * @throws IOException - loader.load() can throw an IOException
	 */
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/ReportMainGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Report Menu");
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * The screen initialization function before it goes up.
	 * Before the screen goes up, we update the Combo boxes, date menus, and the report types menu.
	 * In addition, we choose what will be displayed to the user and according to the user type.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		ArrayList<Integer> cbqurtertemp= new ArrayList<>();
		for(int i=1;i<=4;i++)
			cbqurtertemp.add(i);
		cbQuarter1.setItems(FXCollections.observableArrayList(cbqurtertemp));
		cbQuarter2.setItems(FXCollections.observableArrayList(cbqurtertemp));
		
		int start_year = LocalDate.now().getYear();
		
		//if no quarter finished this year
		if(getQuarterNumberByNow()==1)
			start_year--;//without current year
		
		cbqurtertemp.clear();
		for(int i = start_year; i>=LocalDate.now().minusYears(40).getYear(); i--)
			cbqurtertemp.add(i);			
		
		cbYear1.setItems(FXCollections.observableArrayList(cbqurtertemp));
		cbYear2.setItems(FXCollections.observableArrayList(cbqurtertemp));
		//Select the section segmentation
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
	/**
	 * This function is the heart of the report selection department.
	 * In this function we will perform the selection of the report that the user requested and we will
	 * send the request to the server and we will get the answer back, according to the user's selections
	 * from the existing combo boxes.
	 * Of course, there is a possibility that a chain store manager will be able to choose 2 reports at any time.
	 * @param event - Represntes the event when confirm button pressed in report selector screen
	 * @throws Exception - All calls to the server can create an exception
	 */
	public void askforreportHandler(ActionEvent event) throws Exception {
		Context.mainScene.setMessage("");
		resetall();
		err = 0;
		if (cbTypePick1.getValue() != null && cbStorePick1.getValue() != null && cbQuarter1.getValue() != null&&
				cbYear1.getValue() != null) {
			Spinner1.setVisible(true);
			LocalDate date = pickedDate(cbQuarter1.getValue(),cbYear1.getValue());
			if (date.isBefore(LocalDate.now()) && cbStorePick1.getValue()!=null) {
				n = cbStorePick1.getValue().getStoreID();
				if (this.cbTypePick1.getValue().equals(ReportType.Order))
					Context.fac.orderReport.initproduceOrderReport(date, n);
				else if (cbTypePick1.getValue().equals(ReportType.Incomes))
					Context.fac.incomesReport.initProduceIncomesReport(date, n);
				else if (cbTypePick1.getValue().equals(ReportType.CustomerComplaints))
				    Context.fac.histogramReport.initproduceHistogramOfCustomerComplaintsReport(date, cbStorePick1.getValue());
				else if (cbTypePick1.getValue().equals(ReportType.Satisfaction))
					Context.fac.satisfactionReport.initProduceSatisfactionReport(date, n);
			} else
				err = 2;
			//Here is the part that checks if this user have the permission of chain store manager
			//if there is it check if he choose another report to show.
			if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
				if (this.cbStorePick2.getValue() != null && this.cbTypePick2.getValue() != null
						&& cbQuarter2.getValue() != null&&cbYear2.getValue() != null) {
					Spinner2.setVisible(true);
					LocalDate date1 = pickedDate(cbQuarter2.getValue(), cbYear2.getValue());
					if (date1.isBefore(LocalDate.now()) && cbStorePick2.getValue()!=null) {
						n1 = cbStorePick2.getValue().getStoreID();
						if (this.cbTypePick2.getValue().equals(ReportType.Order)) {
							/*if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.orderReport.initproduceOrderReport(date1, n1);
								} else
									Context.fac.orderReport.initproduceOrderReport(date1, n1);
							}*/
							Context.fac.orderReport.initproduceOrderReport(date1, n1);
						}

						else if (cbTypePick2.getValue().equals(ReportType.Incomes)) {
							/*if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.incomesReport.initProduceIncomesReport(date1, n1);
								} else
									Context.fac.incomesReport.initProduceIncomesReport(date1, n1);
							}*/
							Context.fac.incomesReport.initProduceIncomesReport(date1, n1);
						} else if (cbTypePick2.getValue().equals(ReportType.CustomerComplaints)) {
							/*if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.histogramReport.initproduceHistogramOfCustomerComplaintsReport(date1, cbStorePick2.getValue());
								} else
									Context.fac.histogramReport.initproduceHistogramOfCustomerComplaintsReport(date1, cbStorePick2.getValue());
							}*/
							Context.fac.histogramReport.initproduceHistogramOfCustomerComplaintsReport(date1, cbStorePick2.getValue());
						}

						else if (cbTypePick2.getValue().equals(ReportType.Satisfaction)) {
							/*if (cbTypePick2.getValue().equals(cbTypePick1.getValue()) == false
									|| date.equals(date1) == false
									|| cbStorePick1.getValue().equals(cbStorePick2.getValue()) == false) {
								if (cbTypePick2.getValue().equals(cbTypePick1.getValue())) {
									Context.fac.satisfactionReport.initProduceSatisfactionReport(date1, n1);
								} else
									Context.fac.satisfactionReport.initProduceSatisfactionReport(date1, n1);
							}*/
							Context.fac.satisfactionReport.initProduceSatisfactionReport(date1, n1);
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
	/**
	 * In this function we initialize the store selection menu.
	 * A store manager will show only his store, while a chain manager will have all the stores.
	 * @param stores - in the initialization we ask for the store list from the DB
	 * its stored in stores arraylist and this is the input of this function
	 */
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
					stores.add(new Store(BigInteger.valueOf(-1), "All Stores"));
					cbStorePick1.setItems(FXCollections.observableArrayList(stores));
					cbStorePick2.setItems(FXCollections.observableArrayList(stores));
					cbStorePick2.setVisible(true);
				}
			});
		}
	}
	/**
	 * This function updates all the GUI components associated with the Orders report.
	 * If necessary, it updates to 2 reports, of course, according to the user's permissions.
	 * @param oReports - the answer that come back from the server,
	 *  an OrderReport arraylist that include all information in the selected date range and specific store.
	 */
	public void setOrderReports(ArrayList<OrderReport> oReports) {
		if (oReports == null)
			return;
		/*if(n.equals(-1))
			setAllOrderReports(oReports);
		else {*/
			OrderReport rep = oReports.get(0);
			LocalDate date = pickedDate(cbQuarter1.getValue(),cbYear1.getValue());
			if (rep.getStoreID().equals(n) && cbTypePick1.getValue().equals(ReportType.Order)
					&& date.equals(rep.getEndOfQuarterDate())) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblFlowerArrcnt1.setText(Integer.toString(rep.getCounterPerType().get(ProductType.FlowerArrangment)));
						lblFlowerPlacnt1.setText(Integer.toString(rep.getCounterPerType().get(ProductType.FloweringPlant)));
						lblBridalBoucnt1.setText(Integer.toString(rep.getCounterPerType().get(ProductType.BridalBouquet)));
						lblFlowerClucnt1.setText(rep.getCounterPerType().get(ProductType.FlowersCluster).toString());
						lblFlowerArrsum1.setText(Float.toString(rep.getSumPerType().get(ProductType.FlowerArrangment)));
						lblFlowerPlasum1.setText(Float.toString(rep.getSumPerType().get(ProductType.FloweringPlant)));
						lblBridalBousum1.setText(Float.toString(rep.getSumPerType().get(ProductType.BridalBouquet)));
						lblFlowerClusum1.setText(rep.getSumPerType().get(ProductType.FlowersCluster).toString());
					}
				});
				Spinner1.setVisible(false);
				paneOrderReport1.setVisible(true);
			} else {
				Platform.runLater(new Runnable() {
					public void run() {
						lblFlowerArrcnt2.setText(Integer.toString(rep.getCounterPerType().get(ProductType.FlowerArrangment)));
						lblFlowerPlacnt2.setText(Integer.toString(rep.getCounterPerType().get(ProductType.FloweringPlant)));
						lblBridalBoucnt2.setText(Integer.toString(rep.getCounterPerType().get(ProductType.BridalBouquet)));
						lblFlowerClucnt2.setText(rep.getCounterPerType().get(ProductType.FlowersCluster).toString());
						lblFlowerArrsum2.setText(Float.toString(rep.getSumPerType().get(ProductType.FlowerArrangment)));
						lblFlowerPlasum2.setText(Float.toString(rep.getSumPerType().get(ProductType.FloweringPlant)));
						lblBridalBousum2.setText(Float.toString(rep.getSumPerType().get(ProductType.BridalBouquet)));
						lblFlowerClusum2.setText(rep.getSumPerType().get(ProductType.FlowersCluster).toString());
					}
				});
				Spinner2.setVisible(false);
				paneOrderReport2.setVisible(true);
			}
		//}
	}
	/**
	 * This function updates all the GUI components associated with the Incomes report.
	 * If necessary, it updates to 2 reports, of course, according to the user's permissions.
	 * @param iReports - the answer that come back from the server,
	 *  an IncomesReport arraylist that include all information in the selected date range and specific store.
	 */
	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if (iReports == null)
			return;
		IncomesReport rep = iReports.get(0);
		LocalDate date = pickedDate(cbQuarter1.getValue(),cbYear1.getValue());
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
			Spinner1.setVisible(false);
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
			Spinner2.setVisible(false);
			paneIncomeReport2.setVisible(true);
		}

	}
	/**
	 * This function updates all the GUI components associated with the Satisfaction report.
	 * If necessary, it updates to 2 reports, of course, according to the user's permissions. 
	 * @param sReports - the answer that come back from the server,
	 *  an IncomesReport arraylist that include all information in the selected date range and specific store.
	 */
	public void setSatisfactionReports(ArrayList<SatisfactionReport> sReports) {
		if (sReports == null)
			return;
		SatisfactionReport rep = sReports.get(0);
		LocalDate date = pickedDate(cbQuarter1.getValue(),cbYear1.getValue());
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
			Spinner1.setVisible(false);
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
			Spinner2.setVisible(false);
			paneSatisfactionReport2.setVisible(true);
		}

	}
	/**
	 * This function updates all the GUI components associated with the Histograma report.
	 * Part of it its the updating of the data into a stacked bar chart. 
	 * If necessary, it updates to 2 reports, of course, according to the user's permissions.  
	 * @param ccReports - the answer that come back from the server,
	 *  an IncomesReport arraylist that include all information in the selected date range and specific store.
	 */
	public void setHistogramOfCustomerComplaintsReports(ArrayList<HistogramOfCustomerComplaintsReport> ccReports) {
		if (ccReports == null)
			return;
		HistogramOfCustomerComplaintsReport rep = ccReports.get(0);
		LocalDate date = pickedDate(cbQuarter1.getValue(),cbYear1.getValue());
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
			Spinner1.setVisible(false);
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
			Spinner2.setVisible(false);
			paneHistogram2.setVisible(true);
		}

	}
	/**
	 * 	This function controls the ToggleReport2 button which determines whether another report
	 *  will be displayed.
	 * This button will be displayed only to a chain store manager.
	 * @param event - event represents the behavior when you press the ToggleReport2 button
	 */
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

	/**
	 * This function clears the screen.
	 * In effect, a window will not be created on a window.
	 */
	public void resetall()
	{
		Spinner1.setVisible(false);
		Spinner2.setVisible(false);
		paneOrderReport1.setVisible(false);
		paneOrderReport2.setVisible(false);
		paneIncomeReport1.setVisible(false);
		paneIncomeReport2.setVisible(false);
		paneSatisfactionReport1.setVisible(false);
		paneSatisfactionReport2.setVisible(false);
		paneHistogram1.setVisible(false);
		paneHistogram2.setVisible(false);
	}
	
	
	LocalDate pickedDate(int pick,int year) {
		if (pick==1)
			return LocalDate.of(year, Month.MARCH, 31);
		else if (pick==2)
			return LocalDate.of(year, Month.JUNE, 31);
		else if (pick==3)
			return LocalDate.of(year, Month.SEPTEMBER, 31);
		else if (pick==4)
			return LocalDate.of(year, Month.DECEMBER, 31);
		return null;
	}
	
	/**
	 * The {@link EventHandler} of both the onAction of {@link #cbYear1} and {@link #cbYear2}
	 * @param event - the {@link ActionEvent} of the {@link EventHandler}
	 */
	public void yearSelected(ActionEvent event) {
		JFXComboBox<Integer> src = null, cbq = null;
		//which combo box invoked the handler
		if(event.getSource().equals(cbYear1)) {
			src = cbYear1;
			cbq=cbQuarter1;
		}
		else if(event.getSource().equals(cbYear2)) {
			src=cbYear2;
			cbq=cbQuarter2;
		}
		//if current year chosen
		if(src.getValue()!=null && src.getValue().equals(LocalDate.now().getYear())) {
			ArrayList<Integer> cbQurterTemp= new ArrayList<>();
			//getQuarterNumberByNow() won't be at this point 1 never
			//because we prevented it by removing current year if getQuarterNumberByNow()==1
			for(int i=getQuarterNumberByNow()+1;i<=4;i++)
				cbQurterTemp.add(i);
			cbq.setItems(FXCollections.observableArrayList(cbQurterTemp));
		}
	}

	/**
	 * checks what quarter are we at
	 * @return the quarter number (int between 1-4)
	 */
	private int getQuarterNumberByNow() {
		int year = LocalDate.now().getYear();
		LocalDate now = LocalDate.now(),
				fstQ = LocalDate.of(year, 1, 1),
				sndQ = LocalDate.of(year, 4, 1),
				trdQ = LocalDate.of(year, 7, 1),
				frtQ = LocalDate.of(year, 10, 1);
		if (now.isAfter(fstQ.minusDays(1)) && now.isBefore(sndQ))
			return 1;
		else if (now.isAfter(sndQ.minusDays(1)) && now.isBefore(trdQ))
			return 2;
		else if (now.isAfter(trdQ.minusDays(1)) && now.isBefore(frtQ))
			return 3;
		else
			return 4;
	}
}