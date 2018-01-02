package lior.gui.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.OrderReport;
import entities.Store;
import entities.Store.StoreType;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

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
	@FXML Button btAnotherreport;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		btAnotherreport.setVisible(false);
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
		if(Context.getUser().getPermissions().equals(User.UserType.ChainStoreManager)) {
			btAnotherreport.setVisible(true);
			btAnotherreport.setOnAction(g->{
				/*try {
					loadGUI("ReportSelectorGUI", false);
				} catch (Exception e) {
					lblMsg.setText("Loader failed");
					e.printStackTrace();
				}
			}*/		Stage seconderyStage = new Stage();
			Scene scene;
			try {
				scene = new Scene(
						new FXMLLoader(getClass().getResource("/gui/fxmls/"+"ReportSelectorGUI"+".fxml")).load()
						);
				scene.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
				
				seconderyStage.setScene(scene);
				seconderyStage.setTitle("ReportSelectorGUI".split("GUI")[0].trim());
				seconderyStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	}

	@FXML public void GoToMainMenu(ActionEvent event) {super.loadMainMenu();}
	

}