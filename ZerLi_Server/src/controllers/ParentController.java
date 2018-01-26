package controllers;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class ParentController {
	protected static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	/**
	 * Converting individual data (each Object in {@code obj}) to Object.
	 * Each Controller in ZerLi_Server will implement it.
	 * @param obj is array list that contains the "cells" of the Result (By {@link ResultSet})
	 * from the Data Base
	 * @return {@link ArrayList} of the parsed data that asked to SELECT
	 * @throws Exception
	 */
	public abstract ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception;
	
	/**
	 * Will add the object to the DataBase
	 * @param arr always will contain 2 Objects:
	 * The first one (get(0)) is the Object to add to the DataBase (Depend on the Controller)
	 * The second one (get(1)) is {@link Boolean} that indicates if we want to get the new Object's id
	 * @return {@link ArrayList} that contains the answer: 
	 * if the {@link Boolean}=<code>true</code>, will contain the {@link BigInteger} id
	 * else, will contain true
	 * @throws Exception
	 */
	public abstract ArrayList<Object> add(ArrayList<Object> arr) throws Exception;
	
	/**
	 * Will update the Object by it's Primary Key in DataBase.
	 * @param obj the Object to update in DataBase.
	 * @return {@link ArrayList} that contains true when success.
	 * @throws Exception
	 */
	public abstract ArrayList<Object> update(Object obj) throws Exception;	
}
