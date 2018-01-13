package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
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
	void update(Complaint complaint);

	/**
	 * 
	 * @param storeid
	 */
	void getComplaintsByStore(BigInteger storeid);

	void getNotTreatedComplaints() throws IOException;



	void handleGet(ArrayList<Complaint> obj);

}