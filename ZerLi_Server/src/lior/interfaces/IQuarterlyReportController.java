package lior.interfaces;

public interface IQuarterlyReportController {

	/**
	 * A function that runs a timer that "wakes" once every three months and generates all kinds
	 *  of reports automatically.
	 * The function checks whether it is time to generate the reports ("wake up") if the date is
	 *  the end of one of the quarters.
	 * <b>First quarter</b>:from 1.1 to 31.3.
	 * <b>Second quarter</b>:from  1.4 to 30.6
	 * <b>Third quarter</b>:from 1.7 to 30.9
	 * <b>Fourth quarter</b>:from 1.10 to 31.12
	 */
	void setAutoProductionTimer();

}