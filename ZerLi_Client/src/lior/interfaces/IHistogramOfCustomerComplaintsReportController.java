package lior.interfaces;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.HistogramOfCustomerComplaintsReport;
import orderNproducts.entities.Store;

public interface IHistogramOfCustomerComplaintsReportController {

	/**
	 *<p>
	 * The function sends a request to the server to facilitate all Complaints in the requested date period.
	   </p>
	 * @param Reqdate - The date that the quarter will be calculate, this date minus 3 months.
	 * @param store - The store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void getHistogramOfCustomerComplaintsReport(LocalDate Reqdate, Store store) throws IOException;
	/**
	 *<p>
	 * This function is designed to prevent two requests of the same type from being overridden.
		It simply resets the parameters and calls for {@link produceHistogramOfCustomerComplaintsReport}
	   </p> 
	   @see produceHistogramOfCustomerComplaintsReport
	 * @param date - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void initproduceHistogramOfCustomerComplaintsReport(LocalDate date, Store store) throws IOException;
	/**
	 * This function is intended to receive the data from the server and send them to the appropriate GUI class
	 * in this situation it will sent to {@link setHistogramOfCustomerComplaintsReports} in ReportSelectorGUIController
	 * @param ccReports - Arraylist of HistogramOfCustomerComplaints reports that back from the server
	 */
	void handleGet(ArrayList<HistogramOfCustomerComplaintsReport> ccReports);
}