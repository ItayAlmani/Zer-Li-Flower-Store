package lior.interfaces;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import entities.OrderReport;
import interfaces.IParent;

public interface IOrderReportController extends IParent  {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 * @throws ParseException 
	 */
	OrderReport ProduceOrderReport(Date Reqdate, int storeID) throws ParseException;
}