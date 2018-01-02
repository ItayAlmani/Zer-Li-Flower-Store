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
import controllers.ParentController;
import entities.Store;
import entities.Store.StoreType;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ReportSelectorGUIController extends ParentGUIController {
	
	private @FXML Button askforreport;
	private @FXML Button backbutton;
	private @FXML ComboBox<String> StoreCB;
	private @FXML ComboBox<String> TypeCB;
	private @FXML ComboBox<Integer> DayCB;
	private @FXML ComboBox<Integer> MonthCB;
	public @FXML ComboBox<Integer> YearCB;
	
	private ArrayList<Store> stores;
	
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxmls/ReportSelectorGUI.fxml"));
		Scene scene = new Scene(loader.load());
		stage.setTitle("Report Menu");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		DateFormat day = new SimpleDateFormat("dd");
		DateFormat Month = new SimpleDateFormat("MM");
		DateFormat Year = new SimpleDateFormat("yyyy");
		DateFormat ReqDate = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		ReqDate.format(date);
		String str=Year.format(date);
		YearCB.getItems().removeAll(YearCB.getItems());
		for(int i=1980;i<=Integer.parseInt(str);i++)
			YearCB.getItems().add(i);
		YearCB.setValue(Integer.parseInt(str));
		str=Month.format(date);
		for(int i=1;i<=12;i++)
			MonthCB.getItems().add(i);
		MonthCB.setValue(Integer.parseInt(str));
		str=day.format(date);
		for(int i=1;i<=31;i++)
			DayCB.getItems().add(i);
		DayCB.setValue(Integer.parseInt(str)-1);
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
	}
	
	public void askforreportHandler (ActionEvent event) throws Exception
	{
		@SuppressWarnings("deprecation")
		Date date = new Date(YearCB.getValue()-1900, MonthCB.getValue()-1, DayCB.getValue());
		BigInteger n=null;
		for(int i=0;i<this.stores.size();i++)
			if(this.stores.get(i).getName().equals(this.StoreCB.getValue()))
					n=this.stores.get(i).getStoreID();
		if(this.TypeCB.getValue().equals("Orders Report"))
		{
			try {
				loadGUI("OrderReportFormGUI", false);
			} catch (Exception e) {
				//lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			Context.fac.orderReport.produceOrderReport(date, n);
		}
		
		else if(TypeCB.getValue().equals("Incomes Report"))
		{
			try {
				loadGUI("IncomesReportFormGUI", false);
			} catch (Exception e) {
				//lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			Context.fac.incomesReport.ProduceIncomesReport(date, n);
		}
		
		else if(TypeCB.getValue().equals("Client complaimnts histogram"))
		{
			try {
				loadGUI("HistogramOfCustomerComplaintsFormGUI", false);
			} catch (Exception e) {
				//lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
			//Context.fac.orderReport.produceOrderReport(date, 1);
		}
			
		/*DateFormat day = new SimpleDateFormat("dd");
		DateFormat Month = new SimpleDateFormat("MM");
		DateFormat Year = new SimpleDateFormat("yyyy");
		 * String yearstr=Year.format(date);
		String monthstr=Month.format(date);
		String daystr=day.format(date);
		 if(YearCB.getValue()>Integer.parseInt(yearstr))
			System.out.println(DayCB.getValue()+","+MonthCB.getValue()+","+YearCB.getValue());
		else {
			if(MonthCB.getValue()>Integer.parseInt(monthstr))
				System.out.println("Error Month");
			else if(DayCB.getValue()>Integer.parseInt(daystr)-1)
				System.out.println("Error Day");
			else
				System.out.println(DayCB.getValue()+","+MonthCB.getValue()+","+YearCB.getValue());*/
		}
		
	
	public void setStores(ArrayList<Store> stores) {
		this.stores=stores;
		ArrayList<String> ar = new ArrayList<>();
		for (Store store : stores) {
			if(store.getType().equals(StoreType.Physical))
				ar.add(store.getName());
		}
		StoreCB.setItems(FXCollections.observableArrayList(ar));
		this.stores = stores;
	}
	}
