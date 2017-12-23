package gui.controllers;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.*;
import entities.Product;
import itayNron.ProductController;

public class ProductsFormGUIController extends ParentGUIController{

	private @FXML ComboBox cmbProducts;
	private @FXML Button btnBack, btnUpdate;
	private @FXML Label lblShowID, lblShowType;
	private @FXML TextField txtShowName;
	private @FXML Pane paneItem;
	
	private Product p;
	private ArrayList<Product> products;
	ObservableList<String> list;

	private void setProductsComboBox() {
		try {
			Context.fac.product.getProduct();
		} catch (IOException e) {
			System.err.println("ProdForm");
			e.printStackTrace();
		}
	}
	
	public void productsToComboBox(ArrayList<Product> prods) {
		this.products=prods;
		ArrayList<String> al = new ArrayList<>();
		for (Product p : this.products) {
			al.add(p.getName());
		}

		list = FXCollections.observableArrayList(al);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cmbProducts.setItems(list);
				paneItem.setVisible(false);
			}
		});
	}
	
	public void showProduct(ActionEvent event) throws Exception {
		if(Context.clientConsole.isConnected()==false) {
			serverDown(event);
			return;
		}
		Product prd = null;
		if(cmbProducts.getValue()!=null) {
			for (Product p : this.products) {
				if(p.getName().equals(cmbProducts.getValue())) {
					prd = p;
					break;
				}
			}
			if(prd!=null)
				loadProduct(prd);
		}
	}
	
	public void loadProduct(Product p) {
		this.p=p;
		Integer id = p.getPrdId();
		this.lblShowID.setText(id.toString());
		this.lblShowType.setText(p.getType().toString());
		this.txtShowName.setText(p.getName());
		paneItem.setVisible(true);
	}
	
	public void updateName(ActionEvent event) throws Exception {
		if(Context.clientConsole.isConnected()==false)
			serverDown(event);
		if(txtShowName.getText()!=null) {
			if(txtShowName.getText().equals(p.getName())==false) {//Name changed
				p.setName(txtShowName.getText());
				Context.fac.product.updateProduct(p);
			}
		}
	}
	
	@Override
	public void ShowSuccessMsg() {
		super.ShowSuccessMsg();
		setProductsComboBox();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		setProductsComboBox();
		cmbProducts.setStyle("-fx-font-size:10");
	}
}
