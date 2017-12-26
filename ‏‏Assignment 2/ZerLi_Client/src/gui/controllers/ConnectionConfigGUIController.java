package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import common.*;
import entities.DataBase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConnectionConfigGUIController extends ParentGUIController{
	
	private @FXML TextField txtHost,txtPort,txtName,txtUrl,txtUserName,txtPassword;
	private @FXML Button btnUpdateServer, btnUpdateDB,btnBack;
	
	private String host,dbUrl, dbName, dbUserName, dbPassword;
	private Integer port;
	
	public void setDBDataInGUI(ArrayList<String> dbData) {
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
	
	public void back(ActionEvent event) throws Exception {
		loadMainMenu();
	}
	
	public void updateServer(ActionEvent event) {
		if(txtHost.getText().equals("")==false)
			host=txtHost.getText();
		if(txtPort.getText().equals("")==false)
			port=Integer.parseInt(txtPort.getText());
		if(port!=null&&host!=null) {
			try {
				Context.connectToServer(host, port);
				ShowSuccessMsg();
			} catch (IOException e) {
				ShowErrorMsg();
			}
		}
		else
			ShowErrorMsg();
	}
	
	public void updateDB(ActionEvent event) {
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
				ClientController.askSetDBData(new DataBase(dbUrl, dbName, dbUserName, dbPassword));
			} catch (IOException e) {
				ShowErrorMsg();
				System.err.println("Error in ConnConfigGUIController, on askSetDBData() in updateDB.\n");
				e.printStackTrace();
			}
		}
		else
			ShowErrorMsg();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;		
	
		if(Context.clientConsole !=null && Context.clientConsole.isConnected()==true) {
			this.host=Context.clientConsole.getHost();
			this.port=Context.clientConsole.getPort();
			this.txtHost.setText(this.host);
			this.txtPort.setText(this.port.toString());
			try {
				ClientController.askDBDataFromServer();
			} catch (IOException e) {
				ShowErrorMsg();
			}
		}
		else {
			ShowErrorMsg();
		}
	}
}
