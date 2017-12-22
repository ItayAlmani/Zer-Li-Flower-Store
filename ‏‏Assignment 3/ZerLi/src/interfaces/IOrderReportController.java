package interfaces;

import java.util.Date;

import entities.OrderReport;

public interface IOrderReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	OrderReport ProduceOrderReport(Date Reqdate, String storeID);

}