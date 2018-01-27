package customersSatisfaction.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import customersSatisfaction.entities.Survey.SurveyType;
import customersSatisfaction.entities.SurveyReport;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class SurveyReportGUIController implements Initializable
{
	private @FXML Button btnSend;
	private @FXML Label l1, l2, l3, l4, l5, l6;
	private @FXML DatePicker dpDateStart, dpDateEnd;
	private @FXML TextArea txtVerbal;
	private @FXML VBox vboxVerbal;
	private SurveyReport sr;
	/**
	 * <p>
	 * Function to load analyzes of surveys in specific range of dates
	 * </p>
	 * @throws IOException 
	 */
	@FXML public void loadSurveyAnalyzesToGUI() throws IOException {
		Context.mainScene.setMenuPaneDisable(true);
		Context.fac.surveyReport.analyzeSurveys(
				dpDateStart.getValue().atStartOfDay(), dpDateEnd.getValue().atStartOfDay());
	}
	/**
	 * <p>
	 * Function to add new surveyReport to DB
	 * </p>
	 * @throws IOException 
	 */
	public void sendSurveyReport() throws IOException
	{
		if(txtVerbal.getText()==null || txtVerbal.getText().isEmpty()) {
			Context.mainScene.setMessage("You must enter verbal analyzes");
			return;
		}
		sr.setVerbalReport(txtVerbal.getText());
		sr.getSurveyAnalyzes().setType(SurveyType.Analyzes);
		sr.setStartDate(dpDateStart.getValue().atStartOfDay());
		sr.setEndDate(dpDateEnd.getValue().atStartOfDay());	

		Context.fac.surveyReport.add(sr, false);
		Context.mainScene.loadMainMenu();
	}
	
	/**
	 * Function to set all the survey report answers into GUI
	 * @param surveys - ArrayList of SurveyReport 
	 */
	public void setSurveyReports(ArrayList<SurveyReport> surveys) {
		Context.mainScene.setMenuPaneDisable(false);
		if(surveys!=null && surveys.size()!=0&&surveys.get(0)!=null) {
			this.sr = surveys.get(0);
			if(sr.getSurveyAnalyzes()!=null) {
				float [] ans = sr.getSurveyAnalyzes().getSurveyAnswerers();
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						l1.setText(((Float)ans[0]).toString());
						l2.setText(((Float)ans[1]).toString());
						l3.setText(((Float)ans[2]).toString());
						l4.setText(((Float)ans[3]).toString());
						l5.setText(((Float)ans[4]).toString());
						l6.setText(((Float)ans[5]).toString());
						vboxVerbal.setVisible(true);
					}
				});
			}
			else
				Context.mainScene.loadMainMenu("getSurveyAnalyzes() returns null");
		}
		else
			Context.mainScene.loadMainMenu("surveys is null");
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		dpDateStart.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                //disable today and every date after
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
		dpDateEnd.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                //disable today and every date after
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
		dpDateStart.setValue(LocalDate.now());
		dpDateEnd.setValue(LocalDate.now());
	}
}