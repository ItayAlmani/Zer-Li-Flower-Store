package interfaces;

import java.util.Date;

import entities.IncomesReport;

public interface IIncomesReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	IncomesReport ProduceIncomesReport(Date Reqdate, String storeID);

}