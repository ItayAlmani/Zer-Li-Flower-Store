package lior.interfaces;

import java.util.ArrayList;
import java.util.Date;

import entities.SatisfactionReport;
import interfaces.IParent;

public interface ISatisfactionReportController extends IParent  {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	SatisfactionReport ProduceSatisfactionReport(Date Reqdate, int storeID);
}