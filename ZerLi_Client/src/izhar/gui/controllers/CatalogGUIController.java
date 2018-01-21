package izhar.gui.controllers;

import java.util.ArrayList;

import common.Context;
import entities.Product;
import entities.Stock;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;

public class CatalogGUIController extends ProductsPresentationGUIController {
    
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
		
    	final Pagination oldPag = pagination;		
    	pagination  = new Pagination(size, 0);
    	initArrays(size);
    	
    	if(stocks!=null) {
	    	int i = 0;
			for (Stock stk : stocks) {
				if(stk.getProduct().isInCatalog()) {
					try {
						Float newPrice = Context.fac.product.getPriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
						setVBox(i, 
								stk,
								newPrice==null?null:newPrice*(1-stk.getSalePercetage()),
										addToCart(stk.getProduct(),
												newPrice==null ? 
														stk.getPriceAfterSale() :
															newPrice*(1-stk.getSalePercetage()), stk));
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
				setVBox(i, p,null, null);
				i++;
			}
    	}
		
		if(Platform.isFxApplicationThread())
			updateView(oldPag);
		else
			Platform.runLater(()->updateView(oldPag));
	}
    
    private void updateView(Pagination oldPag) {
    	int pagInd;
		if(vbox.getChildren().contains(oldPag)) {
			vbox.getChildren().remove(oldPag);
			pagInd = vbox.getChildren().size()-1;
		}
		else
			pagInd = vbox.getChildren().size();
		vbox.getChildren().add(pagInd,pagination);
		vbox.setAlignment(Pos.CENTER);
    }
}