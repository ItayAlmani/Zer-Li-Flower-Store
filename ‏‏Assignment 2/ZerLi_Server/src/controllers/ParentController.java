package controllers;

import java.util.ArrayList;

public abstract class ParentController {
	protected static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	/**
	 * 
	 * @param orders - collection of orders
	 * @return ArrayList of the parsed data that asked to SELECT 
	 */
	public abstract ArrayList<Object> handleGet(ArrayList<Object> obj);
	
	/**
	 * 
	 * @param obj
	 * @return true or false, or
	 * @throws Exception
	 */
	public abstract ArrayList<Object> add(ArrayList<Object> obj) throws Exception;
	
	public abstract ArrayList<Object> update(Object obj) throws Exception;
}
