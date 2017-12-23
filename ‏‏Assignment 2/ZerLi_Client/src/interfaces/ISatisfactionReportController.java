package interfaces;

import java.util.ArrayList;
import java.util.Date;

import entities.SatisfactionReport;

public interface ISatisfactionReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	SatisfactionReport ProduceSatisfactionReport(Date Reqdate, String storeID);
}