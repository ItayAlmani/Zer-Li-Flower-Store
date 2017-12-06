package old;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Entity.Product;

public class Old {
	/*DataBase*/
	/*public ArrayList<Product> getAllProducts(){
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
	}*/
}
