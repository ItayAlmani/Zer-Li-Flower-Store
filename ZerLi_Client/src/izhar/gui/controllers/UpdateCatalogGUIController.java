package izhar.gui.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class UpdateCatalogGUIController implements Initializable{

	private @FXML JFXComboBox<Product> cbProducts;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXButton btnUpdate;
	private @FXML JFXTextField txtID, txtName, txtPrice;
	private @FXML VBox paneItem, paneScene;
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
				cbProducts.setItems(FXCollections.observableArrayList(products));
				paneItem.setVisible(false);
			}
		});
	}
	
	public void showProduct() throws Exception {
		if(cbProducts.getValue()!=null)
			loadProduct(cbProducts.getValue());
	}
	
	public void loadProduct(Product p) {
		this.p=p;
		BigInteger id = p.getPrdID();
		this.txtID.setText(id.toString());
		this.txtName.setText(p.getName());
		this.cbType.setValue(p.getType());
		this.cbColor.setValue(p.getColor());
		this.txtPrice.setText(p.getPriceAsString());
		ByteArrayInputStream bais = new ByteArrayInputStream(p.getMybytearray());
		imgImage.setImage(new Image(bais));
		imgImage.prefHeight(200);
		try {
			bais.close();
			for (Node n : paneItem.getChildren()) {
				if(n instanceof Control) {
					((Control)n).setPrefWidth(paneItem.getWidth());
				}
			}
			paneItem.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateName() throws Exception {
		if(txtName.getText()!=null) {
			if(txtName.getText().equals(p.getName())==false) {//Name changed
				p.setName(txtName.getText());
				Context.fac.product.update(p);
				getProductsComboBox();
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initComboBoxes();
	}
	
	private void initComboBoxes() {
		cbType.setItems(FXCollections.observableArrayList(ProductType.values()));
		cbColor.setItems(FXCollections.observableArrayList(Color.values()));
		setComboBoxToCenter(cbType);
		setComboBoxToCenter(cbColor);
		setComboBoxToCenter(cbProducts);
		getProductsComboBox();
	}
	
	private <T> void setComboBoxToCenter(JFXComboBox<T> cb) {
		cb.setButtonCell(new ListCell<T>() {
		    @Override
		    public void updateItem(T item, boolean empty) {
		        super.updateItem(item, empty);
		        if (item != null) {
		            setText(item.toString());
		            setAlignment(Pos.CENTER);
		            Insets old = getPadding();
		            setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
		        }
		    }
		});
		
		cb.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
		    @Override
		    public ListCell<T> call(ListView<T> list) {
		        return new ListCell<T>() {
		            @Override
		            public void updateItem(T item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item != null) {
		                    setText(item.toString());
		                    setAlignment(Pos.CENTER);
		                    setPadding(new Insets(3, 3, 3, 0));
		                }
		            }
		        };
		    }
		});
	}
}
