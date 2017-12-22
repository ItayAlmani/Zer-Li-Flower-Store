package interfaces;

public interface ISatisfactionReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */
	SatisfactionReport ProduceSatisfactionReport(DateTime Reqdate, string storeID);

}