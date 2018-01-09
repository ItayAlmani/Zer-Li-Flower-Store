package lior.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import entities.IncomesReport;
import entities.ProductInOrder;
import interfaces.IParent;

public interface IIncomesReportController {

	/**
	 * 
	 * @param Reqdate
	 * @param storeID
	 * @throws ParseException 
	 * @throws IOException 
	 */
	void ProduceIncomesReport(LocalDate date, BigInteger storeID) throws ParseException, IOException;
}