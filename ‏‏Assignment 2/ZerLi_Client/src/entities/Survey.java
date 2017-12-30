package entities;

import java.math.BigInteger;
import java.time.LocalDate;

public class Survey {

	private BigInteger surveyID;
	private int[] surveyAnswerers;
	private LocalDate date;
	private Store store;
	private SurveyType type;
	
	private static BigInteger idInc = null;
	
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

	public BigInteger getSurveyID() {
		return surveyID;
	}

	public void setSurveyID(BigInteger surveyID) {
		this.surveyID = surveyID;
	}

	public SurveyType getType() {
		return type;
	}

	public void setType(SurveyType type) {
		this.type = type;
	}

	public static BigInteger getIdInc() {
		return idInc;
	}

	public static void setIdInc(BigInteger idInc) {
		Survey.idInc = idInc;
	}

}