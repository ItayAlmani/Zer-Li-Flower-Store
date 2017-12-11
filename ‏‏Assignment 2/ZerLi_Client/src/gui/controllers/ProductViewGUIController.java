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
import controllers.ClientController;
import controllers.ProductController;
import entities.Product;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

public class ProductViewGUIController extends ParentGUIController{

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
	
	public void updateName(ActionEvent event) throws Exception {
		if(Context.cc.isConnected()==false)
			serverDown(event);
		if(txtShowName.getText()!=null) {
			if(txtShowName.getText().equals(p.getName())==false) {//Name changed
				p.setName(txtShowName.getText());
				ProductController.askUpdateProductFromServer(p);
			}
		}
	}
	
	public void backToAllProducts(ActionEvent event) throws Exception {
		loadGUI("ProductsFormGUI", false);
	}
	
	public void backToMainMenu(ActionEvent event) throws Exception {
		loadMainMenu(event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.CurrentGUI = this;
	}
}
