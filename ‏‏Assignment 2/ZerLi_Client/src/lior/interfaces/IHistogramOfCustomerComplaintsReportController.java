package lior.interfaces;

import java.util.ArrayList;
import java.util.Date;

import interfaces.IParent;

public interface IHistogramOfCustomerComplaintsReportController extends IParent {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	HistogramOfCustomerComplaintsReport ProduceHistogramOfCustomerComplaintsReport(Date Reqdate, int storeID);
}