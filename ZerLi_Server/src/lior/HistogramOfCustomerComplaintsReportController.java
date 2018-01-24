package lior;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import common.EchoServer;
import controllers.ParentController;
import entities.Complaint;
import entities.HistogramOfCustomerComplaintsReport;
import entities.Store;
import lior.interfaces.IHistogramOfCustomerCompaintsReportController;

public class HistogramOfCustomerComplaintsReportController extends ParentController implements IHistogramOfCustomerCompaintsReportController {
	private HistogramOfCustomerComplaintsReport ccReport;
	
	public ArrayList<Object> getHistogramOfCustomerComplaintsReport(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof LocalDate == false) || arr.get(1) instanceof Store == false)
			return null;
		//=====CHANGE DATE TO THE END OF THE QUARTER======
		LocalDate date = (LocalDate)arr.get(0);
		Store store=(Store)arr.get(1);
		BigInteger storeID = store.getStoreID();
		String query = String.format(
				"SELECT *"
				+ " FROM histogramreport "
				+ " WHERE storeID='%d'"
				+ " AND endOfQuarterDate='%s'",
					storeID.intValue(),
					Timestamp.valueOf(date.atStartOfDay()).toString());
		ArrayList<Object> inObjs = handleGet(EchoServer.fac.dataBase.db.getQuery(query));
		if(inObjs == null)	throw new Exception();
		if(inObjs.size()==1 && inObjs.get(0) instanceof HistogramOfCustomerComplaintsReport)
			return inObjs;
		else if(inObjs.isEmpty())
			return produceHistogramOfCustomerComplaintsReport(date, store);
		throw new Exception();
	}

	/* (non-Javadoc)
	 * @see lior.IHistogramOfCustomerCompaintsReportController
	 */
	@Override
	public ArrayList<Object> produceHistogramOfCustomerComplaintsReport(LocalDate date, Store store) throws Exception {
		ccReport=new HistogramOfCustomerComplaintsReport(date,store.getStoreID());
		this.ccReport.setStoreID(store.getStoreID());
		this.ccReport.setStartdate(date.minusMonths(3).plusDays(1));
		this.ccReport.setEnddate(date);
		this.ccReport.setStoreID(store.getStoreID());
		this.ccReport.setNotTreatedCnt(0);
		this.ccReport.setRefundedCnt(0);
		this.ccReport.setTreatedCnt(0);
		return analyzeComplaints(EchoServer.fac.complaint.getComplaintsByStore(store));
	}
	/* (non-Javadoc)
	 * @see lior.IHistogramOfCustomerCompaintsReportController
	 */
	public ArrayList<Object> analyzeComplaints(ArrayList<Object> objs) throws Exception{
		ArrayList<Complaint> complaints= new ArrayList<>();
		if(objs == null)
			throw new Exception();
		if(objs.isEmpty()) {
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.ccReport);
			add(ar);
			return ar;
		}
		for (Object c : objs) {
			if(c instanceof Complaint)
				complaints.add((Complaint)c);
		}
		this.ccReport.setComplaints(complaints);
		for(int i=0;i<complaints.size();i++)
		{
			Date date = Date.from(complaints.get(i).getDate().atZone(ZoneId.systemDefault()).toInstant());
			if(date.after(Date.from(this.ccReport.getEnddate().atStartOfDay(ZoneId.systemDefault()).toInstant()))==false&&
					date.after(Date.from(this.ccReport.getStartdate().atStartOfDay(ZoneId.systemDefault()).toInstant()))
					)
			{
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
			ArrayList<Object> ar = new ArrayList<>();
			ar.add(this.ccReport);
			add(ar);
			return ar;
}
	
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj){
		if (obj == null || obj.size()<5)
			return new ArrayList<>();
		ArrayList<Object> hrs = new ArrayList<>();
		BigInteger storeID = BigInteger.valueOf((Integer) obj.get(0));
		LocalDate endDate = ((Timestamp) obj.get(1)).toLocalDateTime().toLocalDate();
		HistogramOfCustomerComplaintsReport hr = new HistogramOfCustomerComplaintsReport(endDate,storeID);
		hr.setEnddate(endDate);
		hr.setStartdate(endDate.minusMonths(3).plusDays(1));
		for (int i = 0; i < obj.size(); i += 5) {
			parse(	hr,
					(Integer) obj.get(i + 2),
					(Integer) obj.get(i + 3),
					(Integer) obj.get(i + 4)
				);
		}
		hrs.add(hr);
		return hrs;
	}
	
	private void parse(HistogramOfCustomerComplaintsReport hr,Integer Trtcnt,Integer rfndcnt,Integer NotTrtcnt) {
		hr.setTreatedCnt(Trtcnt);
		hr.setRefundedCnt(rfndcnt);
		hr.setNotTreatedCnt(NotTrtcnt);
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr==null || arr.isEmpty() || arr.get(0) instanceof HistogramOfCustomerComplaintsReport == false )
			throw new Exception();
		HistogramOfCustomerComplaintsReport hr = (HistogramOfCustomerComplaintsReport)arr.get(0);
		ArrayList<String> queries = new ArrayList<>();
		queries.add(String.format(
				"INSERT INTO histogramreport"
				+ " (storeID, endOfQuarterDate,Treatedcnt,Refundedcnt,NotTreatedcnt)"
				+ " VALUES ('%d', '%s','%d','%d',%d);",
				hr.getStoreID(),
				Timestamp.valueOf(hr.getEndOfQuarterDate().atStartOfDay()).toString(),
				hr.getTreatedCnt(),hr.getRefundedCnt(),hr.getNotTreatedCnt()));
		EchoServer.fac.dataBase.db.insertWithBatch(queries);
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		return null;
	}

}