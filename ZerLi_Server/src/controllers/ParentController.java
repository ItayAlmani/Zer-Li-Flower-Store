package controllers;

import java.util.ArrayList;

public abstract class ParentController {
	protected static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	/**
	 * 
	 * @param orders - collection of orders
	 * @return ArrayList of the parsed data that asked to SELECT 
	 * @throws Exception 
	 */
	public abstract ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	
	/**
	 * 
	 * @param arr
	 * @return true or false, or
	 * @throws Exception
	 */
	public abstract ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	
	public abstract ArrayList<Object> update(Object obj) throws Exception;

	public ArrayList<Object> getNotTreatedComplaints(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
