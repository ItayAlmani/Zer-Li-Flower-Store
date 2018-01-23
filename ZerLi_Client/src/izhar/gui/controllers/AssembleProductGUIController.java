package izhar.gui.controllers;

import java.util.ArrayList;

import org.controlsfx.control.RangeSlider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import entities.Stock;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;

public class AssembleProductGUIController extends ProductsPresentationGUIController implements Initializable{

	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML RangeSlider rsPrice;
	private @FXML JFXTextField txtMinPrice, txtMaxPrice;
	private @FXML MaterialDesignIconView icnFlower;
	private @FXML JFXButton btnSend;
	private HBox hxProds;
	private ArrayList<Stock> stocks = new ArrayList<>(), stocksByType;
	
	/**
	 * 
	 */
	public void createForm() {		
		cbType.valueProperty().addListener((obs,oldVal,newVal)->{
			cbColor.setValue(null);
			cbColor.getItems().clear();
			stocksByType=new ArrayList<>();
			if(vbox.getChildren().contains(hxProds))
				vbox.getChildren().remove(hxProds);
			Float min = stocks.get(0).getPriceAfterSale(), max=0f;
			ArrayList<Color> cal = new ArrayList<>();
			for (Stock s : stocks) {
				if(s.getProduct().getType().equals(newVal)&& s.getProduct().getColor()!=null &&
						s.getProduct().getColor().equals(Color.Colorfull)==false &&
						cal.contains(s.getProduct().getColor())==false) {
					cal.add(s.getProduct().getColor());
					if(s.getPriceAfterSale()<min)
						min=s.getPriceAfterSale();
					if(s.getPriceAfterSale()>max)
						max=s.getPriceAfterSale();
					stocksByType.add(s);
				}
			}
			min=min!=0f?min:0f;
			max++;
			rsPrice.setMin(min.intValue());
			rsPrice.setMax(max.intValue());
			rsPrice.setHighValue(max.intValue());
			rsPrice.setLowValue(min.intValue());
			Integer ticks = ((Integer)((Float)((max-min)/10f)).intValue());
			ticks=ticks>0?ticks:1;
			rsPrice.setMajorTickUnit(++ticks);
			rsPrice.setMinorTickCount(ticks);
			cbColor.setItems(FXCollections.observableArrayList(cal));
		});
		ArrayList<ProductType> ptal = new ArrayList<>();
		for (Stock s : stocksByType) {
			if(ptal.contains(s.getProduct().getType())==false)
				ptal.add(s.getProduct().getType());
		}
		
		if(Platform.isFxApplicationThread()) {
			cbType.setItems(FXCollections.observableArrayList(ptal));
			btnSend.setDisable(false);
		}
		else
			Platform.runLater(()->{
				cbType.setItems(FXCollections.observableArrayList(ptal));
				btnSend.setDisable(false);
			});
	}
	
	/**
	 * The function when the button {@link #btnSend} clicked.
	 */
	public void assembleProduct() {
		ProductType type = cbType.getValue();
		Color color = cbColor.getValue();
		Float min = Float.parseFloat(txtMinPrice.getText()), 
				max =Float.parseFloat(txtMaxPrice.getText());
		stocksByType=Context.fac.product.assembleProduct(type, min, max, color, this.stocks);
		if(stocksByType.isEmpty()==false) {
			try {
				initPage();
			} catch (Exception e) {
				Context.mainScene.ShowErrorMsg();
				e.printStackTrace();
			}
		}
		else {
			if(vbox.getChildren().contains(hxProds))
				vbox.getChildren().remove(hxProds);
			initListeners();
			Context.mainScene.setMessage("Can't assemble right now");
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
			if(vbox.getChildren().contains(hxProds))
				vbox.getChildren().remove(hxProds);
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
		txtMinPrice.textProperty().addListener(e->Context.mainScene.clearMsg());
		txtMaxPrice.textProperty().addListener(e->Context.mainScene.clearMsg());
		txtMaxPrice.textProperty().addListener((obs, oldval, newVal)->{
			try {
				rsPrice.setHighValue(Integer.parseInt(newVal));
			} catch (NumberFormatException e) {
				Context.mainScene.setMessage("Must enter only integers");
			}
		});
	}
	
	private void initPage() throws Exception {
		components.clear();    	
    	pagination  = new Pagination(stocksByType.size(), 0);
    	initArrays(stocksByType.size());
    	int i = 0;
		for (Stock stk : stocksByType) {
			Float newPrice = Context.fac.product.getPriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
			setVBox(i, 
					stk,
					newPrice==null?null:newPrice*(1-stk.getSalePercetage()),
						addToCart(stk.getProduct(),
							newPrice==null ? 
								stk.getPriceAfterSale() :
								newPrice*(1-stk.getSalePercetage()), stk));
			i++;
		}
		if(Platform.isFxApplicationThread())
			resetView();
		else
			Platform.runLater(()->resetView());
	}
	
	private void resetView() {
		if(vbox.getChildren().contains(hxProds)==false)
			vbox.getChildren().remove(hxProds);
		hxProds = new HBox(5,pagination);
		hxProds.setAlignment(Pos.TOP_CENTER);
		vbox.getChildren().add(vbox.getChildren().size(),hxProds);
		vbox.setAlignment(Pos.TOP_CENTER);
	}

	@Override
	protected void getProducts() {
		btnSend.setDisable(true);
		if(Context.order!=null && 
    			Context.order.getDelivery()!=null && 
    			Context.order.getDelivery().getStore()!=null) {
			stocksByType=this.stocks=Context.fac.stock.getNotInCatalogOnlyStock(Context.order.getDelivery().getStore().getStock());
    		createForm();
    	}
    	else
    		Context.mainScene.setMessage("Can't show products right now!");
		initListeners();
	}

	@Override
	protected void getProducts(ArrayList<Product> prds) {
		// TODO Auto-generated method stub
		
	}
}