package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class SurveyReport implements Serializable {

	public SurveyReport(Survey surveyAnalyzes) {
		super();
		this.surveyAnalyzes = surveyAnalyzes;
	}

	private BigInteger surveyReportID;
	private Survey surveyAnalyzes;
	private String verbalReport;
	private LocalDateTime startDate;
	private LocalDateTime endDate;



	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
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

	

	public BigInteger getSurveyReportID() {
		return surveyReportID;
	}

	public void setSurveyReportID(BigInteger surveyReportID) {
		this.surveyReportID = surveyReportID;
	}
}