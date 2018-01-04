package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import common.Context;
import entities.Product;
import entities.ProductInOrder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;

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
			setVBox(i, p,addToCart());
			i++;
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnBack = new Button("Back");
				btnBack.setOnAction(actionEvent -> Context.mainScene.loadMainMenu());
				
				vbox.getChildren().addAll(pagination,btnBack);
				vbox.setAlignment(Pos.CENTER);
				vbox.getScene().getWindow().sizeToScene();
			}
		});
	}
    
    private EventHandler<ActionEvent> addToCart(){
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(event.getSource() instanceof Button) {
					Button btn = (Button)event.getSource();
					Product prd = productsInCatalog.get((int) btn.getUserData());
					ProductInOrder pio = Context.order.containsProduct(prd);
					if(pio==null) {
						pio = new ProductInOrder(prd, 1, Context.order.getOrderID());
						if(Context.order.getProducts()==null)
							Context.order.setProducts(new ArrayList<>());
						Context.order.getProducts().add(pio);
						try {
							Context.fac.prodInOrder.addPIO(pio);
						} catch (IOException e) {
							System.err.println("Can't add PIO in Catalog\n");
							e.printStackTrace();
						}
					}
					else {
						pio.addOneToQuantity();
						
						Context.fac.prodInOrder.updatePriceWithSubscription(pio, Context.getUserAsCustomer());
						try {
							Context.fac.prodInOrder.updatePIO(pio);
						} catch (IOException e) {
							System.err.println("Can't update PIO in Catalog\n");
							e.printStackTrace();
						}
					}
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							Context.mainScene.setMessage("Added");
						}
					});
					
				}
			}
		};
    }
    
    public static void setPIOID(BigInteger id) {
    	
	}
}