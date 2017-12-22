package interfaces;

import java.util.Date;

public interface IHistogramOfCustomerComplaintsReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	HistogramOfCustomerComplaintsReport ProduceHistogramOfCustomerComplaintsReport(Date Reqdate, String storeID);

}