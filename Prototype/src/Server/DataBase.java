package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.sql.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import Entity.Product;
import Entity.ProductType;

public class DataBase {

	public Connection con;

	public DataBase(String dbUrl, String dbName, String dbUserName, String dbPassword) {
		this.con = connectToDB(dbUrl, dbName, dbUserName, dbPassword);
	}
	
	public ArrayList<Object> getQuery(String query) {
		Statement stmt;
		try {
			stmt = EchoServer.db.con.createStatement();
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

	private Connection connectToDB(String dbUrl, String dbName, String dbUserName, String dbPassword) {
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://"+dbUrl+"/" + dbName, dbUserName, dbPassword);
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}

	public boolean updateProductToDB(Product p) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = String.format("UPDATE product SET productname='%s' WHERE productid='%d'",p.getName(),p.getId()); 
			stmt.execute(query);
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

}
