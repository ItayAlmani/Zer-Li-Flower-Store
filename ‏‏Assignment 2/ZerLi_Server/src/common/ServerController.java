package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import controllers.OrderController;
import controllers.ProductInOrderController;
import controllers.SurveyController;
import controllers.SurveyReportController;
import entities.CSMessage;
import entities.Order;
import entities.ProductInOrder;
import entities.Survey;
import entities.SurveyReport;
import entities.CSMessage.MessageType;

public class ServerController {
	public static DataBase db;
	private static String dbUrl_default = "localhost",
			dbName_default="dbassignment2",
			dbUserName_default = "root", 
			dbPassword_default = "1234";
	
	private static final String dbTxtPath="/common/DataBaseAddress.txt";
	
	
	public static CSMessage setMessageToClient(CSMessage csMsg) throws Exception{
		MessageType msgType = csMsg.getType();
		ArrayList<Object> objArr = csMsg.getObjs();
		
		if(msgType.equals(MessageType.SELECT) || msgType.equals(MessageType.UPDATE)
				|| msgType.equals(MessageType.GetAI)) {
			if(objArr.size() == 1 && objArr.get(0) instanceof String) {
				String query = (String) objArr.get(0);
				objArr.clear();
				
				/*-------SELECT queries from DB-------*/
				if(msgType.equals(MessageType.SELECT) || msgType.equals(MessageType.GetAI))
					csMsg.setObjs(db.getQuery(query));
				
				/*-------UPDATE queries from DB-------*/
				else if(msgType.equals(MessageType.UPDATE)){
					try {
						db.updateQuery(query);
						objArr.add(true);
					} catch (Exception e) {
						objArr.add(false);
					}
					csMsg.setObjs(objArr);
				}
			}
			else {
				System.err.println("Not query");
				throw new Exception();
			}
		}
		else if(msgType.equals(MessageType.INSERT)) {
			if(csMsg.getObjs() != null &&
					csMsg.getObjs().size()!=0 && csMsg.getObjs().get(0) != null) {
				if(csMsg.getClasz().equals(Order.class) && 
						csMsg.getObjs().get(0) instanceof Order) {
					Order ord = (Order)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(OrderController.addOrder(ord));
				}
				else if(csMsg.getClasz().equals(Survey.class) && 
						csMsg.getObjs().get(0) instanceof Survey) {
					Survey survey = (Survey)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(SurveyController.addSurvey(survey));	
				}
				else if(csMsg.getClasz().equals(SurveyReport.class) && 
						csMsg.getObjs().get(0) instanceof SurveyReport) {
					SurveyReport sr = (SurveyReport)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(SurveyReportController.addSurveyReport(sr));
				}
				else if(csMsg.getClasz().equals(ProductInOrder.class) &&
						csMsg.getObjs().get(0) instanceof ProductInOrder) {
					ProductInOrder pio = (ProductInOrder)(csMsg.getObjs().get(0));
					objArr.clear();
					objArr.add(ProductInOrderController.addPIO(pio));
				}
				csMsg.setObjs(objArr);
			}
		}
		else if(msgType.equals(MessageType.DBStatus)) {
			if(objArr!=null)	objArr.clear();
			else				objArr = new ArrayList<>();
			objArr.add(db!=null);
			csMsg.setObjs(objArr);
		}
		else if(msgType.equals(MessageType.DBData)) {
			if(objArr!=null)	objArr.clear();
			else				objArr = new ArrayList<>();
			objArr.add(dbUrl_default);
			objArr.add(dbName_default);
			objArr.add(dbUserName_default);
			objArr.add(dbPassword_default);
			csMsg.setObjs(objArr);
		}
		else if(msgType.equals(MessageType.SetDB)) {
			String[] dbData = new String[4];
			int i = 0;
			for (Object object : objArr) {
				if(object instanceof String == false) {
					System.err.println("One of dbData isn't String!\n");
					objArr.clear();
					objArr.add(false);
					return csMsg;
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
				return csMsg;
			}
			objArr.clear();
			objArr.add(true);
			csMsg.setObjs(objArr);
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
		return csMsg;
	}
	
	private static void writeNewDBDataIntoTxt() throws IOException {
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

	private static void updateDB(String url, String name, String userName, String password) throws SQLException {
		db = new DataBase(url, name,userName,password);
	}
	
	private static void updateDB(String[] args) throws SQLException {
		String url = args[0], name=args[1],userName=args[2],password=args[3];
		db = new DataBase(url, name,userName,password);
	}

	public static void connectToDB() {
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
		}/* catch (FileNotFoundException e) {
			db=null;
			System.err.println("Can't find txt file at "+dbTxtPath+"\n");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		if(dbSuccessFlag==0) {	//db data corrupted 
			try {
				updateDB(dbUrl_default, dbName_default,dbUserName_default,dbPassword_default);
			} catch (SQLException e) {
				db=null;
				System.err.println("Default Data Base data is wrong!\nGo to EchoServer to fix it!\n");
				System.err.println("No DataBase connection!!\n");
			}
		}
	}
}
