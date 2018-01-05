package lior.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

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

public class ReportSelectorGUIController implements Initializable {

	private ArrayList<Store> stores;
	private BigInteger n, n1;
	private @FXML Button askforreport,backbutton,btnHide;
	private @FXML ComboBox<String> StoreCB,TypeCB,StoreCB1,TypeCB1;
	private @FXML ComboBox<Integer> DayCB,MonthCB,YearCB,DayCB1,MonthCB1,YearCB1;
	//Order Report General Variables
	private @FXML AnchorPane paneorder1,paneorder11,paneReport2;
	//Order Report 1 Variables
	private @FXML Label lblBBousum1,lblfPlntsum1,lblfArrnsum1,lblFClusum1,lblFClucnt1,lblBBoucnt1,lblfPlntcnt1
	,lblfArrncnt1,lblTotPrice1,lblNumOford1,lblFlowClu1,lblBridBou1,lblFlopl1,lblFloArr1;
	//Order Report 2 Variables
	private @FXML Label lblBBousum11,lblfPlntsum11,lblfArrnsum11,lblFClucnt11,lblBBoucnt11,lblfPlntcnt11
	,lblfArrncnt11,lblTotPrice11,lblNumOford11,lblFlowClu11,lblBridBou11,lblFlopl11,lblFloArr11,
	lblFClusum11,lblReport2;
	//Income Report General Variables
	private @FXML AnchorPane paneTotincome,paneTotincome1;
	//Income Report 1 Variables
	private @FXML Label lblIncometitle,lblincomeDataerr,lblTotIncomeSum,lblIncomeStoreID,lblIncomeEndDate
	,lblIncomeStartDate,lblTotIncome,lblIncomeStore,lblIncomeTo,lblIncomeFrom;
	//Income Report 2 Variables
	private @FXML Label lblIncomeFrom1,lblIncomeTo1,lblIncomeStore1,lblTotIncome1,lblIncomeStartDate1,
	lblIncomeEndDate1,lblIncomeStoreID1,lblTotIncomeSum1,lblincomeDataerr1,lblIncometitle1;

	
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/ReportSelectorGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Report Menu");
		stage.setScene(scene);
		stage.show();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
		paneorder1.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		paneorder11.setBorder(new Border(new BorderStroke(Color.BLUE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		paneTotincome.setBorder(new Border(new BorderStroke(Color.FORESTGREEN,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		paneTotincome1.setBorder(new Border(new BorderStroke(Color.BLUE,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		DateFormat day = new SimpleDateFormat("dd");
		DateFormat Month = new SimpleDateFormat("MM");
		DateFormat Year = new SimpleDateFormat("yyyy");
		DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		ReqDate.format(date);
		String str = Year.format(date);
		YearCB.getItems().removeAll(YearCB.getItems());
		for (int i = 1980; i <= Integer.parseInt(str); i++)
			YearCB.getItems().add(i);
		YearCB.setValue(Integer.parseInt(str));
		str = Month.format(date);
		for (int i = 1; i <= 12; i++)
			MonthCB.getItems().add(i);
		MonthCB.setValue(Integer.parseInt(str));
		str = day.format(date);
		for (int i = 1; i <= 31; i++)
			DayCB.getItems().add(i);
		DayCB.setValue(Integer.parseInt(str) - 1);
		TypeCB.getItems().add("Incomes Report");
		TypeCB.getItems().add("Orders Report");
		TypeCB.getItems().add("Client complaimnts histogram");
		TypeCB.getItems().add("Satisfaction Report");
		try {
			Context.fac.store.getAllStores();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			str = Year.format(date);
			for (int i = 1980; i <= Integer.parseInt(str); i++)
				YearCB1.getItems().add(i);
			YearCB1.setValue(Integer.parseInt(str));
			str = Month.format(date);
			for (int i = 1; i <= 12; i++)
				MonthCB1.getItems().add(i);
			MonthCB1.setValue(Integer.parseInt(str));
			str = day.format(date);
			for (int i = 1; i <= 31; i++)
				DayCB1.getItems().add(i);
			DayCB1.setValue(Integer.parseInt(str) - 1);
			TypeCB1.getItems().add("Incomes Report");
			TypeCB1.getItems().add("Orders Report");
			TypeCB1.getItems().add("Client complaimnts histogram");
			TypeCB1.getItems().add("Satisfaction Report");
			YearCB1.setVisible(true);
			MonthCB1.setVisible(true);
			DayCB1.setVisible(true);
			TypeCB1.setVisible(true);
			btnHide.setVisible(true);
		}

	}

	public void askforreportHandler(ActionEvent event) throws Exception {
		@SuppressWarnings("deprecation")
		Date date = new Date(YearCB.getValue() - 1900, MonthCB.getValue() - 1, DayCB.getValue());
		for (int i = 0; i < this.stores.size(); i++) {
			if (this.stores.get(i).getName().equals(this.StoreCB.getValue()))
				n = this.stores.get(i).getStoreID();
		}
		if (this.TypeCB.getValue().equals("Orders Report")) {
			try {
			} catch (Exception e) {
				// lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			Context.fac.orderReport.initproduceOrderReport(date, n);
		}

		else if (TypeCB.getValue().equals("Incomes Report")) {
			try {
				//loadGUI("IncomesReportFormGUI", false);
			} catch (Exception e) {
				// lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			Context.fac.incomesReport.initProduceIncomesReport(date, n);
		}

		else if (TypeCB.getValue().equals("Client complaimnts histogram")) {
			try {
				//loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
			} catch (Exception e) {
				// lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			// Context.fac.orderReport.produceOrderReport(date, 1);
		}
		else if (TypeCB.getValue().equals("Client complaimnts histogram")) {
			try {
				//loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
			} catch (Exception e) {
				// lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			// Context.fac.orderReport.produceOrderReport(date, 1);
		}
		/*
		 * if(Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)
		 * ) {
		 * 
		 * @SuppressWarnings("deprecation") Date date1 = new
		 * Date(YearCB1.getValue()-1900, MonthCB1.getValue()-1, DayCB1.getValue());
		 * for(int i=0;i<this.stores.size();i++) {
		 * if(this.stores.get(i).getName().equals(this.StoreCB1.getValue()))
		 * n1=this.stores.get(i).getStoreID(); }
		 * if(this.TypeCB1.getValue().equals("Orders Report")) { try {
		 * Context.prevGUI=this; //loadGUI("OrderReportFormGUI", false); } catch
		 * (Exception e) { //lblMsg.setText("Loader failed"); e.printStackTrace(); } //
		 * Context.fac.orderReport.produceOrderReport(date1, n1); } }
		 */
		
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			@SuppressWarnings("deprecation")
			Date date1 = new Date(YearCB1.getValue() - 1900, MonthCB1.getValue() - 1, DayCB1.getValue());
			for (int i = 0; i < this.stores.size(); i++) {
				if (this.stores.get(i).getName().equals(this.StoreCB1.getValue()))
					n1 = this.stores.get(i).getStoreID();
			}
			if(this.StoreCB1.getValue()!=null&&this.TypeCB1.getValue()!=null)
			{
				if (this.TypeCB1.getValue().equals("Orders Report")) {
					try {
						// loadGUI("OrderReportFormGUI", false);
					} catch (Exception e) {
						// lblMsg.setText("Loader failed");
						e.printStackTrace();
					}
					if(TypeCB1.getValue().equals(TypeCB.getValue()))
						Context.fac.orderReport.produceOrderReport(date1, n1);
					else
						Context.fac.orderReport.initproduceOrderReport(date, n);
				}

				else if (TypeCB1.getValue().equals("Incomes Report")) {
					//Context.mainScene.loadGUI("IncomesReportFormGUI", false);
					if(TypeCB1.getValue().equals(TypeCB.getValue()))
						Context.fac.incomesReport.ProduceIncomesReport(date1, n1);
					else
						Context.fac.incomesReport.initProduceIncomesReport(date1, n1);
				}

				else if (TypeCB.getValue().equals("Client complaimnts histogram")) {
					Context.mainScene.loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
					// Context.fac.orderReport.produceOrderReport(date1, n1);
				}
			}
		}
		askforreport.setVisible(false);
		btnHide.setVisible(false);
	}

	public void setStores(ArrayList<Store> stores) {
		this.stores = stores;
		ArrayList<String> ar = new ArrayList<>();
		for (Store store : stores) {
			// if(store.getType().equals(StoreType.Physical))
			ar.add(store.getName());
		}
		StoreCB.setItems(FXCollections.observableArrayList(ar));
		if (Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			StoreCB1.setItems(FXCollections.observableArrayList(ar));
			StoreCB1.setVisible(true);
		}
		this.stores = stores;
	}

	public void setOrderReports(ArrayList<OrderReport> oReports) {
		if (oReports == null)
			return;
		OrderReport rep = oReports.get(0);
		if (rep.getStoreID().equals(n)&&TypeCB.getValue().equals("Orders Report")) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblfArrncnt1.setText(Integer.toString(rep.getCounterPerType().get(0)+rep.getCounterPerType().get(4)));
					lblfPlntcnt1.setText(Integer.toString(rep.getCounterPerType().get(1)+rep.getCounterPerType().get(5)));
					lblBBoucnt1.setText(Integer.toString(rep.getCounterPerType().get(2)+rep.getCounterPerType().get(6)));
					lblFClucnt1.setText(rep.getCounterPerType().get(3).toString());
					lblfArrnsum1.setText(Float.toString(rep.getSumPerType().get(0)+rep.getSumPerType().get(4)));
					lblfPlntsum1.setText(Float.toString(rep.getSumPerType().get(1)+rep.getSumPerType().get(5)));
					lblBBousum1.setText(Float.toString(rep.getSumPerType().get(2)+rep.getSumPerType().get(6)));
					lblFClusum1.setText(rep.getSumPerType().get(3).toString());

				}
			});
			paneorder1.setVisible(true);
			//oReports.remove(0);
		} else {
			Platform.runLater(new Runnable() {
				public void run() {
					lblfArrncnt11.setText(Integer.toString(rep.getCounterPerType().get(0)+rep.getCounterPerType().get(4)));
					lblfPlntcnt11.setText(Integer.toString(rep.getCounterPerType().get(1)+rep.getCounterPerType().get(5)));
					lblBBoucnt11.setText(Integer.toString(rep.getCounterPerType().get(2)+rep.getCounterPerType().get(6)));
					lblFClucnt11.setText(rep.getCounterPerType().get(3).toString());

					lblfArrnsum11.setText(Float.toString(rep.getSumPerType().get(0)+rep.getSumPerType().get(4)));
					lblfPlntsum11.setText(Float.toString(rep.getSumPerType().get(1)+rep.getSumPerType().get(5)));
					lblBBousum11.setText(Float.toString(rep.getSumPerType().get(2)+rep.getSumPerType().get(6)));
					lblFClusum11.setText(rep.getSumPerType().get(3).toString());

				}
			});
			paneorder11.setVisible(true);
			//oReports.remove(1);
		}
	}
	
	public void setIncomeReports(ArrayList<IncomesReport> iReports) {
		if(iReports==null)return;
		IncomesReport rep = iReports.get(0);
		if (rep.getStoreID().equals(n)&&TypeCB.getValue().equals("Incomes Report")) {
			DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if(rep.getTotIncomes()!=-1) {
						String s=String.valueOf(rep.getTotIncomes());
						lblIncomeStoreID.setText(rep.getStoreID().toString());
						lblIncomeEndDate.setText(ReqDate.format(rep.getEnddate()));
						lblIncomeStartDate.setText(ReqDate.format(rep.getStartdate()));
						lblTotIncomeSum.setText(s);
						lblincomeDataerr.setVisible(false);
					}
					else {
						lblIncomeFrom.setVisible(false);
						lblIncomeTo.setVisible(false);
						lblIncomeStore.setVisible(false);
						lblTotIncome.setVisible(false);
						lblIncomeStartDate.setVisible(false);
						lblIncomeEndDate.setVisible(false);
						lblIncomeStoreID.setVisible(false);
						lblTotIncomeSum.setVisible(false);
						lblincomeDataerr.setVisible(true);
					}
						
				}
			});
			
			paneTotincome.setVisible(true);
		}
		else
		{
			DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					String s=String.valueOf(rep.getTotIncomes());
					lblIncomeStoreID1.setText(rep.getStoreID().toString());
					lblIncomeEndDate1.setText(ReqDate.format(rep.getEnddate()));
					lblIncomeStartDate1.setText(ReqDate.format(rep.getStartdate()));
					lblTotIncomeSum1.setText(s);
				}
			});
			paneTotincome1.setVisible(true);
		}

	}

	public void hideReport2(ActionEvent event) {
		if (btnHide.getText().equals("Hide Report 2")) {
			paneReport2.setVisible(false);
			btnHide.setText("Ask for two reports");
		} else if (btnHide.getText().equals("Ask for two reports")) {
			paneReport2.setVisible(true);
			btnHide.setText("Hide Report 2");
		}

	}
	
	public void Backtomainmenuhandler (ActionEvent event) throws Exception
	{
		Context.mainScene.loadMainMenu();
	}
}

