package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import common.*;
import entities.CSMessage;
import entities.Customer;
import entities.Order;
import entities.Product;
import entities.CSMessage.MessageType;
import entities.User.UserType;
import gui.controllers.ParentGUIController;

public class ProductsFormGUIController extends ParentGUIController{

	private @FXML ComboBox cmbProducts;
	private @FXML Button btnBack, btnUpdate;
	private @FXML Label lblShowID, lblShowType, lblShowColor, lblShowPrice;
	private @FXML TextField txtShowName;
	private @FXML Pane paneItem;
	private @FXML ImageView imgImage;
	
	private Product p;
	private ArrayList<Product> products;
	ObservableList<String> list;

	private void getProductsComboBox() {
		try {
			Context.fac.product.getAllProducts();
		} catch (IOException e) {
			System.err.println("ProdForm");
			e.printStackTrace();
		}
	}
	
	public void setProducts(ArrayList<Product> prods) {
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
		BigInteger id = p.getPrdID();
		this.lblShowID.setText(id.toString());
		this.lblShowType.setText(p.getType().toString());
		this.txtShowName.setText(p.getName());
		this.lblShowColor.setText(p.getColor().toString());
		this.lblShowPrice.setText(((Float)p.getPrice()).toString() + "¤");
		this.imgImage.setImage(p.getImage());
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
		getProductsComboBox();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		getProductsComboBox();
		cmbProducts.setStyle("-fx-font-size:10");
	}
}
