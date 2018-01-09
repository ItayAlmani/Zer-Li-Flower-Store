package lior.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import entities.SatisfactionReport;
import interfaces.IParent;

public interface ISatisfactionReportController  {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 * @throws IOException 
	 */

	void ProduceSatisfactionReport(LocalDate Reqdate, BigInteger storeID) throws ParseException, IOException;
}