package lior.interfaces;

import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import entities.IncomesReport;
import entities.ProductInOrder;
import interfaces.IParent;

public interface IIncomesReportController extends IParent {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 * @throws ParseException 
	 */
	void ProduceIncomesReport(Date Reqdate, BigInteger storeID) throws ParseException;
	void sendIncomeReports(ArrayList<IncomesReport> iReports);
	void setPIOs(ArrayList<ProductInOrder> products);
	void ProduceIncomesReport(LocalDate date, BigInteger storeID) throws ParseException;
}