package lior.interfaces;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import entities.SatisfactionReport;
import interfaces.IParent;

public interface ISatisfactionReportController extends IParent  {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 */

	void ProduceSatisfactionReport(LocalDate Reqdate, BigInteger storeID) throws ParseException;
}