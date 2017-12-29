package lior.interfaces;

import java.text.ParseException;
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
	IncomesReport ProduceIncomesReport(Date Reqdate, int storeID) throws ParseException;
	void sendIncomeReports(ArrayList<IncomesReport> iReports);
	void setPIOs(ArrayList<ProductInOrder> products);
}