package lior.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import common.Context;
import entities.IncomesReport;
import entities.OrderReport;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.chart.BarChart;

public class ReportSelectorGUIController implements Initializable {

	private ArrayList<Store> stores;
	private BigInteger n, n1;
	
	//Report1Pick variables
	private @FXML JFXComboBox cbStorePick1,cbTypePick1;
	private @FXML JFXDatePicker DatePicker1;
	private @FXML JFXToggleButton ToggleReport2;
	private @FXML JFXButton btConfirm1,btBack;
	//Report1Pick variables
	private @FXML Label lblReport2;
	private @FXML JFXComboBox cbStorePick2,cbTypePick2;
	private @FXML JFXDatePicker DatePicker2;
	private @FXML AnchorPane paneReport2;
	//Order Report General Variables
	private @FXML AnchorPane paneOrderReport1,paneOrderReport2;
	//Order Report 1 Variables
	private @FXML Label lblFlowerArrcnt1,lblFlowerClusum1,lblBridalBousum1,lblFlowerPlasum1
	,lblFlowerArrsum1,lblFlowerPlacnt1,lblBridalBoucnt1,lblFlowerClucnt1;
	//Order Report 2 Variables
	private @FXML Label lblFlowerArrcnt2,lblFlowerClusum2,lblBridalBousum2,lblFlowerPlasum2
		,lblFlowerArrsum2,lblFlowerPlacnt2,lblBridalBoucnt2,lblFlowerClucnt2;
	//Income Report General Variables
	private @FXML AnchorPane paneIncomeReport2,paneIncomeReport1;
	//Income Report 1 Variables
	private @FXML Label lblStartDate1,lblEndDate1,lblStoreID1,lblTotIncome1;
	//Income Report 2 Variables
	private @FXML Label lblStartDate2,lblEndDate2,lblStoreID2,lblTotIncome2;
	//Histogram Report General Variables
	private @FXML AnchorPane paneHistogram1,paneHistogram2;
	@SuppressWarnings("rawtypes")
	private @FXML BarChart Barchart1,Barchart2;
	

	
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/ReportMainGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Report Menu");
		stage.setScene(scene);
		stage.show();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
		//paneAll.getChildren().removeAll(paneReport2,paneorder11,paneTotincome1);
		
