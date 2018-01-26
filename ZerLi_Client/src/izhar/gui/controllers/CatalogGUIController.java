package izhar.gui.controllers;

import java.util.ArrayList;

import common.Context;
import entities.Product;
import entities.Stock;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;

public class CatalogGUIController extends ProductsPresentationGUIController {
    
	@Override
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
    
	@Override
    protected void getProducts(ArrayList<Product> prds) {
    	if(prds!=null && !prds.isEmpty()) {
    		this.prds=prds;
    		this.stocks=null;
			createCatalog();
    	}
    	else
    		Context.mainScene.setMessage("Can't open catalog right now!");
	}

	/** setting the screen by data */
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
								stk.getPriceAfterSale().equals(newPrice)?null:newPrice,
										addToCart(stk.getProduct(),newPrice, stk));
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
    
    /**
     * updating the screen after changes
     * @param oldPag will be removed from {@link ProductsPresentationGUIController#vbox}
     */
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