package entities;
public class Survey {

	private string surveyID;
	private int[] surveyAnswerers;
	private DateTime date;
	private Store store;

	public int[] getSurveyAnswerers() {
		return this.surveyAnswerers;
	}

	public void setSurveyAnswerers(int[] surveyAnswerers) {
		this.surveyAnswerers = surveyAnswerers;
	}

	public DateTime getDate() {
		return this.date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}