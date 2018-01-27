package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.ResultSetMetaData;

public class DataBaseHandler{
	public Connection con;

	public DataBaseHandler(String dbUrl, String dbName, String dbUserName, String dbPassword) throws SQLException {
		this.con = connectToDB(dbUrl, dbName, dbUserName, dbPassword);
	}
/**
 * 	<p>
 * Function to get the answers of the query we asked from DB
 * </p>
 * @param the query we want to execute 
 * @return ArrayList<Object> with the attributes of the objects we got
 * @throws SQLException
 */
	public ArrayList<Object> getQuery(String query) throws SQLException {
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
			throw e;
		}
	}
	
	/**
	 * <p>
	 * Function to fire when we want to update raws in DB
	 * </p>
	 * @param query - the query we want to update
	 * @throws SQLException
	 */
	public void updateQuery(String query) throws SQLException {
		Statement stmt;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
	}
	
	/**
	 * <p>
	 * Function to fire arrayList of queries. <br>
	 * we call this function if we want that few queries will execute,<br>
	 * if one query will failed all the batch will failed
	 * </p>
	 * @param queries - arrayList of queries to execute
	 * @throws SQLException
	 */
	public void insertWithBatch(ArrayList<String> queries) throws SQLException {
		Statement stmt = null;
		try{
		      stmt = con.createStatement();
		      con.setAutoCommit(false);
		      for (String s : queries) {
		    	  stmt.addBatch(s);
		      }
		      stmt.executeBatch();
		      con.commit();
		      con.setAutoCommit(true);
/*		      if(stmt!=null)
		    	  stmt.close();*/
		}catch(Exception e){
		      e.printStackTrace();
		      con.rollback();
		      con.setAutoCommit(true);
/*		      if(stmt!=null)
		    	  stmt.close();*/
		      throw e;
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
