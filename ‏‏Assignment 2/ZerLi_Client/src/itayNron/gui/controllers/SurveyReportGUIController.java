package itayNron.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.SurveyReport;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class SurveyReportGUIController implements Initializable
{

	private @FXML TextField txtVerbalReport;
	private @FXML Button btnSubmit;
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
	

	/**
	 * 
	 * @param survey
	 * @throws IOException 
	 */
	@FXML public void loadSurveyAnalyzesToGUI() throws IOException {
		Context.fac.surveyReport.getSurveysForAnalays(dpDateStart.getValue(), dpDateEnd.getValue());
	}
	
	public void setSurveyReports(ArrayList<SurveyReport> surveys) {
		if(surveys!=null && surveys.size()!=0&&surveys.get(0)!=null) {
			SurveyReport sr = surveys.get(0);
			if(sr.getSurveyAnalyzes()!=null) {
				float[] ans = sr.getSurveyAnalyzes().getSurveyAnswerers();
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
				System.err.println("getSurveyAnalyzes() returns null");
		}
		else
			System.err.println("surveys is null");
	}
	
	

	/**
	 * 
	 * @param txtVerbalReport
	 */
	public void attachVerbalReport(TextField txtVerbalReport) {
		// TODO - implement SurveyReportGUI.attachVerbalReport
		throw new UnsupportedOperationException();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
		
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