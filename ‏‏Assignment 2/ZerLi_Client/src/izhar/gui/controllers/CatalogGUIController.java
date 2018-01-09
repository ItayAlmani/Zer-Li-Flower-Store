package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import com.sun.prism.Image;

import common.Context;
import entities.Product;
import entities.ProductInOrder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;

public class CatalogGUIController extends ProductsPresentationGUIController {
	private ArrayList<Product> productsInCatalog = new ArrayList<>();
    
    protected void getProducts() {
    	if(productsInCatalog.size()!=0)
    		setProducts(productsInCatalog);
    	else {
			try {
				Context.fac.product.getProductsInCatalog();
			} catch (IOException e) {
				System.err.println("View Catalog");
				e.printStackTrace();
			}
    	}
	}

    public void setProducts(ArrayList<Product> prds) {	
    	components.clear();
    	productsInCatalog = prds;
		initArrays(prds.size());
    	
    	pagination  = new Pagination(productsInCatalog.size(), 0);
    	int i = 0;
		for (Product p : prds) {
			setVBox(i, p,addToCart(productsInCatalog));
			i++;
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnBack = new Button("Back");
				btnBack.setOnAction(actionEvent -> {
					pagination=null;
					Context.mainScene.loadMainMenu();
					});
				
				vbox.getChildren().addAll(pagination,btnBack);
				vbox.setAlignment(Pos.CENTER);
				vbox.getScene().getWindow().sizeToScene();
			}
		});
	}
    
    public static void setPIOID(BigInteger id) {
    	
	}
}