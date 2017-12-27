package lior.interfaces;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import entities.OrderReport;
import interfaces.IParent;

public interface IOrderReportController extends IParent  {

	/**
	 * 
	 * @param reqDate
	 * @param storeID
	 * @throws ParseException 
	 */
	void ProduceOrderReport(Date reqDate, int storeID) throws ParseException;
}