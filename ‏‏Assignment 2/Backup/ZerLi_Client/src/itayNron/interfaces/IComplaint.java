package itayNron.interfaces;

import java.util.ArrayList;

import entities.Complaint;
import interfaces.IParent;

public interface IComplaint extends IParent {

	/**
	 * 
	 * @param complaint
	 */
	void addComplaint(Complaint complaint);

	/**
	 * 
	 * @param complaint
	 */
	void updateComplaint(Complaint complaint);

	/**
	 * 
	 * @param storeid
	 */
	void getComplaintsByStore(int storeid);

	void sendComplaints(ArrayList<Complaint> complaints);

	void getNotTreatedComplaints();

	void getAllComplaints();

}