package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Survey.SurveyType;
import entities.SurveyReport;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SurveyReportGUIController implements Initializable
{
	private @FXML TextField txtVerbalReport;
	private @FXML Button btnSubmit;
	private @FXML Button btnSend;
	private @FXML Label l1;
	private @FXML Label l2;
	private @FXML Label l3;
	private @FXML Label l4;
	private @FXML Label l5;
	private @FXML Label l6;
	@FXML DatePicker dpDateStart;
	@FXML DatePicker dpDateEnd;
	@FXML TextArea txtVerbal;
	@FXML VBox vboxVerbal;
	private SurveyReport sr;

	

	/**
	 * 
	 * @param survey
	 * @throws IOException 
	 */
	@FXML public void loadSurveyAnalyzesToGUI() throws IOException {
		Context.fac.surveyReport.analyzeSurveys(dpDateStart.getValue(), dpDateEnd.getValue());
	}
	
	@FXML public void sendSurveyReport(ActionEvent event) throws IOException
	{
		sr.setVerbalReport(txtVerbal.getText());
		sr.getSurveyAnalyzes().setType(SurveyType.Analyzes);
		sr.setStartDate(dpDateStart.getValue().atStartOfDay());
		sr.setEndDate(dpDateEnd.getValue().atStartOfDay());	

		Context.fac.surveyReport.add(sr, false);
	}
	
	public void setSurveyReports(ArrayList<SurveyReport> surveys) {
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
	
	

	/**
	 * 
	 * @param txtVerbalReport
	 */
	public void attachVerbalReport(TextArea txtVerbalReport)
	{
		txtVerbalReport.getText();
		throw new UnsupportedOperationException();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		
		dpDateStart.setValue(LocalDate.now());
		dpDateEnd.setValue(LocalDate.now());
		l1.setText("");
		l2.setText("");
		l3.setText("");
		l4.setText("");
		l5.setText("");
		l6.setText("");
	}

	public void loadMainMenu() {
		Context.mainScene.loadMainMenu();
	}
}