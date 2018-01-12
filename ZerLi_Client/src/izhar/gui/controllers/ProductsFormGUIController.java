package izhar.gui.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
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
import javafx.scene.image.Image;
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

public class ProductsFormGUIController implements Initializable{

	private @FXML ComboBox<Product> cmbProducts;
	private @FXML Button btnBack, btnUpdate;
	private @FXML Label lblShowID, lblShowType, lblShowColor, lblShowPrice;
	private @FXML TextField txtShowName;
	private @FXML Pane paneItem;
	private @FXML ImageView imgImage;
	
	private Product p;
	private ArrayList<Product> products;

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
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cmbProducts.setItems(FXCollections.observableArrayList(products));
				paneItem.setVisible(false);
			}
		});
	}
	
	public void showProduct() throws Exception {
		if(cmbProducts.getValue()!=null)
			loadProduct(cmbProducts.getValue());
	}
	
	public void loadProduct(Product p) {
		try {
			this.p=p;
			BigInteger id = p.getPrdID();
			this.lblShowID.setText(id.toString());
			this.lblShowType.setText(p.getType().toString());
			this.txtShowName.setText(p.getName());
			this.lblShowColor.setText(p.getColor().toString());
			this.lblShowPrice.setText(((Float)p.getPrice()).toString() + "¤");
			InputStream is = getClass().getResourceAsStream("/images/"+p.getImageName());
			imgImage = new ImageView(new Image(is));
			is.close();
			paneItem.setVisible(true);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public void updateName() throws Exception {
		if(txtShowName.getText()!=null) {
			if(txtShowName.getText().equals(p.getName())==false) {//Name changed
				p.setName(txtShowName.getText());
				Context.fac.product.update(p);
				getProductsComboBox();
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
		getProductsComboBox();
		cmbProducts.setStyle("-fx-font-size:10");
	}

	public void loadMainMenu() {
		Context.mainScene.loadMainMenu();
	}
}
