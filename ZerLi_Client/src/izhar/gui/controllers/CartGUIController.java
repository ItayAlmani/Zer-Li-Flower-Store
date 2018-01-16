package izhar.gui.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import common.Context;
import entities.ProductInOrder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class CartGUIController extends ProductsPresentationGUIController {	
	public static ArrayList<ProductInOrder> allPIOS, notZeroPIOS;
	private @FXML JFXButton btnOrderNow;
	
	public static Float ordPrice = 0f;
	public static boolean cartEmpty = true;
	 
	 protected void getProducts() {
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
	
	private void checkCartEmpty() {
		if((cartEmpty/*=(notZeroPIOS==null || notZeroPIOS.size()==0 || notZeroPIOS.get(0)==null)*/)==true) {
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
    		Context.mainScene.setMessage("Cart is EMPTY!");
    		btnOrderNow.setDisable(true);
    	}
    	else {
    		pagination.setVisible(true);
    		lblFinalPrice.setText("");
    		lblTitleFPrice.setVisible(true);
    		btnOrderNow.setDisable(false);
    	}
	}
	
	public static boolean firstPagination = false;
	public void setPIOs(ArrayList<ProductInOrder> prds) {	
		if(firstPagination==true)
			firstPagination=false;
		Context.order.setProducts(prds);
		initGrids(prds);
    	int i = 0;
    	if(pagination==null) {
    		pagination  = new Pagination(notZeroPIOS.size(), 0);
    		firstPagination=true;
    	}
    	ordPrice = Context.order.getFinalPrice();
		if (notZeroPIOS != null && !notZeroPIOS.isEmpty()) {
			cartEmpty=false;
			for (ProductInOrder p : notZeroPIOS) {
				setVBox(i, p,updateQuantity());
				i++;
			}
		}
		else
			cartEmpty=true;
    	if(Platform.isFxApplicationThread())
    		updateView();
    	else
    		Platform.runLater(()->updateView());
	}
	
	private void updateView() {
		if(cartEmpty==true) {
			if(Platform.isFxApplicationThread())
	    		checkCartEmpty();
	    	else
	    		Platform.runLater(()->checkCartEmpty());
		}
		else {
			setLblFinalPrice();
			Context.mainScene.setMessage("");
		}
		if(!vbox.getChildren().contains(pagination))
			vbox.getChildren().add(0,pagination);
		vbox.setAlignment(Pos.CENTER);
		//vbox.getScene().getWindow().sizeToScene();
		vbox.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
	}
    
    private EventHandler<ActionEvent> updateQuantity(){
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(event.getSource() instanceof Button) {
					Button btn = (Button)event.getSource();
					ProductInOrder pio = (ProductInOrder) btn.getUserData();
					int gridInx = (int) btn.getParent().getUserData();
					Integer quantity = null;
					try {
						quantity = spnShowQuantity[gridInx].getValue();
					}catch (NumberFormatException e) {
						Context.mainScene.setMessage("Quantity not number");
						return;
					}
					
					float oldFinalPrice = pio.getFinalPrice();
					pio.setQuantity(quantity);
					pio.setFinalPrice();
					try {
						Context.fac.prodInOrder.updatePriceOfPIO(pio);
						lblShowPrice[gridInx].setText(pio.getFinalPriceAsString());
						ordPrice=ordPrice-oldFinalPrice+pio.getFinalPrice();
						setLblFinalPrice();
						Context.fac.order.calcFinalPriceOfOrder(Context.order);
						if(quantity==0) {
							notZeroPIOS.remove(gridInx);
							grids[gridInx] = new GridPane();
							if(vbox.getChildren().contains(pagination))
								vbox.getChildren().remove(pagination);
				    		pagination  = new Pagination(notZeroPIOS.size(), 0);
							setPIOs(allPIOS);
						}
					} catch (IOException e) {
						System.err.println("prodInOrder.updatePriceOfPIO query failed");
						e.printStackTrace();
					}
				}
			}
		};
    }
    
    private void setLblFinalPrice() {
    	if(ordPrice == Math.round(ordPrice))
			lblFinalPrice.setText(((Integer)Math.round(ordPrice)).toString()+ "¤");
		else
			lblFinalPrice.setText(ordPrice.toString()+ "¤");
    }

	private void initGrids(ArrayList<ProductInOrder> prds) {	
    	components.clear();
    	allPIOS = prds;
    	notZeroPIOS = Context.fac.prodInOrder.getPIOsNot0Quantity(prds);
		
		/* The labels which indicates the title of each data of all products */
		lblTitleQuantity = new Label[notZeroPIOS.size()];
		
		/* The quantity of all products */
		spnShowQuantity = new Spinner[notZeroPIOS.size()];
		
		initArrays(notZeroPIOS.size());
	}

	public void createOrder() {
		if(notZeroPIOS != null && !notZeroPIOS.isEmpty()) {
			Context.order.setProducts(allPIOS);
			Context.fac.order.calcFinalPriceOfOrder(Context.order);
			
			Context.mainScene.loadGUI("DeliveryGUI", false);
		}
		else
			Context.mainScene.setMessage("Cart is empty!");
	}
}