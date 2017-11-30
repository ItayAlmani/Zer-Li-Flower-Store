import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class mysqlConnection {

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}

		try {
			String url = "jdbc:mysql://localhost:3306/test";
			String username = "root";
			String password = "1234";
			Connection conn = DriverManager.getConnection(url, username, password);
			//System.out.println("SQL connection succeed");

			createTableFlights(conn);
			updateArrival(conn, "flight = 'KU101'");
			updateArrival(conn, "origin='Paris' and scheduled<'15:00:00'");
			updatePrepearedStatArrival(conn, "AA6622", new Time(15,15,15));
			countDelayByX(conn, 5);
			dropTable(conn, "flights1");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void updateArrival(Connection con, String wherequery) {
		Statement stmt;
		System.out.println("\nUpdateArrival:");
		try {
			stmt = con.createStatement();
			ResultSet rs1 = stmt.executeQuery("select * from flights1 where "+wherequery+";");
			printFlights(rs1, "Before");
			stmt.executeUpdate("UPDATE flights1 SET delay = concat('Landed ', cast(current_timestamp as time)) WHERE "
					+ wherequery + ";");
			ResultSet rs = stmt.executeQuery("SELECT * FROM flights1 WHERE " + wherequery + ";");
			System.out.println();
			printFlights(rs, "After");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updatePrepearedStatArrival(Connection con, String f, Time t) {
		Statement stmt;
		System.out.println("\nPreparedStatement:");
		try {
			stmt = con.createStatement();
			ResultSet rs1 = stmt.executeQuery("select * from flights1 where flight ='"+f+"';");
			printFlights(rs1, "Before");
			
			PreparedStatement updateFlights = con.prepareStatement("UPDATE flights1 SET delay = concat('Landed ', ?) WHERE flight=?;");
			updateFlights.setTime(1,t);
			updateFlights.setString(2, f);
			updateFlights.executeUpdate();
			ResultSet rs = stmt.executeQuery("SELECT * FROM flights1 WHERE flight='" + f + "';");
			printFlights(rs, "After");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public static void countDelayByX(Connection con1, Integer minutes) {
		System.out.println("\nDelay:");
		String str = "00:";
		if (minutes < 10)
			str += "0";
		str += minutes.toString() + ":00";
		Statement stmt;
		try {
			stmt = con1.createStatement();
			/*ResultSet rs1 = stmt.executeQuery("select * from flights1 where delay LIKE 'Expected%' AND addtime(scheduled,'"+ str + "') <= cast((substring(delay,9,6)) as time);");
			printFlights(rs1);*/
			ResultSet rs = stmt.executeQuery(
					"select COUNT(*) as allMoreThanX from flights1 where delay LIKE 'Expected%' AND addtime(scheduled,\'"
							+ str + "\') <= cast((substring(delay,9,6)) as time);");
			while (rs.next())
				System.out.println(
						"There are " + rs.getInt(1) + " flights that are delayed in " + minutes + " min or more");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void printFlights(ResultSet rs, String s) throws SQLException {
		while (rs.next()) {
			System.out.println(s+":\t"+rs.getTime(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
					+ rs.getString(4) + "\t" + rs.getInt(5));
		}
	}
	
	
	
	
	
	
	public static void showByQuery(Connection con, String query) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("This is the query result:");
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void dropTable(Connection con, String tabName) {
		Statement stmt;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate("DROP TABLE " + tabName);
			//System.out.println("Table " + tabName + " dropped");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTableFlights(Connection con1) {
		Statement stmt;
		try {
			stmt = con1.createStatement();
			stmt.executeUpdate(
					"CREATE TABLE flights1(scheduled TIME, flight VARCHAR(40), origin VARCHAR(40),delay VARCHAR(40), terminal INT);");
			stmt.executeUpdate("load data local infile \"arrived_flights.txt\" into table flights1");
			//System.out.println("Flights1 created");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void addTableColumn(Connection con1, String name, String type, String table) {
		Statement stmt;
		try {
			stmt = con1.createStatement();
			stmt.executeUpdate("ALTER TABLE " + table + " ADD " + name + " " + type + ";");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
}
