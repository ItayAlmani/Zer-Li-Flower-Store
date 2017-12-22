package entities;

import java.util.Date;

public class SurveyReport {

	private String surveyReportID;
	private Survey surveyAnalyzes;
	private String verbalReport;
	private Date date;
	private String storeID;
	
	private static Integer idCounter = 1;
	public SurveyReport() {
		this.surveyReportID = idCounter.toString();
		idCounter++;
	}

	public Survey getSurveyAnalyzes() {
		return this.surveyAnalyzes;
	}

	public void setSurveyAnalyzes(Survey surveyAnalyzes) {
		this.surveyAnalyzes = surveyAnalyzes;
	}

	public String getVerbalReport() {
		return this.verbalReport;
	}

	public void setVerbalReport(String verbalReport) {
		this.verbalReport = verbalReport;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}