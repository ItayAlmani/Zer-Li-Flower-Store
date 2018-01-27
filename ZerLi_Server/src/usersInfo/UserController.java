package usersInfo;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import common.EchoServer;
import common.ParentController;
import usersInfo.entities.User;
import usersInfo.entities.User.UserType;
import usersInfo.interfaces.IUser;

public class UserController extends ParentController implements IUser {

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj == null) return new ArrayList<>();
		ArrayList<Object> users = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 9)
			users.add(parse(
					BigInteger.valueOf((Integer) obj.get(i)), 
					(String) obj.get(i + 1), 
					(String) obj.get(i + 2),
					(String) obj.get(i + 3),
					(String) obj.get(i + 4),
					(String)obj.get(i + 5),
					(String)obj.get(i+6),
					(Boolean)obj.get(i+7),
					(Boolean)obj.get(i+8))
					);
		return users;
	}

	public User parse(BigInteger userID, String privateID, String firstName,
			String lastName, String userName, String password, 
			String permissions, boolean isConnected, boolean isActive) {
		return new User(userID, privateID, 
				firstName, lastName, userName, password,
				UserType.valueOf(permissions), isConnected, isActive);
	}

	public ArrayList<Object> getAllUsers() throws SQLException {
		String query = "SELECT * FROM user";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getUserForLogIn(User user) throws SQLException {
		String query = "SELECT *" + 
				" FROM user" + 
				" WHERE userName='"+user.getUserName()+"'"
				+ " AND password='"+user.getPassword()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getUser(BigInteger uID) throws SQLException {
		String query = "SELECT *" + 
				" FROM user" + 
				" WHERE userID='"+uID.toString()+"'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	/**Dummy function*/
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof User) {
			User user = (User)obj;
			String query=String.format(
					"UPDATE user SET"
					+ " firstName='%s',"
					+ " lastName='%s',"
					+ " userName='%s',"
					+ " password='%s',"
					+ " permissions='%s',"
					+ " isConnected=%b,"
					+ " isActive=%b"
					+ " WHERE userID='%d'",
					user.getFirstName(),user.getLastName(),user.getUserName(),user.getPassword(),
					user.getPermissions().name(),user.isConnected(),user.isActive(),
					user.getUserID().intValue(),user.getPrivateID());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		throw new Exception();
	}
}