		//btn.setRipplerFill(Color.ALICEBLUE);
		paneOrderReport1.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		paneOrderReport2.setBorder(new Border(new BorderStroke(Color.BLUE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		paneIncomeReport1.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		paneIncomeReport2.setBorder(new Border(new BorderStroke(Color.BLUE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		cbTypePick1.getItems().add("Incomes Report");
		cbTypePick1.getItems().add("Orders Report");
		cbTypePick1.getItems().add("Client complaimnts histogram");
		cbTypePick1.getItems().add("Satisfaction Report");
		try {
			Context.fac.store.getAllStores();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			cbTypePick2.getItems().add("Incomes Report");
			cbTypePick2.getItems().add("Orders Report");
			cbTypePick2.getItems().add("Client complaimnts histogram");
			cbTypePick2.getItems().add("Satisfaction Report");
			cbTypePick2.setVisible(true);
			cbTypePick2.setVisible(true);
			cbTypePick2.setVisible(true);
			cbTypePick2.setVisible(true);
			ToggleReport2.setVisible(true);
			//ToggleReport2.set;
		}

	}

	public void askforreportHandler(ActionEvent event) throws Exception {
		//paneAll.getChildren().add(paneReport2);
		if(cbTypePick1.getValue()!=null&&cbStorePick1.getValue()!=null) {
			@SuppressWarnings("deprecation")
			LocalDate date = DatePicker1.getValue();
			for (int i = 0; i < this.stores.size(); i++) {
				if (this.stores.get(i).getName().equals(this.cbStorePick1.getValue()))
					n = this.stores.get(i).getStoreID();
			}
			if (this.cbTypePick1.getValue().equals("Orders Report")) {
				try {
				} catch (Exception e) {
					// lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
				Context.fac.orderReport.initproduceOrderReport(date, n);
			}
	
			else if (cbTypePick1.getValue().equals("Incomes Report")) {
				try {
					//loadGUI("IncomesReportFormGUI", false);
				} catch (Exception e) {
					// lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
				Context.fac.incomesReport.initProduceIncomesReport(date, n);
			}
	
			else if (cbTypePick1.getValue().equals("Client complaimnts histogram")) {
				try {
					//loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
				} catch (Exception e) {
					// lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
				// Context.fac.orderReport.produceOrderReport(date, 1);
			}
			else if (cbTypePick1.getValue().equals("Satisfaction Report")) {
				try {
					//loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
				} catch (Exception e) {
					// lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
				// Context.fac.orderReport.produceOrderReport(date, 1);
			}
	
			if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
				@SuppressWarnings("deprecation")
				LocalDate date1 = DatePicker2.getValue();
				for (int i = 0; i < this.stores.size(); i++) {
					if (this.stores.get(i).getName().equals(this.cbStorePick2.getValue()))
						n1 = this.stores.get(i).getStoreID();
				}
				if(this.cbStorePick2.getValue()!=null&&this.cbTypePick2.getValue()!=null)
				{
					if (this.cbTypePick2.getValue().equals("Orders Report")) {
						try {
							// loadGUI("OrderReportFormGUI", false);
						} catch (Exception e) {
							// lblMsg.setText("Loader failed");
							e.printStackTrace();
						}
						if(cbTypePick2.getValue().equals(cbTypePick1.getValue())&&date.equals(date1));
						else
						{
							if(cbTypePick2.getValue().equals(cbTypePick1.getValue()))
							{
								Context.fac.orderReport.produceOrderReport(date1, n1);
							}
							else
								Context.fac.orderReport.initproduceOrderReport(date1, n1);
						}
							
					}
	
					else if (cbTypePick2.getValue().equals("Incomes Report")) {
						//Context.mainScene.loadGUI("IncomesReportFormGUI", false);
						if(cbTypePick2.getValue().equals(cbTypePick1.getValue())&&date.equals(date1));
						else
						{
							if(cbTypePick2.getValue().equals(cbTypePick1.getValue()))
								Context.fac.incomesReport.ProduceIncomesReport(date1, n1);
							else
								Context.fac.incomesReport.initProduceIncomesReport(date1, n1);
						}
					}
	
					else if (cbTypePick2.getValue().equals("Client complaimnts histogram")) {
						Context.mainScene.loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
						// Context.fac.orderReport.produceOrderReport(date1, n1);
					}
				}
			}
			btConfirm1.setVisible(false);
			ToggleReport2.setVisible(false);
		}
	}

	@SuppressWarnings("unchecked")
	public void setStores(ArrayList<Store> stores) {
		this.stores = stores;
		ArrayList<String> ar = new ArrayList<>();
		for (Store store : stores) {
			// if(store.getType().equals(StoreType.Physical))
			ar.add(store.getName());
		}
		cbStorePick1.setItems(FXCollections.observableArrayList(ar));
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			cbStorePick2.setItems(FXCollections.observableArrayList(ar));
			cbStorePick2.setVisible(true);
		}
		this.stores = stores;
	}

	public void setOrderReports(ArrayList<OrderReport> oReports) {
		if (oReports == null)
			return;
		OrderReport rep = oReports.get(0);
		@SuppressWarnings("deprecation")
		LocalDate date = DatePicker1.getValue();
		if (rep.getStoreID().equals(n)&&cbTypePick1.getValue().equals("Orders Report")&&date.equals(rep.getEnddate())) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblFlowerArrcnt1.setText(Integer.toString(rep.getCounterPerType().get(0)+rep.getCounterPerType().get(4)));
					lblFlowerPlacnt1.setText(Integer.toString(rep.getCounterPerType().get(1)+rep.getCounterPerType().get(5)));
					lblBridalBoucnt1.setText(Integer.toString(rep.getCounterPerType().get(2)+rep.getCounterPerType().get(6)));
					lblFlowerClucnt1.setText(rep.getCounterPerType().get(3).toString());
					
					lblFlowerArrsum1.setText(Float.toString(rep.getSumPerType().get(0)+rep.getSumPerType().get(4)));
					lblFlowerPlasum1.setText(Float.toString(rep.getSumPerType().get(1)+rep.getSumPerType().get(5)));
					lblBridalBousum1.setText(Float.toString(rep.getSumPerType().get(2)+rep.getSumPerType().get(6)));
					lblFlowerClusum1.setText(rep.getSumPerType().get(3).toString());
				}
			});
			paneOrderReport1.setVisible(true);
		} else {
			Platform.runLater(new Runnable() {
				public void run() {
					lblFlowerArrcnt2.setText(Integer.toString(rep.getCounterPerType().get(0)+rep.getCounterPerType().get(4)));
					lblFlowerPlacnt2.setText(Integer.toString(rep.getCounterPerType().get(1)+rep.getCounterPerType().get(5)));
					lblBridalBoucnt2.setText(Integer.toString(rep.getCounterPerType().get(2)+rep.getCounterPerType().get(6)));
					lblFlowerClucnt2.setText(rep.getCounterPerType().get(3).toString());
					
					lblFlowerArrsum2.setText(Float.toString(rep.getSumPerType().get(0)+rep.getSumPerType().get(4)));
					lblFlowerPlasum2.setText(Float.toString(rep.getSumPerType().get(1)+rep.getSumPerType().get(5)));
					lblBridalBousum2.setText(Float.toString(rep.getSumPerType().get(2)+rep.getSumPerType().get(6)));
					lblFlowerClusum2.setText(rep.getSumPerType().get(3).toString());
				}
			});
			paneOrderReport2.setVisible(true);
		}
	}
	
	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if(iReports==null)return;
		IncomesReport rep = iReports.get(0);
		@SuppressWarnings("deprecation")
		LocalDate date = DatePicker1.getValue();
		if (rep.getStoreID().equals(n)&&cbTypePick1.getValue().equals("Incomes Report")&&date.equals(rep.getEnddate())) {
			DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
						String s=String.valueOf(rep.getTotIncomes());
						lblStoreID1.setText(rep.getStoreID().toString());
						lblEndDate1.setText(ReqDate.format(rep.getEnddate()));
						lblStartDate1.setText(ReqDate.format(rep.getStartdate()));
						lblTotIncome1.setText(s);
				}
			});
			
			paneIncomeReport1.setVisible(true);
		}
		else
		{
			DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					String s=String.valueOf(rep.getTotIncomes());
					lblStoreID2.setText(rep.getStoreID().toString());
					lblEndDate2.setText(ReqDate.format(rep.getEnddate()));
					lblStartDate2.setText(ReqDate.format(rep.getStartdate()));
					lblTotIncome2.setText(s);
				}
			});
			paneIncomeReport2.setVisible(true);
		}

	}

	public void hideReport2(ActionEvent event) {
		if (!this.ToggleReport2.isSelected()) {
			System.out.println("asdasd");
			paneReport2.setVisible(false);
		} else/* if (btnHide.getText().equals("Ask for two reports"))*/ {
			paneReport2.setVisible(true);
		}

	}
	
	public void Backtomainmenuhandler (ActionEvent event) throws Exception{
		Context.mainScene.loadMainMenu();
	}
}

