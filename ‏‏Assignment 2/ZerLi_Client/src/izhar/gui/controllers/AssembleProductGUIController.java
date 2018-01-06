package izhar.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.RangeSlider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AssembleProductGUIController extends ProductsPresentationGUIController implements Initializable{

	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML RangeSlider rsPrice;
	private @FXML JFXTextField txtMinPrice, txtMaxPrice;
	private @FXML MaterialDesignIconView icnFlower;
	private @FXML JFXButton btnSend;
	private HBox hxProds;
	
	private ArrayList<Product> products, inConditionProds=new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI=this;
		btnSend.setDisable(true);
		getProducts();
		initListeners();
	}
	
	public void setProducts(ArrayList<Product> prds) {
		if(prds==null || prds.isEmpty()) {
			Context.mainScene.loadMainMenu("Can't assemble product right now!");
			return;
		}
		this.products=prds;
		
		cbType.valueProperty().addListener((obs,oldVal,newVal)->{
			cbColor.setValue(null);
			if(vbox.getChildren().contains(hxProds))
				vbox.getChildren().remove(hxProds);
			Float min = products.get(0).getPrice(), max=0f;
			ArrayList<Color> cal = new ArrayList<>();
			for (Product p : products) {
				if(p.getType().equals(newVal)&& p.getColor()!=null &&
						p.getColor().equals(Color.Colorfull)==false &&
						cal.contains(p.getColor())==false) {
					cal.add(p.getColor());
					if(p.getPrice()<min)
						min=p.getPrice();
					if(p.getPrice()>max)
						max=p.getPrice();
				}
			}
			min=min!=0f?min-1:0f;
			max++;
			rsPrice.setMin(min.intValue());
			rsPrice.setMax(max.intValue());
			rsPrice.setHighValue(max.intValue());
			rsPrice.setLowValue(min.intValue());
			Integer ticks = ((int)((max-min)/10f));
			rsPrice.setMajorTickUnit(++ticks);
			rsPrice.setMinorTickCount(ticks);
			cbColor.setItems(FXCollections.observableArrayList(cal));
		});
		ArrayList<ProductType> ptal = new ArrayList<>();
		for (Product p : products) {
			if(ptal.contains(p.getType())==false)
				ptal.add(p.getType());
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				cbType.setItems(FXCollections.observableArrayList(ptal));
				btnSend.setDisable(false);
			}
		});
	}
	
	public void typeSelected() {
		
	}
	
	public void back() {
		Context.mainScene.loadMainMenu();
	}
	
	public void assembleProduct() {
		ProductType type = cbType.getValue();
		Color color = cbColor.getValue();
		Float min = Float.parseFloat(txtMinPrice.getText()), 
				max =Float.parseFloat(txtMaxPrice.getText());
		inConditionProds=Context.fac.product.assembleProduct(type, min, max, color, this.products);
		if(inConditionProds.isEmpty()==false) {
			initPage();
		}
	}
	
	private void initListeners() {
		cbColor.valueProperty().addListener((obs, oldval, newVal) -> {
			javafx.scene.paint.Color color = null;
			if(newVal==null)
				color=javafx.scene.paint.Color.BLACK;
			else
				color=javafx.scene.paint.Color.valueOf(newVal.toString());
			icnFlower.setFill(color);
			cbColor.setFocusColor(color);
		});
		
		rsPrice.setHighValue(40);
		txtMaxPrice.setText(((Integer)((Double)rsPrice.getHighValue()).intValue()).toString());
		
		rsPrice.setLowValue(0);
		txtMinPrice.setText(((Integer)((Double)rsPrice.getLowValue()).intValue()).toString());
		
		rsPrice.highValueProperty().addListener((obs, oldval, newVal) -> {
			rsPrice.setHighValue(newVal.intValue());
			txtMaxPrice.setText(((Integer)newVal.intValue()).toString());
		});		
		
		rsPrice.lowValueProperty().addListener((obs, oldval, newVal) -> {
			rsPrice.setLowValue(newVal.intValue());
			txtMinPrice.setText(((Integer)newVal.intValue()).toString());
		});
		
		txtMinPrice.textProperty().addListener((obs, oldval, newVal)->{
			try {
				rsPrice.setLowValue(Integer.parseInt(newVal));
			} catch (NumberFormatException e) {
				Context.mainScene.setMessage("Must enter only integers");
			}
		});
		txtMinPrice.textProperty().addListener(e->Context.mainScene.setMessage(""));
		txtMaxPrice.textProperty().addListener(e->Context.mainScene.setMessage(""));
		txtMaxPrice.textProperty().addListener((obs, oldval, newVal)->{
			try {
				rsPrice.setHighValue(Integer.parseInt(newVal));
			} catch (NumberFormatException e) {
				Context.mainScene.setMessage("Must enter only integers");
			}
		});
	}
	
	private void initPage() {
		components.clear();
		initArrays(inConditionProds.size());
    	
    	pagination  = new Pagination(inConditionProds.size(), 0);
    	int i = 0;
		for (Product p : inConditionProds) {
			setVBox(i, p,addToCart(inConditionProds));
			i++;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {			
				if(vbox.getChildren().contains(hxProds))
					vbox.getChildren().remove(hxProds);
				hxProds = new HBox(5,pagination);
				hxProds.setAlignment(Pos.CENTER);
				vbox.getChildren().add(vbox.getChildren().size()-1,hxProds);
				vbox.setAlignment(Pos.CENTER);
				ParentGUIController.primaryStage.getScene().getWindow().sizeToScene();
			}
		});
	}

	@Override
	protected void getProducts() {
		try {
			Context.fac.product.getAllProductsNotInCatalog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}