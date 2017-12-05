package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Map.Entry;

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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import CS.*;
import Controllers.Factory;
import Controllers.ProductController;
import Entity.Product;
import client.MainClient;

public class ProductsFormGUIController extends TemplateGUI implements Initializable{

	@FXML
	private ComboBox cmbProducts;
	
	@FXML
	private Button btnView, btnBack;
	
	private ArrayList<Product> products;
	
	ObservableList<String> list;

	// creating list of Students
	private void setProductsComboBox() {
		ClientController cc = new ClientController(this);
		try {
			cc.askProductsFromServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setProductsComboBox();
		cmbProducts.setStyle("-fx-font-size:10");
	}
	
	public void showProduct(ActionEvent event) throws Exception {
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
	
	public void backToAllProducts(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = FXMLLoader.load(getClass().getResource("/gui/MainMenuGUI.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
}
