package entities;

import java.util.Date;

public class Survey {

	private int surveyID;
	private int[] surveyAnswerers;
	private Date date;
	private Store store;

	public int[] getSurveyAnswerers() {
		return this.surveyAnswerers;
	}

	public void setSurveyAnswerers(int[] surveyAnswerers) {
		this.surveyAnswerers = surveyAnswerers;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}