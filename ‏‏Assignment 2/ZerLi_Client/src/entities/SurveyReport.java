package entities;

import java.math.BigInteger;
import java.util.Date;

public class SurveyReport {

	private BigInteger surveyReportID;
	private BigInteger storeID;
	private Survey surveyAnalyzes;
	private String verbalReport;
	private Date date;

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