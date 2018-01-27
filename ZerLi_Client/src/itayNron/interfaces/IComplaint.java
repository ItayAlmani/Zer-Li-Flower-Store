package itayNron.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import common.ClientConsole;
import entities.Complaint;

public interface IComplaint{
/**
 * <p>
 * The function adds complaint object to DB
 * </p>
 * @param complaint complaint object to be added to DB
 * @param getCurrentID boolean object to decide correct ID in server
 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
 */
	void add(Complaint complaint, boolean getCurrentID) throws IOException;

	/**
	 * <p> Function to update complaint object in DB
	 * </p>
	 * @param complaint complaint object to be updated in DB
	 * @throws IOException {@link ClientConsole#handleMessageFromClientUI(entities.CSMessage)} throws IOException.
	 */
	void update(Complaint complaint) throws IOException;

	/**
	 * <p>
	 * Function to get all complaint objects from a requested store
	 * </p>
	 * @param storeID Unique identifier storeID to draw all complaints from store with that specific ID
	 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
	 */
	void getComplaintsByStore(BigInteger storeID) throws IOException;
/**
 * Function to get all complaints object which didn't got treated
 * @param cswID TODO
 * @throws IOException Context.clientConsole.handleMessageFromClientUI throws IOException.
 */
	void getNotTreatedComplaints(BigInteger cswID) throws IOException;


/**
 * <p>
 * Function to handle the information from the server, back to GUI
 * </p>
 * @param obj generic object which casted according to this class type
 */
	void handleGet(ArrayList<Complaint> obj);

}