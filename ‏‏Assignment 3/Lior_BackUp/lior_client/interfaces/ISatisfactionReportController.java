package lior.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.SatisfactionReport;

public interface ISatisfactionReportController  {

	/**
	 *<p>
	 * The function sends a request to the server to facilitate all surveys in the requested date period.
	   </p>
	 * @param Reqdate - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The storeID of the store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void ProduceSatisfactionReport(LocalDate Reqdate, BigInteger storeID) throws IOException;
	/**
	 *<p>
	 * This function is designed to prevent two requests of the same type from being overridden.
		It simply resets the parameters and calls for {@link ProduceSatisfactionReport}
	   </p> 
	   @see ProduceSatisfactionReport
	 * @param date - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void initProduceSatisfactionReport(LocalDate date, BigInteger storeID) throws IOException;
	/**
	 * This function is intended to receive the data from the server and send them to the appropriate GUI class
	 * in this situation it will sent to {@link setSatisfactionReports} in ReportSelectorGUIController
	 * @param sReports - Arraylist of Satisfaction reports that back from the server
	 */
	void handleGet(ArrayList<SatisfactionReport> sReports);
}