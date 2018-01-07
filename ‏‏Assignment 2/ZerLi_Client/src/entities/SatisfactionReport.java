package entities;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Not DB table -> no association class
 */
public class SatisfactionReport extends QuarterlyReport {

	private ArrayList<Survey> Surveys;
	private LocalDate Startdate;
	private LocalDate Enddate;
	private float[] finalanswers;
	
	public float[] getFinalanswers() {
		return finalanswers;
	}
	public void setFinalanswers(float[] finalanswers) {
		this.finalanswers = finalanswers;
	}
	public ArrayList<Survey> getSurveys() {
		return Surveys;
	}
	public void setSurveys(ArrayList<Survey> Surveys) {
		this.Surveys = Surveys;
	}
	public LocalDate getStartdate() {
		return Startdate;
	}
	public void setStartdate(LocalDate startdate) {
		Startdate = startdate;
	}
	public LocalDate getEnddate() {
		return Enddate;
	}
	public void setEnddate(LocalDate enddate) {
		Enddate = enddate;
	}


}