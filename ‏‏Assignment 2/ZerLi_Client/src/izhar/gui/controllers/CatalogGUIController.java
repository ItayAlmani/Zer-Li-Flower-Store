package izhar.gui.controllers;

import java.util.ArrayList;

import com.jfoenix.controls.JFXComboBox;

import common.Context;
import entities.Customer;
import entities.PaymentAccount;
import entities.Product;
import entities.Stock;
import entities.Store;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;

public class CatalogGUIController extends ProductsPresentationGUIController {
	private ArrayList<Stock> stocks = new ArrayList<>();
    
    protected void getProducts() {
    	if(Context.order!=null && 
    			Context.order.getDelivery()!=null && 
    			Context.order.getDelivery().getStore()!=null) {
    		this.stocks=Context.fac.stock.getInCatalogOnlyStock(Context.order.getDelivery().getStore().getStock());
    		createCatalog();
    	}
    	else
    		Context.mainScene.setMessage("Can't open catalog right now!");
	}

    public void createCatalog() {	
    	components.clear();
		initArrays(stocks.size());
		Pagination oldPag = pagination;
		
    	pagination  = new Pagination(stocks.size(), 0);
    	int i = 0;
		for (Stock stk : stocks) {
			if(stk.getProduct().isInCatalog()) {
				setVBox(i, stk,addToCart(stk.getProduct(),stk.getPriceAfterSale()));
				i++;
			}
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				int pagInd;
				if(vbox.getChildren().contains(oldPag)) {
					vbox.getChildren().remove(oldPag);
					pagInd = vbox.getChildren().size()-1;
				}
				else
					pagInd = vbox.getChildren().size();
				vbox.getChildren().add(pagInd,pagination);
				
				if(btnBack == null || vbox.getChildren().contains(btnBack) == false) {
					btnBack = new Button("Back");
					btnBack.setOnAction(actionEvent -> {
						pagination=null;
						Context.mainScene.loadMainMenu();
						});
					vbox.getChildren().add(btnBack);
				}
				
				vbox.setAlignment(Pos.CENTER);
				vbox.getScene().getWindow().sizeToScene();
			}
		});
	}
    
    /*public static void setPIOID(BigInteger id) {
    	
	}*/
}