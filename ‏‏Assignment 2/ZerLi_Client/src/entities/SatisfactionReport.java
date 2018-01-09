package entities;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Not DB table -> no association class
 */
public class SatisfactionReport extends QuarterlyReport {

	private ArrayList<Survey> Surveys=new ArrayList<>();
	private LocalDate Startdate;
	private LocalDate Enddate;
	private float[] finalanswers;
	private float AverageTotanswer;
	
	public float getAverageTotanswer() {
		return AverageTotanswer;
	}
	public void setAverageTotanswer(float averageTotanswer) {
		AverageTotanswer = averageTotanswer;
	}
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
<<<<<<< HEAD
=======


>>>>>>> branch 'master' of https://github.com/ItayAlmani/Zer-Li-Flower-Store.git
}