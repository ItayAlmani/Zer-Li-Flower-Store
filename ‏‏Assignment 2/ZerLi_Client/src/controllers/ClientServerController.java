package controllers;

import java.io.IOException;
import java.util.ArrayList;

import common.Context;
import common.MainClient;
import entities.*;
import gui.controllers.*;

/**
 * The connector between the GUI to the <code>ClientConsole</code>
 */
public class ClientServerController {
	private ArrayList<Object> myMsgArr;

	public ClientServerController(Object gui) {
		super();
		myMsgArr = new ArrayList<>();
	}

	public void askProductsFromServer() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr), this);
	}
	
	public void askUpdateProductFromServer(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productID = '%d',productName='%s',productType='%s'"
				+ "WHERE productID=%d;",p.getId(),p.getName(),p.getType(),p.getId()));
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr), this);
	}
	
	public void askDBDataFromServer() throws IOException {
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.DBData,null), this);
	}
	
	public void askSetDBData(DataBase db) throws IOException {
		myMsgArr.add(db.getDbUrl());
		myMsgArr.add(db.getDbName());
		myMsgArr.add(db.getDbUserName());
		myMsgArr.add(db.getDbPassword());
		Context.cc.handleMessageFromClientUI(new CSMessage(MessageType.SetDB,myMsgArr), this);
	}
	
	public void sendDBDataToClient(ArrayList<String> dbData) {
		if(Context.CurrentGUI instanceof ConnectionConfigGUIController) {
			((ConnectionConfigGUIController)Context.CurrentGUI).setDBDataInGUI(dbData);
		}
	}
	
	public void sendProductsToClient(ArrayList<Product> prds) {
		if(Context.CurrentGUI instanceof ProductsFormGUIController)
			((ProductsFormGUIController)Context.CurrentGUI).updateCB(prds);
	}

	public void sendResultToClient(boolean response) {
		if(response==true)	((ParentGUIController)Context.CurrentGUI).ShowSuccessMsg();
		else				((ParentGUIController)Context.CurrentGUI).ShowErrorMsg();
	}
	
	public Product parsingTheData(int id, String name, String type) {
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
