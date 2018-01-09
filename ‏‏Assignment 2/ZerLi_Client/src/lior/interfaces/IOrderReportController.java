package lior.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import interfaces.IParent;

public interface IOrderReportController{

	/**
	 * 
	 * @param reqDate
	 * @param storeID
	 * @throws ParseException 
	 * @throws IOException 
	 */
	void produceOrderReport(LocalDate date, BigInteger storeID) throws ParseException, IOException;
}