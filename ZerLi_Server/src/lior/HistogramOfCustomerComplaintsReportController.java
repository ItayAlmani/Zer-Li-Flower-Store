package lior;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import common.EchoServer;
import entities.Complaint;
import entities.HistogramOfCustomerComplaintsReport;
import entities.Store;
import lior.interfaces.IHistogramOfCustomerCompaintsReportController;

public class HistogramOfCustomerComplaintsReportController implements IHistogramOfCustomerCompaintsReportController {
	private HistogramOfCustomerComplaintsReport ccReport;
	private LocalDate rDate, startDate;

	/* (non-Javadoc)
	 * @see lior.IHistogramOfCustomerCompaintsReportController
	 */
	@Override
	public ArrayList<Object> produceHistogramOfCustomerComplaintsReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof Store == false)
			return null;
		LocalDate date = (LocalDate)arr.get(0);
		Store store = (Store)arr.get(1);
		ccReport=new HistogramOfCustomerComplaintsReport();
		this.ccReport.setStoreID(store.getStoreID());
		this.ccReport.setNotTreatedCnt(0);
		this.ccReport.setRefundedCnt(0);
		this.ccReport.setTreatedCnt(0);
		rDate=date;
		Calendar c = Calendar.getInstance(); 
		c.setTime(Date.from(rDate.atStartOfDay(ZoneId.systemDefault()).toInstant())); 
		c.add(Calendar.MONTH, -3);
		startDate = c.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.ccReport.setStartdate(this.startDate);
		this.ccReport.setEnddate(this.rDate);
		return analyzeComplaints(EchoServer.fac.complaint.getComplaintsByStore(store));
	}
	/* (non-Javadoc)
	 * @see lior.IHistogramOfCustomerCompaintsReportController
	 */
	public ArrayList<Object> analyzeComplaints(ArrayList<Object> objs) throws Exception{
		ArrayList<Complaint> complaints= new ArrayList<>();
		if(objs == null || objs.isEmpty())
			throw new Exception();
		for (Object c : objs) {
			if(c instanceof Complaint)
				complaints.add((Complaint)c);
		}
		int flag=0;
		this.ccReport.setComplaints(complaints);
		for(int i=0;i<complaints.size();i++)
		{
			Date date = Date.from(complaints.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(Date.from(this.ccReport.getEnddate().atStartOfDay(ZoneId.systemDefault()).toInstant()))==false&&
					date.after(Date.from(this.ccReport.getStartdate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
					)
			{
				flag=1;
				if(complaints.get(i).isTreated())
				{
					this.ccReport.setTreatedCnt(this.ccReport.getTreatedCnt()+1);
					if(complaints.get(i).isRefunded())
						this.ccReport.setRefundedCnt(this.ccReport.getRefundedCnt()+1);
				}
				else
					this.ccReport.setNotTreatedCnt(this.ccReport.getNotTreatedCnt()+1);
			}	
		}
		if(flag==1) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.ccReport);
			return ar;
		}
		else {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.ccReport);
			return ar;
		}
	}
}