package lior.interfaces;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

import interfaces.IParent;

public interface IOrderReportController extends IParent  {

	/**
	 * 
	 * @param reqDate
	 * @param storeID
	 * @throws ParseException 
	 */
	void produceOrderReport(Date reqDate, BigInteger storeID) throws ParseException;

	void produceOrderReport(LocalDate date, BigInteger storeID) throws ParseException;
}