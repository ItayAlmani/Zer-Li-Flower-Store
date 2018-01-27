package common;

import java.io.Serializable;

public class DataBase implements Serializable{
	private static final long serialVersionUID = 5L;
	private String dbUrl,dbName, dbUserName, dbPassword;

	public DataBase(String dbUrl, String dbName, String dbUserName, String dbPassword) {
		this.dbUrl = dbUrl;
		this.dbName = dbName;
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
}
