package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSetMetaData;

import controllers.ServerController;

public class DataBase{
	public Connection con;

	public DataBase(String dbUrl, String dbName, String dbUserName, String dbPassword) throws SQLException {
		this.con = connectToDB(dbUrl, dbName, dbUserName, dbPassword);
	}
	
	public ArrayList<Object> getQuery(String query) throws Exception {
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ArrayList<Object> objectArr = new ArrayList<>();
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			while(rs.next()) {
				for(int i=1;i<=rsmd.getColumnCount();i++)
					objectArr.add(rs.getObject(i));
			}
			return objectArr;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean setQuery(String query) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Connection connectToDB(String dbUrl, String dbName, String dbUserName, String dbPassword) throws SQLException {
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		conn = DriverManager.getConnection("jdbc:mysql://"+dbUrl+"/" + dbName +"?autoReconnect=true&useSSL=false"
				, dbUserName, dbPassword);
		return conn;
	}

}
