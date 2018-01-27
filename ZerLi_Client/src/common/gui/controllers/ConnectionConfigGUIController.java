package common.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.ClientController;
import common.Context;
import common.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnectionConfigGUIController implements Initializable{
	
	private @FXML TextField txtHost,txtPort,txtName,txtUrl,txtUserName,txtPassword;
	private @FXML Button btnUpdateServer, btnUpdateDB,btnBack;
	
	private String host,dbUrl, dbName, dbUserName, dbPassword;
	private Integer port;
	
	public void setDBData(ArrayList<String> dbData) {
		if(dbData!=null) {
			this.dbUrl=dbData.get(0);
			this.dbName=dbData.get(1);
			this.dbUserName=dbData.get(2);
			this.dbPassword=dbData.get(3);
			this.txtUrl.setText(this.dbUrl);
			this.txtName.setText(this.dbName);
			this.txtUserName.setText(this.dbUserName);
			this.txtPassword.setText(this.dbPassword);
		}
	}
	
	public void updateServer() {
		Context.mainScene.clearMsg();
		if(txtHost.getText().equals("")==false)
			host=txtHost.getText();
		if(txtPort.getText().equals("")==false)
			port=Integer.parseInt(txtPort.getText());
		if(port!=null&&host!=null) {
			try {
				ClientController.connectToServer(host, port);
				Context.mainScene.setMessage("Server connected successfully");
				Context.fac.dataBase.getDBStatus();
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
			}
		}
		else
			Context.mainScene.ShowErrorMsg();
	}
	
	public void updateDB(ActionEvent event) {
		Context.mainScene.clearMsg();
		if(txtUrl.getText().equals("")==false)		//if the field is empty
			dbUrl=txtUrl.getText();
		
		if(txtName.getText().equals("")==false)
			dbName=txtName.getText();
		
		if(txtUserName.getText().equals("")==false)
			dbUserName=txtUserName.getText();
		
		if(txtPassword.getText().equals("")==false)
			dbPassword=txtPassword.getText();
		
		if(dbUrl!=null && dbName!=null && dbUserName!=null&&dbPassword!=null) {
			try {
				Context.fac.dataBase.setDB(new DataBase(dbUrl, dbName, dbUserName, dbPassword));
				Context.fac.dataBase.getDBStatus();
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				System.err.println("Error in ConnConfigGUIController, on askSetDBData() in updateDB.\n");
				e.printStackTrace();
			}
		}
		else
			Context.mainScene.ShowErrorMsg();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
	
		if(Context.clientConsole !=null && Context.clientConsole.isConnected()==true) {
			this.host=Context.clientConsole.getHost();
			this.port=Context.clientConsole.getPort();
			this.txtHost.setText(this.host);
			this.txtPort.setText(this.port.toString());
			try {
				Context.fac.dataBase.getDB();
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
			}
		}
		else {
			txtHost.setText("localhost");
			txtPort.setText("5555");
		}
	}
	
	public void setDBStatus(Boolean dbStatus) {
		if(dbStatus) {
			if(Context.clientConsole!=null && Context.clientConsole.isConnected()) {
				if(Context.getUser()!=null) {
					Context.mainScene.setServerAvailable("Server connected successfully");
					Context.mainScene.loadMainMenu("Server connected successfully");
				}					
				else
					Context.mainScene.loadGUI("LogInGUI", false);
			}
			else
				Context.mainScene.setMessage("Can't connect!");
		}
		else
			Context.mainScene.setMessage("Data base details are incorrect!");
	}
}