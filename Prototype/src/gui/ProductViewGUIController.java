package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import Entity.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

public class ProductViewGUIController implements Initializable{
	
	private Product p;
	
	@FXML
	private Label lblShowID;
	
	@FXML
	private Label lblShowType;
	
	@FXML
	private TextField txtShowName;
	
	@FXML
	private Button btnUpdate;
	
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
		
	}
}
