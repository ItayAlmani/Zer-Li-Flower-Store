package customersSatisfaction.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

public class SurveyReport implements Serializable {
	
	private static final long serialVersionUID = 22L;
	private BigInteger surveyReportID;
	private Survey surveyAnalyzes;
	private String verbalReport;
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public SurveyReport(Survey surveyAnalyzes) {
		super();
		this.surveyAnalyzes = surveyAnalyzes;
	}

	public SurveyReport (BigInteger id,Survey surveyAnalyzes, String verbalReport, LocalDateTime startDate,
			LocalDateTime endDate) 
	{
		this.surveyReportID=id;
		this.surveyAnalyzes=surveyAnalyzes;
		this.verbalReport=verbalReport;
		this.startDate=startDate;
		this.endDate=endDate;
	}

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