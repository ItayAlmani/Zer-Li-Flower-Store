package entities;

import java.math.BigInteger;
import java.time.LocalDate;

public class Survey {

	private BigInteger surveyID;
	private float[] surveyAnswerers;
	private LocalDate date;
	private BigInteger storeID;
	private SurveyType type;
	
	private static BigInteger idInc = null;
	
	public Survey(float[] surveyAnswerers, LocalDate date, BigInteger storeID, SurveyType type) {
		super();
		this.surveyAnswerers = surveyAnswerers;
		this.date = date;
		this.storeID = storeID;
		this.type = type;
	}
	public Survey(BigInteger surveyID,float[] surveyAnswerers, LocalDate date, BigInteger storeID, SurveyType type) {
		super();
		this.surveyID=surveyID;
		this.surveyAnswerers = surveyAnswerers;
		this.date = date;
		this.storeID = storeID;
		this.type = type;
	}
	public enum SurveyType{
		Answer, Analyzes;
	}
	
	public Survey(float[] surveyAnswerers) {
		this.surveyAnswerers=surveyAnswerers;
	}

	public Survey(float[] surveyAnswerers, LocalDate date, BigInteger storeID) {
		super();
		this.surveyAnswerers = surveyAnswerers;
		this.date = date;
		this.storeID = storeID;
	}

	public float[] getSurveyAnswerers() {
		return this.surveyAnswerers;
	}

	public void setSurveyAnswerers(float[] surveyAnswerers) {
		this.surveyAnswerers = surveyAnswerers;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public BigInteger getStoreID() {
		return this.storeID;
	}

	public void setStore(BigInteger storeID) {
		this.storeID = storeID;
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