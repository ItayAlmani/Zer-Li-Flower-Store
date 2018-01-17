package lior.interfaces;

import java.io.IOException;
import java.time.LocalDate;

import entities.Store;

public interface IHistogramOfCustomerComplaintsReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */

	void produceHistogramOfCustomerComplaintsReport(LocalDate Reqdate, Store store) throws IOException;
}