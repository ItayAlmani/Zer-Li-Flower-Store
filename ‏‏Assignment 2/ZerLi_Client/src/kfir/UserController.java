package kfir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import controllers.ParentController;
import entities.CSMessage;
import entities.Customer;
import entities.Order;
import entities.User;
import entities.User.UserType;
import javafx.scene.control.Tooltip;
import entities.CSMessage.MessageType;
import kfir.interfaces.IUser;

public class UserController extends ParentController implements IUser {

	@Override
	public void handleGet(ArrayList<Object> obj) {
		ArrayList<User> users = new ArrayList<>();
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
		sendUsers(users);
	}

	@Override
	public Boolean getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SetConnection(Boolean bool) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUserByUNameAndPass(User user) throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT *" + 
				" FROM user" + 
				" WHERE userName='"+user.getUserName()+"' "
						+ "AND password='"+user.getPassword()+"'");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,User.class));
	}

	@Override
	public void LogIn(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean CheckMemberPermission(User UserDet) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User parse(BigInteger userID, String privateID, String firstName,
			String lastName, String userName, String password, 
			String permissions, boolean isConnected) {
		return new User(userID, privateID, 
				firstName, lastName, userName, password,
				UserType.valueOf(permissions), isConnected);
	}

	@Override
	public void sendUsers(ArrayList<User> users) {
		String methodName = "setUsers";
		Method m = null;
		try {
			//a controller asked data, not GUI
			if(Context.askingCtrl!=null && Context.askingCtrl.size()!=0) {
				m = Context.askingCtrl.get(0).getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.askingCtrl, users);
				Context.askingCtrl.remove(0);
			}
			else {
				m = Context.currentGUI.getClass().getMethod(methodName,ArrayList.class);
				m.invoke(Context.currentGUI, users);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			System.err.println("Couldn't invoke method '"+methodName+"'");
			e1.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e2) {
			System.err.println("No method called '"+methodName+"'");
			e2.printStackTrace();
		}
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllUsers() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM user");
		Context.clientConsole.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr,User.class));
	}
}