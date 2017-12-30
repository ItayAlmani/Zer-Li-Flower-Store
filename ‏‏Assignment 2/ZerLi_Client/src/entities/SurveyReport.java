package entities;

import java.math.BigInteger;
import java.util.Date;

public class SurveyReport {

	private BigInteger surveyReportID;
	private BigInteger storeID;
	private Survey surveyAnalyzes;
	private String verbalReport;
	private Date date;
	
	private static BigInteger idInc = null;

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

	public BigInteger getSurveyReportID() {
		return surveyReportID;
	}

	public void setSurveyReportID(BigInteger surveyReportID) {
		this.surveyReportID = surveyReportID;
	}

	public BigInteger getStoreID() {
		return storeID;
	}

	public void setStoreID(BigInteger storeID) {
		this.storeID = storeID;
	}

	public static BigInteger getIdInc() {
		return idInc;
	}

	public static void setIdInc(BigInteger idInc) {
		SurveyReport.idInc = idInc;
	}

}