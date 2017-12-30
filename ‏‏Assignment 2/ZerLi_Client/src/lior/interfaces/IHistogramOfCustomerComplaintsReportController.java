package lior.interfaces;

import java.util.ArrayList;
import java.util.Date;

import entities.CustomerComplaintsReport;
import interfaces.IParent;

public interface IHistogramOfCustomerComplaintsReportController extends IParent {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	CustomerComplaintsReport ProduceHistogramOfCustomerComplaintsReport(Date Reqdate, int storeID);
}