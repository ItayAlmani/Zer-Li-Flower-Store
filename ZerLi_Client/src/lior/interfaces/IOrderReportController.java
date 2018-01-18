package lior.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.OrderReport;

public interface IOrderReportController{

	/**
	 *<p>
	 * The function sends a request to the server to facilitate all Orders in the requested date period.
	   </p>
	 * @param date - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The storeID of the store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void produceOrderReport(LocalDate date, BigInteger storeID) throws IOException;
	/**
	 *<p>
	 * This function is designed to prevent two requests of the same type from being overridden.
		It simply resets the parameters and calls for {@link produceOrderReport}
	   </p> 
	   @see produceOrderReport
	 * @param date - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void initproduceOrderReport(LocalDate date, BigInteger storeID) throws IOException;
	/**
	 * This function is intended to receive the data from the server and send them to the appropriate GUI class
	 * in this situation it will sent to {@link setOrderReports} in ReportSelectorGUIController
	 * @param oReports - Arraylist of Order reports that back from the server
	 */
	void handleGet(ArrayList<OrderReport> oReports);
}