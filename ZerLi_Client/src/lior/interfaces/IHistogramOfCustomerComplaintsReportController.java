package lior.interfaces;

import java.math.BigInteger;
import java.time.LocalDate;

import interfaces.IParent;

public interface IHistogramOfCustomerComplaintsReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */

	void ProduceHistogramOfCustomerComplaintsReport(LocalDate Reqdate, BigInteger storeID);
}