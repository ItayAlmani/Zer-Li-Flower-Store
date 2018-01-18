package izhar.gui.controllers;

import java.util.ArrayList;

import common.Context;
import entities.Product;
import entities.Stock;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;

public class CatalogGUIController extends ProductsPresentationGUIController {
	private ArrayList<Stock> stocks = new ArrayList<>();
	private ArrayList<Product> prds = null;
    
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
    
    protected void getProducts(ArrayList<Product> prds) {
    	if(prds!=null && !prds.isEmpty()) {
    		this.prds=prds;
    		this.stocks=null;
			createCatalog();
    	}
    	else
    		Context.mainScene.setMessage("Can't open catalog right now!");
	}

    public void createCatalog() {	
    	components.clear();
    	int size = stocks!=null?stocks.size():(prds!=null?prds.size():0);
		initArrays(size);
		Pagination oldPag = pagination;
		
    	pagination  = new Pagination(size, 0);
    	
    	if(stocks!=null) {
	    	int i = 0;
			for (Stock stk : stocks) {
				if(stk.getProduct().isInCatalog()) {
					try {
						Context.fac.product.updatePriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
						setVBox(i, stk,addToCart(stk.getProduct(),stk.getPriceAfterSale()));
						i++;
					} catch (Exception e) {
						Context.mainScene.loadMainMenu("Your'e not customer");
						return;
					}
				}
			}
    	}
    	else if(prds != null) {
    		int i = 0;
			for (Product p : prds) {
				setVBox(i, p,null);
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
				
				vbox.setAlignment(Pos.CENTER);
				vbox.getScene().getWindow().sizeToScene();
			}
		});
	}
}