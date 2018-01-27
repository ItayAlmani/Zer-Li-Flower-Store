package orderNproducts.gui.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.controlsfx.control.RangeSlider;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import common.Context;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import orderNproducts.entities.Product;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Product.Color;
import orderNproducts.entities.Product.ProductType;
import usersInfo.entities.PaymentAccount;
import usersInfo.entities.Subscription;
import javafx.scene.layout.VBox;

public class AssembleProductGUIController extends ProductsPresentationGUIController{
	
	protected class CollectionByType{
		protected ArrayList<Stock> stocks = new ArrayList<>();
		protected Integer min = null, max = null;
		protected ArrayList<Color> cal = new ArrayList<>();
	}
	
	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML RangeSlider rsPrice;
	private @FXML JFXTextField txtMinPrice, txtMaxPrice;
	private @FXML MaterialDesignIconView icnFlower;
	private @FXML JFXButton btnSend;
	private HBox hxProds = null;
	private ArrayList<Stock> stocksAfterAssemble;
	private HashMap<ProductType, CollectionByType> stockByType = new HashMap<>();
	private Subscription sub = null;
	@FXML VBox paneAss;
	
	@Override
	protected void getProducts() {
		try {
			btnSend.setDisable(true);
			if(Context.order!=null && 
	    			Context.order.getDelivery()!=null && 
	    			Context.order.getDelivery().getStore()!=null) {
					if(Context.getUserAsCustomer().getPaymentAccounts()!= null && Context.getUserAsCustomer().getPaymentAccounts().isEmpty()==false)
						stocksAfterAssemble=this.stocks=Context.fac.stock.getNotInCatalogOnlyStock(Context.order.getDelivery().getStore().getStock());
					else
						throw new Exception();
					initView();
				
	    	}
	    	else
	    		Context.mainScene.setMessage("Can't show products right now!");
			initListeners();
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
	}
	
	/** reseting the screen after changes */
	private void resetView() {
		if(vbox.getChildren().contains(hxProds))
			vbox.getChildren().remove(hxProds);
		hxProds = new HBox(5,pagination);
		hxProds.setAlignment(Pos.TOP_CENTER);
		vbox.getChildren().add(vbox.getChildren().size(),hxProds);
		vbox.setAlignment(Pos.TOP_CENTER);
		paneAss.setVisible(true);
	}
	
	/** creates {@link HashMap} by {@link ProductType} of {@link CollectionByType}
	 * and set the {@link #cbType} and {@link #cbColor} */
	public void initView() {	
		cbType.valueProperty().addListener((obs,oldVal,newVal)->{
			CollectionByType cbt = stockByType.get(newVal);
			cbColor.setValue(null);
			cbColor.getItems().clear();
			if(vbox.getChildren().contains(hxProds))
				vbox.getChildren().remove(hxProds);
			rsPrice.setMin(cbt.min);
			rsPrice.setMax(cbt.max);
			rsPrice.setHighValue(cbt.max);
			rsPrice.setLowValue(cbt.min);
			rsPrice.setMajorTickUnit(4);
			rsPrice.setMinorTickCount(3);
			cbColor.setItems(FXCollections.observableArrayList(cbt.cal));
		});
		
		for (Stock s : stocks) {
			//The key for all the assemble
			ProductType type = s.getProduct().getType();
			
			if(s.getProduct().getColor()!=null &&
					s.getProduct().getColor().equals(Color.Colorfull)==false) {
				
				//if it's the first Product with this type, add it to the Map
				if(stockByType.containsKey(type)==false)
					stockByType.put(type, new CollectionByType());
				
				CollectionByType cbt = stockByType.get(type);
				if(cbt.cal.contains(s.getProduct().getColor())==false)
					cbt.cal.add(s.getProduct().getColor());
				
				Float price_to_compare = s.getPriceAfterSale();
				ArrayList<PaymentAccount> pas;
				PaymentAccount pa;
				try {
					pas = Context.getUserAsCustomer().getPaymentAccounts();
					pa = Context.fac.paymentAccount.getPaymentAccountOfStore(pas,Context.mainScene.getCurrentStore());
					if(pa==null)
						throw new Exception();
				} catch (Exception e) {
					e.printStackTrace();
					Context.mainScene.loadMainMenu("Error");
					return;
				}
				this.sub=pa.getSub();
				if(sub != null)
					price_to_compare = Context.fac.sub.getPriceBySubscription(pa.getSub(), price_to_compare);
				if(cbt.min==null || cbt.min>price_to_compare.intValue())
					cbt.min=price_to_compare.intValue();
				if(cbt.max==null || cbt.max<price_to_compare+1)
					cbt.max = price_to_compare.intValue()+1;
				cbt.stocks.add(s);
			}
		}		
		if(Platform.isFxApplicationThread()) {
			cbType.setItems(FXCollections.observableArrayList(stockByType.keySet()));
			btnSend.setDisable(false);
		}
		else
			Platform.runLater(()->{
				cbType.setItems(FXCollections.observableArrayList(stockByType.keySet()));
				btnSend.setDisable(false);
			});
	}
	
	/** The function when the button {@link #btnSend} clicked. */
	public void assembleProduct() {
		ProductType type = cbType.getValue();
		Color color = cbColor.getValue();
		Float min = Float.parseFloat(txtMinPrice.getText()), 
				max =Float.parseFloat(txtMaxPrice.getText());
		if(type==null ||  min == null || max == null) {
			Context.mainScene.setMessage("You must select type and price range");
			return;
		}
		stocksAfterAssemble=Context.fac.product.assembleProduct(type, min, max, color, stockByType.get(type).stocks, this.sub);
		if(stocksAfterAssemble.isEmpty()==false) {
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
			Context.mainScene.setMessage("No match found");
		}
	}
	
	/** set the GUI by the assemble */
	private void initPage() throws Exception {
		components.clear();    	
    	pagination  = new Pagination(stocksAfterAssemble.size(), 0);
    	initArrays(stocksAfterAssemble.size());
    	int i = 0;
		for (Stock stk : stocksAfterAssemble) {
			Float newPrice = Context.fac.product.getPriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
			setVBox(i, 
					stk,
					stk.getPriceAfterSale().equals(newPrice)?null:newPrice,
					addToCart(stk.getProduct(),newPrice, stk));
			i++;
		}
		if(Platform.isFxApplicationThread())
			resetView();
		else
			Platform.runLater(()->resetView());
	}
	
	/** adds {@link ChangeListener} to all the various {@link Control}s	 */
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

	@Override
	protected void getProducts(ArrayList<Product> prds) {}

	@FXML public void typeSelected() {
		paneAss.setVisible(true);
	}
}