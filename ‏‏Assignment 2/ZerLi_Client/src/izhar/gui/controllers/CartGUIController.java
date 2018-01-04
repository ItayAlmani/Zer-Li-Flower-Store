package izhar.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CartGUIController extends ProductsPresentationGUIController {	
	public static ArrayList<ProductInOrder> products;
	
	public static Float ordPrice = 0f;
	public static boolean cartEmpty = true;
	
	 @Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		getProducts();
	}
	 
	 private void getProducts() {
		if(Context.order != null && Context.order.getProducts()!=null)
			setPIOs(Context.order.getProducts());
		else {
			 try {
				Context.fac.prodInOrder.getPIOsByOrder(Context.order.getOrderID());
			} catch (IOException e) {
				System.err.println("View Catalog");
				e.printStackTrace();
			}
		}
	}

	public void noPaymentAccountErrMsg() {
		// TODO - implement ViewCartGUI.noPaymentAccountErrMsg
		throw new UnsupportedOperationException();
	}
	
	private boolean isIfCartEmpty() {
		if(products==null || products.size()==0 || products.get(0)==null)
			return true;
		for (ProductInOrder prd : products)
			if(prd.getQuantity()>0)
				return false;
		return true;
	}
	
	public static boolean firstPagination = false;
	public void setPIOs(ArrayList<ProductInOrder> prds) {	
		if(firstPagination==true)
			firstPagination=false;
		Context.order.setProducts(prds);
		initGrids(prds);
		products=prds;
    	int i = 0;
    	if(pagination==null) {
    		pagination  = new Pagination(prds.size(), 0);
    		firstPagination=true;
    	}
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	if(cartEmpty=isIfCartEmpty()) {
		    		if(firstPagination==false) {
			    		pagination.setPageFactory(new Callback<Integer, Node>() {
				            @Override
				            public Node call(Integer pageIndex) {
				            	 return null;
				            }
						});
		    		}
		    		pagination.setVisible(false);
		    		lblTitleFPrice.setVisible(false);
		    		lblFinalPrice.setText("");
		    		lblMsg.setText("Cart is EMPTY!");
		    	}
		    	else {
		    		pagination.setVisible(true);
		    		lblFinalPrice.setText("");
		    		lblTitleFPrice.setVisible(true);
		    	}
			}});
		for (ProductInOrder p : prds) {
			ordPrice+=p.getFinalPrice();
			if(p.getQuantity()>0)
				cartEmpty=false;
			setVBox(i, p,updateQuantity());
			i++;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(cartEmpty==true)
					lblMsg.setText("Cart is EMPTY!");
				else {
					setLblFinalPrice();
					lblMsg.setText("");
				}
				if(vbox.getChildren().contains(pagination)==false)
					vbox.getChildren().add(0,pagination);
			}
		});
	}	
    
    private EventHandler<ActionEvent> updateQuantity(){
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(event.getSource() instanceof Button) {
					Button btn = (Button)event.getSource();
					int gridInx = (int) btn.getUserData();
					Integer quantity = null;
					
					try {
						quantity = spnShowQuantity[gridInx].getValue();
					}catch (NumberFormatException e) {
						lblMsg.setText("Quantity not number");
						return;
					}
					float oldFinalPrice = products.get(gridInx).getFinalPrice();
					products.get(gridInx).setQuantity(quantity);
					products.get(gridInx).setFinalPrice();
					ordPrice=ordPrice-oldFinalPrice+products.get(gridInx).getFinalPrice();
					setLblFinalPrice();
					try {
						Context.fac.prodInOrder.updatePriceOfPIO(products.get(gridInx));
						lblShowPrice[gridInx].setText(((Float)products.get(gridInx).getFinalPrice()).toString() + "�");
					} catch (IOException e) {
						System.err.println("prodInOrder.updatePriceOfPIO query failed");
						e.printStackTrace();
					}
					if(quantity==0) {
						products.remove(gridInx);
						grids[gridInx] = new GridPane();
						setPIOs(products);
					}
				}
			}
		};
    }
    
    private void setLblFinalPrice() {
    	if(ordPrice == Math.round(ordPrice))
			lblFinalPrice.setText(((Integer)Math.round(ordPrice)).toString()+ "�");
		else
			lblFinalPrice.setText(ordPrice.toString()+ "�");
    }

	/**
	 * 
	 * @param items
	 */
	private void initGrids(ArrayList<ProductInOrder> prds) {	
    	components.clear();
    	products = prds;
		
		/* The labels which indicates the title of each data of all products */
		lblTitleQuantity = new Label[prds.size()];
		
		/* The quantity of all products */
		spnShowQuantity = new Spinner[prds.size()];
		
		initArrays(prds.size());
	}

	public void createOrder(ActionEvent event) {
		if(Context.fac.order.isCartEmpty(products)==false) {
			Context.order.setProducts(products);
			Context.fac.order.calcFinalPriceOfOrder(Context.order);
			
			try {
				loadGUI("DeliveryGUI", false);
			} catch (Exception e) {
				lblMsg.setText("Loader failed");
				e.printStackTrace();
			}
		}
		else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblMsg.setText("Cart is empty!");
				}
			});
		}
	}
}