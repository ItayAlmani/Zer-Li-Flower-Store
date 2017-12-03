package gui;

import java.awt.Label;
import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import Entity.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ProductViewGUIController implements Initializable{
	
	private Product p;
	
	@FXML
	private Label lblShowID;
	
	@FXML
	private Label lblShowType;
	
	@FXML
	private TextField txtShowName;
	
	ObservableList<String> list;
	
	
	
	public ProductViewGUIController() {
		super();
	}

	public void loadProduct(Product p) {
		this.p=p;
		this.lblShowID.setText(Integer.toString(p.getId()));
		this.lblShowType.setText(p.getType().toString());
		this.txtShowName.setText(p.getName());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
