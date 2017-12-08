package gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

public class ConnectionConfigGUIController extends ParentGUI implements Initializable {
	
	@FXML
	private TextField txtHost,txtPort,txtName,txtUrl,txtUserName,txtPassword;
	
	@FXML
	private Button btnUpdateServer, btnUpdateDB,btnBack;
	
	@FXML
	private Label lblDBMsg,lblServerMsg;
	
	private String host,dbUrl, dbName, dbUserName, dbPassword;
	private Integer port;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientServerController csc = new ClientServerController(this);
		
		if(MainClient.cc !=null && MainClient.cc.isConnected()==true) {
			this.host=MainClient.cc.getHost();
			this.port=MainClient.cc.getPort();
			this.txtHost.setText(this.host);
			this.txtPort.setText(this.port.toString());
			try {
				csc.askDBDataFromServer();
			} catch (IOException e) {
				lblDBMsg.setText("Cant show data.\nServer disconnected");
			}
		}
		else {
			lblServerMsg.setText("Connection failed!");
			lblDBMsg.setText("Can't show data.");
		}
	}
	
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
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/fxmls/MainMenuGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	public void updateServer(ActionEvent event) throws Exception {
		if(txtHost.getText().equals("")==false)
			if(host==null||host.equals(txtHost.getText())==false)
				host=txtHost.getText();
		if(txtPort.getText().equals("")==false)
			if(port==null||port.equals(Integer.parseInt(txtPort.getText()))==false)
				port=Integer.parseInt(txtPort.getText());
		if(port!=null&&host!=null) {
			MainMenuGUIController.host=this.host;
			MainMenuGUIController.port=this.port;
			lblServerMsg.setText("Updated");
		}
		else
			lblServerMsg.setText("Enter data");
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
			ClientServerController csc = new ClientServerController(this);
			try {
				csc.askSetDBData(new DataBase(dbUrl, dbName, dbUserName, dbPassword));
			} catch (IOException e) {
				lblDBMsg.setText("Error");
				System.err.println("Error in ConnConfigGUIController, on askSetDBData() in updateDB.\n");
				e.printStackTrace();
			}
			lblDBMsg.setText("Updated");
		}
		else
			lblDBMsg.setText("Enter data");
	}
}
