package itayNron.interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Complaint;
import interfaces.IParent;

public interface IComplaint{

	/**
	 * 
	 * @param complaint
	 */
	void add(Complaint complaint, boolean getCurrentID) throws IOException;

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

	void handleGet(ArrayList<Object> obj);

}