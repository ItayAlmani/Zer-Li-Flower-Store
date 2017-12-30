package lior;

import java.util.ArrayList;
import java.util.Date;

import controllers.ParentController;
import entities.CustomerComplaintsReport;
import lior.interfaces.IHistogramOfCustomerComplaintsReportController;

public class HistogramOfCustomerComplaintsReportController extends ParentController implements IHistogramOfCustomerComplaintsReportController {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerComplaintsReport ProduceHistogramOfCustomerComplaintsReport(Date Reqdate, int storeID) {
		/*
		return null;
	}


}