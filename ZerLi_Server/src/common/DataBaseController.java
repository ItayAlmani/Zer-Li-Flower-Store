package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataBaseController {
	public DataBaseHandler db;
	private String dbUrl_default = "localhost", dbName_default = "dbassignment2", dbUserName_default = "root",
			dbPassword_default = "1234";

	private final String dbTextFileName = "DataBaseAddress.txt";
	private final String txtBinPath = getClass().getResource("").getPath()+dbTextFileName;
	private final String txtSrcPath = System.getProperty("user.dir") + "//src//common//" + dbTextFileName;

	public ArrayList<Object> getDBData() {
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

	private void writeNewDBDataIntoTxt() throws IOException {
		File fBin = new File(txtBinPath);
		if (fBin.exists() == false) // Create a new file if doesn't exists yet
			fBin.createNewFile();
		PrintStream output = new PrintStream(fBin);
		output.flush();// flush whole txt file
		output.println("Url: " + dbUrl_default);
		output.println("Name: " + dbName_default);
		output.println("UserName: " + dbUserName_default);
		output.println("Password: " + dbPassword_default);
		output.close();
		copyFiles(false);
	}
	
	private void updateDB(String[] dbData) throws SQLException {
		String url = dbData[0], name = dbData[1], userName = dbData[2], password = dbData[3];
		db = new DataBaseHandler(url, name, userName, password);
		updateDefaults(dbData);
	}
	private void updateDB(String url, String name, String userName, String password) throws SQLException {
		String[] dbData = new String[]{url,name,userName,password};
		updateDB(dbData);
	}
	private void updateDefaults(String[] dbData) {
		dbUrl_default = dbData[0];
		dbName_default = dbData[1];
		dbUserName_default = dbData[2];
		dbPassword_default = dbData[3];
	}

	public void connectToDB() {
		int dbSuccessFlag = 0; // will be 1 if updateDB(args) succeeded
		try {
			copyFiles(true);
			InputStream is = getClass().getResourceAsStream(dbTextFileName);
			Scanner scnr = new Scanner(is);
			scnr.useDelimiter("\\w");
			String[] dbData = new String[4];
			for (int i = 0; i < 4 && scnr.hasNextLine(); i++) {
				String[] tempSplit = scnr.nextLine().split("\\W+");
				dbData[i] = tempSplit[tempSplit.length - 1];
			}
			scnr.close();
			is.close();
			updateDB(dbData);
			writeNewDBDataIntoTxt();
			dbSuccessFlag = 1;
		} catch (SQLException | IOException e) {
			System.err.println("Default Data Base data is wrong!\nGo to EchoServer to fix it!\n");
		}
		if (dbSuccessFlag == 0) { // db data corrupted
			try {
				updateDB(dbUrl_default, dbName_default, dbName_default, dbPassword_default);
			} catch (SQLException  e) {
				db = null;
				System.err.println(
						"DataBaseAddress.txt data is corrupted, or the process is.\nGo to EchoServer for the process\n");
				System.err.println("No DataBase connection!!\n");
			}
		}
	}
	
	/**
	 * 
	 * @param srcToBin - if <code>true</code>, will copy the txt file from src to bin, else from bin to src
	 */
	private void copyFiles(boolean srcToBin) {
		try {
			File fBin = new File(txtBinPath);
			File fSrc = new File(txtSrcPath);
			if (fSrc.exists() == false)
				return;
			if(fBin.exists()==false)
				fBin.createNewFile();
			FileChannel sourceChannel = null;
			FileChannel destChannel = null;
			try {
				if(srcToBin) {
					sourceChannel = new FileInputStream(fSrc).getChannel();
					destChannel = new FileOutputStream(fBin).getChannel();
				}
				else {
					sourceChannel = new FileInputStream(fBin).getChannel();
					destChannel = new FileOutputStream(fSrc).getChannel();
				}
				destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			} finally {
				sourceChannel.close();
				destChannel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
