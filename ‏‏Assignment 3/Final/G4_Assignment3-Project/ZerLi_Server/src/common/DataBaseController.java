package common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseController {
	public DataBaseHandler db;
	private String dbUrl_default = "localhost",
			dbName_default = "dbassignment2",
			dbUserName_default = "root",
			dbPassword_default = "kfir1234";
	/**
	 * The text file with the DB info
	 */
	private final String dbTextFileName = "DataBaseAddress.txt";
	/**
	 * The path to the file with DB info
	 */
	private final String txtLocalPath = EchoServer.tempPath + dbTextFileName;

	/**
	 * Function to get DB info
	 * @return ArrayList<Object> - arrayList with the DB info 
	 */
	public ArrayList<Object> getDBData() {
		ArrayList<Object> objArr = new ArrayList<>();
		objArr.add(dbUrl_default);
		objArr.add(dbName_default);
		objArr.add(dbUserName_default);
		objArr.add(dbPassword_default);
		return objArr;
	}

	/**
	 * Function to set DB info
	 * @param objArr - arrayList of objects with DB info
	 */
	public void setDBData(ArrayList<Object> objArr) {
		String[] dbData = new String[4];
		int i = 0;
		for (Object object : objArr) {
			if (object instanceof String == false) {
				System.err.println("One of dbData isn't String!\n");
				objArr.clear();
				objArr.add(false);
				return;
			} else
				dbData[i++] = (String) object;
		}
		try {
			updateDB(dbData);
		} catch (SQLException e) {
			System.err.println("Incorrect data of message from client!\n");
			objArr.clear();
			objArr.add(false);
			return;
		}
		objArr.clear();
		objArr.add(true);
		updateDefaults(dbData);
		try {
			writeNewDBDataIntoTxt();
		} catch (IOException e) {
			System.err.println("Can't write to txt file new data\n");
		}
	}

	/**
	 * Function to change DB info in textFile
	 */
	private void writeNewDBDataIntoTxt() throws IOException {
		File fTempDir = new File(EchoServer.tempPath);
		if(fTempDir.exists() == false)
			fTempDir.mkdir();
		File fInput = new File(txtLocalPath);
		if (fInput.exists() == false) // Create a new file if doesn't exists yet
			fInput.createNewFile();
		PrintStream output = new PrintStream(fInput);
		output.flush();// flush whole txt file
		output.println("Url: " + dbUrl_default);
		output.println("Name: " + dbName_default);
		output.println("UserName: " + dbUserName_default);
		output.println("Password: " + dbPassword_default);
		output.close();
		//copyFiles(false);
	}
	/**
	 * Function to update new configurations into DB  
	 * @param dbData - array of strings with DB details
	 * @throws SQLException
	 */
	private void updateDB(String[] dbData) throws SQLException {
		String url = dbData[0], name = dbData[1], userName = dbData[2], password = dbData[3];
		db = new DataBaseHandler(url, name, userName, password);
		updateDefaults(dbData);
	}
	/**
	 * Function to update new configurations into DB  
	 * @param url - url to update in DB 
	 * @param name -name to update in DB
	 * @param userName - userName to update in DB 
	 * @param password - password to update in DB
	 * @throws SQLException
	 */
	private void updateDB(String url, String name, String userName, String password) throws SQLException {
		String[] dbData = new String[]{url,name,userName,password};
		updateDB(dbData);
	}
	/**
	 * Function to update the default configurations
	 * @param dbData - array of strings with new dbData
	 */
	private void updateDefaults(String[] dbData) {
		dbUrl_default = dbData[0];
		dbName_default = dbData[1];
		dbUserName_default = dbData[2];
		dbPassword_default = dbData[3];
	}
	/**
	 *Function to connect to DB from textFile.<br>
	 *if some data was corrupted we will execute the connection with default values 
	 */
	public void connectToDB() {
		int dbSuccessFlag = 0; // will be 1 if updateDB(args) succeeded
		try {
			File fInput = new File(txtLocalPath);
			if(fInput.exists()==false) {
				getDataFromDefault();
				return;
			}
			Scanner scnr = new Scanner(fInput);
			scnr.useDelimiter("\\w");
			String[] dbData = new String[4];
			for (int i = 0; i < 4 && scnr.hasNextLine(); i++) {
				String[] tempSplit = scnr.nextLine().split("\\W+");
				dbData[i] = tempSplit[tempSplit.length - 1];
			}
			scnr.close();
			updateDB(dbData);
			writeNewDBDataIntoTxt();
			dbSuccessFlag = 1;
			EchoServer.fac.setComplaintController();
			EchoServer.fac.qurReport.setAutoProductionTimer();
		} catch (SQLException | IOException e) {
			System.err.println("Default Data Base data is wrong!\nGo to EchoServer to fix it!\n");
		}
		if (dbSuccessFlag == 0) { // db data corrupted
			getDataFromDefault();
		}
	}
	/**
	 * Function to update DB with default configurations
	 */
	private void getDataFromDefault() {
		try {
			updateDB(dbUrl_default, dbName_default, dbName_default, dbPassword_default);
			EchoServer.fac.setComplaintController();
			EchoServer.fac.qurReport.setAutoProductionTimer();
		} catch (SQLException  e) {
			db = null;
			System.err.println(
					"DataBaseAddress.txt data is corrupted, or the process is.\nGo to EchoServer for the process\n");
			System.err.println("No DataBase connection!!\n");
		}
	}
}
