package lior.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

import entities.IncomesReport;

public interface IIncomesReportController {

	/**
	 *<p>
	 * The function sends a request to the server to facilitate all Orders in the requested date period.
	   </p>
	 * @param date - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The storeID of the store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void ProduceIncomesReport(LocalDate date, BigInteger storeID) throws IOException;
	/**
	 *<p>
	 * This function is designed to prevent two requests of the same type from being overridden.
		It simply resets the parameters and calls for {@link ProduceIncomesReport}
	   </p> 
	   @see ProduceIncomesReport
	 * @param date - The date that the quarter will be calculate, this date minus 3 months.
	 * @param storeID - The store that the report was created for. 
	 * @throws IOException - Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void initProduceIncomesReport(LocalDate date, BigInteger storeID) throws IOException;
	/**
	 * This function is intended to receive the data from the server and send them to the appropriate GUI class
	 * in this situation it will sent to {@link setIncomeReports} in ReportSelectorGUIController
	 * @param iReports - Arraylist of Incomes report that back from the server
	 */
	void handleGet(ArrayList<IncomesReport> iReports);
}