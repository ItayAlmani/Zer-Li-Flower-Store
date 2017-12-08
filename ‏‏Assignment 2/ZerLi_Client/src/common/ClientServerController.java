package common;

import java.io.IOException;
import java.util.ArrayList;

import entities.*;
import gui.controllers.*;

/**
 * The connector between the GUI to the <code>ClientConsole</code>
 */
public class ClientServerController {
	private Object gui;
	private ArrayList<Object> myMsgArr;

	public ClientServerController(Object gui) {
		super();
		this.gui = gui;
		myMsgArr = new ArrayList<>();
	}

	public void askProductsFromServer() throws IOException {
		myMsgArr.clear();
		myMsgArr.add("SELECT * FROM product;");
		MainClient.cc.handleMessageFromClientUI(new CSMessage(MessageType.SELECT,myMsgArr), this);
	}
	
	public void askUpdateProductFromServer(Product p) throws IOException {
		myMsgArr.clear();
		myMsgArr.add(String.format(
				"UPDATE product SET productID = '%d',productName='%s',productType='%s'"
				+ "WHERE productID=%d;",p.getId(),p.getName(),p.getType(),p.getId()));
		MainClient.cc.handleMessageFromClientUI(new CSMessage(MessageType.UPDATE,myMsgArr), this);
	}
	
	public void askDBDataFromServer() throws IOException {
		MainClient.cc.handleMessageFromClientUI(new CSMessage(MessageType.DBData,null), this);
	}
	
	public void askSetDBData(DataBase db) throws IOException {
		myMsgArr.add(db.getDbUrl());
		myMsgArr.add(db.getDbName());
		myMsgArr.add(db.getDbUserName());
		myMsgArr.add(db.getDbPassword());
		MainClient.cc.handleMessageFromClientUI(new CSMessage(MessageType.SetDB,myMsgArr), this);
	}
	
	public void sendDBDataToClient(ArrayList<String> dbData) {
		if(gui instanceof ConnectionConfigGUIController) {
			((ConnectionConfigGUIController)gui).setDBDataInGUI(dbData);
		}
	}
	
	public void sendProductsToClient(ArrayList<Product> prds) {
		((ProductsFormGUIController)gui).updateCB(prds);
	}

	public void sendResultToClient(boolean response) {
		if(response==true)	((ParentGUI)gui).ShowSuccessMsg();
		else				((ParentGUI)gui).ShowErrorMsg();

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
	
	public Object getGui() {
		return gui;
	}

	public void setGui(Object gui) {
		this.gui = gui;
	}

}
