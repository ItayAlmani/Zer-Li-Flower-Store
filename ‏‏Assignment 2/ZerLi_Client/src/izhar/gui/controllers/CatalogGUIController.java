package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Order;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.Product;
import entities.ProductInOrder;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

public class CatalogGUIController extends ProductsPresentationGUIController {
	private ArrayList<Product> productsInCatalog = new ArrayList<>();
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;

		getProducts();
	}

    private void getProducts() {
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
				btnBack.setOnAction(actionEvent -> loadMainMenu());
				lblMsg = new Label();
				
				vbox.getChildren().addAll(pagination,btnBack,lblMsg);
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
							lblMsg.setText("Added");
						}
					});
					
				}
			}
		};
    }
    
    public static void setPIOID(BigInteger id) {
    	
	}
}