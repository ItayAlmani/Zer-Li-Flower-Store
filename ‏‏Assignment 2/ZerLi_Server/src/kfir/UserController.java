package kfir;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.User;
import entities.User.UserType;

public class UserController extends ParentController {

	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		ArrayList<Object> users = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 8)
			users.add(parse(
					BigInteger.valueOf((int) obj.get(i)), 
					(String) obj.get(i + 1), 
					(String) obj.get(i + 2),
					(String) obj.get(i + 3),
					(String) obj.get(i + 4),
					(String)obj.get(i + 5),
					(String)obj.get(i+6),
					((int)obj.get(i+7))!=0)
					);
		return users;
	}

	public void LogIn(User user) {
		// TODO Auto-generated method stub
		
	}

	public User parse(BigInteger userID, String privateID, String firstName,
			String lastName, String userName, String password, 
			String permissions, boolean isConnected) {
		return new User(userID, privateID, 
				firstName, lastName, userName, password,
				UserType.valueOf(permissions), isConnected);
	}

	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Object> getAllUsers() throws SQLException {
		String query = "SELECT * FROM user";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getUser(User user) throws SQLException {
		String query = "SELECT *" + 
				" FROM user" + 
				" WHERE userName='"+user.getUserName()+"' "
						+ "AND password='"+user.getPassword()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getUser(BigInteger uID) throws SQLException {
		String query = "SELECT *" + 
				" FROM user" + 
				" WHERE userID='"+uID.toString()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}