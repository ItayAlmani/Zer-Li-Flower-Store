package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Entity.Product;
import Entity.Student;
import common.ProductType;

public class DataBase {

	private Connection con;

	public DataBase(String dbName, String dbUserName, String dbPassword) {
		this.con = connectToDB(dbName, dbUserName, dbPassword);
	}

	private Connection connectToDB(String dbName, String dbUserName, String dbPassword) {
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName, dbUserName, dbPassword);
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}

	public ArrayList<Product> getAllProducts(){
		Statement stmt;
		try {
			ArrayList<Product> prds = new ArrayList<>();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM product");
			int i = 1;
			while (rs.next()) {
				prds.add(parsingTheData(rs.getInt(i),rs.getString(i+1),rs.getString(i+2)));
				i+=3;
			}
			return prds;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void updateProductToDB(Product p) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = String.format("UPDATE students SET productname=%s WHERE productid=%d", p.getId(),p.getName()); 
			stmt.execute(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private Product parsingTheData(int id, String name, String type) {
		Product p = new Product(id, name);
		switch (type.toLowerCase()) {
		case "bouqute":
			p.setType(ProductType.Bouqute);
			break;
		default:
			p.setType(ProductType.Empty);
			break;
		}
		return p;
	}
}
