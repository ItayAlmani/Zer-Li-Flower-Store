package kfir.interfaces;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import entities.User;
import interfaces.IParent;

public interface IUser{
	
	void update(User user);
	
	void getAllUsers() throws IOException;
	
	void getUser(User user) throws IOException;
	
	void getUser(BigInteger uID) throws IOException;
}