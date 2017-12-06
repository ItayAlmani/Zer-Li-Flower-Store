package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import common.*;
import entities.Product;

public class ProductsFormGUIController extends ParentGUI implements Initializable{

	@FXML
	private ComboBox cmbProducts;
	
	@FXML
	private Button btnView, btnBack;
	
	
	private ArrayList<Product> products;
	
	ObservableList<String> list;

	private void setProductsComboBox() {
		ClientServerController cc = new ClientServerController(this);
		try {
			cc.askProductsFromServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCB(ArrayList<Product> prods) {
		this.products=prods;
		ArrayList<String> al = new ArrayList<>();
		for (Product p : this.products) {
			al.add(p.getName());
		}

		list = FXCollections.observableArrayList(al);
		cmbProducts.setItems(list);
	}
	
	public void showProduct(ActionEvent event) throws Exception {
		if(MainClient.cc.isConnected()==false) {
			serverDown(event);
			return;
		}
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProductViewGUI.fxml"));
		Pane root = loader.load();
		
		
		Product prd = null;
		ProductViewGUIController productViewGUIController = loader.getController();
		if(cmbProducts.getValue()!=null) {
			for (Product p : this.products) {
				if(p.getName().equals(cmbProducts.getValue())) {
					prd = p;
					break;
				}
			}
			if(prd!=null) {
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				productViewGUIController.loadProduct(prd);
				Scene scene = new Scene(root);			
				
				primaryStage.setScene(scene);		
				primaryStage.show();
			}
		}
	}
	
	public void backToMainMenu(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/MainMenuGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setProductsComboBox();
		cmbProducts.setStyle("-fx-font-size:10");
	}
}
