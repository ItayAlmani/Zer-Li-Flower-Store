package lior.interfaces;

import java.util.ArrayList;
import java.util.Date;

import entities.IncomesReport;
import interfaces.IParent;

public interface IIncomesReportController extends IParent {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	IncomesReport ProduceIncomesReport(Date Reqdate, int storeID);
}