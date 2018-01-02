package lior.interfaces;

import java.math.BigInteger;
import java.util.Date;

import entities.CustomerComplaintsReport;
import interfaces.IParent;

public interface IHistogramOfCustomerComplaintsReportController extends IParent {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	CustomerComplaintsReport ProduceHistogramOfCustomerComplaintsReport(Date Reqdate, BigInteger storeID);
}