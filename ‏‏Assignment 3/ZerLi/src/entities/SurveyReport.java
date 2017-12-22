package entities;
public class SurveyReport {

	private string surveyReportID;
	private Survey surveyAnalyzes;
	private string verbalReport;
	private DateTime date;
	private string storeID;

	public Survey getSurveyAnalyzes() {
		return this.surveyAnalyzes;
	}

	public void setSurveyAnalyzes(Survey surveyAnalyzes) {
		this.surveyAnalyzes = surveyAnalyzes;
	}

	public string getVerbalReport() {
		return this.verbalReport;
	}

	public void setVerbalReport(string verbalReport) {
		this.verbalReport = verbalReport;
	}

	public DateTime getDate() {
		return this.date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

}