package controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import common.DataBase;
import common.ServerController;

public class DataBaseController {
	public DataBase db;
	private String dbUrl_default = "localhost",
			dbName_default="dbassignment2",
			dbUserName_default = "root", 
			dbPassword_default = "1234";
	
	private final String dbTxtPath="/common/DataBaseAddress.txt";
	
	
	public ArrayList<Object> getDBData(){
		ArrayList<Object> objArr = new ArrayList<>();
		objArr.add(dbUrl_default);
		objArr.add(dbName_default);
		objArr.add(dbUserName_default);
		objArr.add(dbPassword_default);
		return objArr;
	}
	
	public void setDBData(ArrayList<Object> objArr) {
		String[] dbData = new String[4];
		int i = 0;
		for (Object object : objArr) {
			if(object instanceof String == false) {
				System.err.println("One of dbData isn't String!\n");
				objArr.clear();
				objArr.add(false);
				return;
			}
			else
				dbData[i++]=(String)object;
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
		dbUrl_default=dbData[0];
		dbName_default=dbData[1];
		dbUserName_default=dbData[2];
		dbPassword_default=dbData[3];
		try {
			writeNewDBDataIntoTxt();
		} catch (IOException e) {
			System.err.println("Can't write to txt file new data\n");
		}
	}
	
	private void writeNewDBDataIntoTxt() throws IOException {
		File f = null;
		try {
			f = new File(ServerController.class.getResource(dbTxtPath).toURI());
		} catch (URISyntaxException e) {
			f.createNewFile();
			e.printStackTrace();
		}
		if (f.exists() == false) //Create a new file if doesn't exists yet
			f.createNewFile();
		PrintStream output = new PrintStream(f);
		output.flush();//flush whole txt file
		output.println("Url: "+dbUrl_default);
		output.println("Name: "+dbName_default);
		output.println("UserName: "+dbUserName_default);
		output.println("Password: "+dbPassword_default);
		output.close();
	}

	private void updateDB(String url, String name, String userName, String password) throws SQLException {
		db = new DataBase(url, name,userName,password);
	}
	
	private void updateDB(String[] args) throws SQLException {
		String url = args[0], name=args[1],userName=args[2],password=args[3];
		db = new DataBase(url, name,userName,password);
	}

	public void connectToDB() {
		int dbSuccessFlag = 0;		//will be 1 if updateDB(args) succeeded
		try {
			Scanner scnr = null;
			scnr = new Scanner(ServerController.class.getResourceAsStream(dbTxtPath));
			scnr.useDelimiter("\\w");
			String[] args = new String[4];
			for(int i = 0;i<4 && scnr.hasNextLine();i++) {
				String[] tempSplit = scnr.nextLine().split("\\W+");
				args[i]= tempSplit[tempSplit.length-1];
			}
			scnr.close();
			updateDB(args);
			dbSuccessFlag = 1;
		} catch (SQLException e) {
			System.err.println("DataBaseAddress.txt data is corrupted, or the process is.\nGo to EchoServer for the process\n");
		}
		if(dbSuccessFlag==0) {	//db data corrupted 
			try {
				updateDB(dbUrl_default, dbName_default,dbName_default,dbPassword_default);
			} catch (SQLException e) {
				db=null;
				System.err.println("Default Data Base data is wrong!\nGo to EchoServer to fix it!\n");
				System.err.println("No DataBase connection!!\n");
			}
		}
	}
}
