package entities;

import java.time.LocalDate;
import java.util.Date;

public class Survey {

	private int surveyID;
	private int[] surveyAnswerers;
	private LocalDate date;
	private Store store;
	private SurveyType type;
	
	public Survey(int[] surveyAnswerers, LocalDate date, Store store, SurveyType type) {
		super();
		this.surveyAnswerers = surveyAnswerers;
		this.date = date;
		this.store = store;
		this.type = type;
	}

	public enum SurveyType{
		Answer, Analyzes;
	}

	public Survey(int[] surveyAnswerers, LocalDate date, Store store) {
		super();
		this.surveyAnswerers = surveyAnswerers;
		this.date = date;
		this.store = store;
	}

	public int[] getSurveyAnswerers() {
		return this.surveyAnswerers;
	}

	public void setSurveyAnswerers(int[] surveyAnswerers) {
		this.surveyAnswerers = surveyAnswerers;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public int getSurveyID() {
		return surveyID;
	}

	public void setSurveyID(int surveyID) {
		this.surveyID = surveyID;
	}

	public SurveyType getType() {
		return type;
	}

	public void setType(SurveyType type) {
		this.type = type;
	}

}