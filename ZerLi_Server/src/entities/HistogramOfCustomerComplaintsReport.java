package entities;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;

public class HistogramOfCustomerComplaintsReport extends QuarterlyReport {

	private static final long serialVersionUID = 7L;
	private ArrayList<Complaint> complaints;
	private int treatedCnt;
	private int notTreatedCnt;
	private int refundedCnt;
	private LocalDate Startdate;//=new LocalDate();
	private LocalDate Enddate;//=new LocalDate(qReportID, qReportID, qReportID);
	
	public HistogramOfCustomerComplaintsReport(LocalDate date,BigInteger storeid)
	{
		super(date,storeid);
	}

	public ArrayList<Complaint> getComplaints() {
		return complaints;
	}
	public void setComplaints(ArrayList<Complaint> complaints) {
		this.complaints = complaints;
	}
	public int getTreatedCnt() {
		return treatedCnt;
	}
	public void setTreatedCnt(int treatedCnt) {
		this.treatedCnt = treatedCnt;
	}
	public int getNotTreatedCnt() {
		return notTreatedCnt;
	}
	public void setNotTreatedCnt(int notTreatedCnt) {
		this.notTreatedCnt = notTreatedCnt;
	}
	public int getRefundedCnt() {
		return refundedCnt;
	}
	public void setRefundedCnt(int refundedCnt) {
		this.refundedCnt = refundedCnt;
	}
	
	public LocalDate getStartdate() {
		return Startdate;
	}

	public void setStartdate(LocalDate startdate) {
		Startdate = startdate;
	}

	public LocalDate getEnddate() {
		return Enddate;
	}

	public void setEnddate(LocalDate enddate) {
		Enddate = enddate;
	}
}