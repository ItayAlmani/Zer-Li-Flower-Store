package gui.controllers;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import common.*;
import entities.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

public class ProductViewGUIController extends ParentGUI implements Initializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Product p;
	
	@FXML
	private Label lblShowID, lblShowType;
	
	@FXML
	private TextField txtShowName;
	
	@FXML
	private Button btnUpdate, btnBack;
	
	ObservableList<String> list;
	
	public ProductViewGUIController() {
		super();
	}

	public void loadProduct(Product p) {
		this.p=p;
		Integer id = p.getId();
		this.lblShowID.setText(id.toString());
		this.lblShowType.setText(p.getType().toString());
		this.txtShowName.setText(p.getName());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void updateName(ActionEvent event) throws Exception {
		if(MainClient.cc.isConnected()==false)
			serverDown(event);
		if(txtShowName.getText()!=null) {
			if(txtShowName.getText().equals(p.getName())==false) {//Name changed
				p.setName(txtShowName.getText());
				ClientServerController csc = new ClientServerController(this);
				csc.askUpdateProductFromServer(p);
			}
		}
	}
	
	public void backToAllProducts(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/fxmls/ProductsFormGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	public void backToMainMenu(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/fxmls/MainMenuGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
}
