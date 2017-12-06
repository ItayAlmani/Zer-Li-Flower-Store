package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Server.DataBase;
import Server.EchoServer;
import client.ClientConsole;
import client.ClientServerController;
import client.MainClient;
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

public class ConnectionConfigGUIController extends TemplateGUI implements Initializable {
	
	@FXML
	private TextField txtHost,txtPort,txtName,txtUrl,txtUserName,txtPassword;
	
	@FXML
	private Button btnUpdateServer, btnUpdateDB,btnBack;
	
	@FXML
	private Label lblDBMsg,lblServerMsg;
	
	private String host,dbUrl, dbName, dbUserName, dbPassword;
	private Integer port;
	private int[] updatecnt = new int[] {1,1};

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
		Pane root = FXMLLoader.load(getClass().getResource("/gui/MainMenuGUI.fxml"));
		
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
			lblServerMsg.setText("Updated " + updatecnt[0]++);
		}
		else
			lblServerMsg.setText("Enter data " + updatecnt[0]++);
	}
	
	public void updateDB(ActionEvent event) throws Exception {
		EchoServer.dbUrl=this.dbUrl=this.txtUrl.getText();
		EchoServer.dbName=this.dbName=this.txtName.getText();
		EchoServer.dbUserName=this.dbUserName=this.txtUserName.getText();
		EchoServer.dbPassword=this.dbPassword=this.txtPassword.getText();
		EchoServer.updateDB();
		this.lblDBMsg.setText("Success "+ updatecnt[1]++);
	}
}
