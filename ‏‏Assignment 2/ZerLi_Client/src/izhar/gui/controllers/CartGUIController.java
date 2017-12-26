package izhar.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Product;
import entities.ProductInOrder;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class CartGUIController extends ParentGUIController {
	
	protected @FXML ScrollPane scroll;
	protected @FXML FlowPane flow;
	protected @FXML GridPane[] grids;
	protected @FXML Button[] btnViewProduct;
	protected @FXML ImageView[] imgImages;
	protected @FXML TextField[] txtShowQuantity;
	protected @FXML Label[] lblShowID, lblShowType, lblShowColor, lblShowPrice, lblShowName,
			lblTitleID, lblTitleType, lblTitleColor, lblTitlePrice, lblTitleName, lblTitleQuantity;
	
	protected ArrayList<Node> components = new ArrayList<>();
	
	protected ArrayList<ProductInOrder> products;
	
	private Button btnBack;
	
	 @Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		getProducts();
	}
	 
	 private void getProducts() {
		if(Context.order != null && Context.order.getProducts()!=null)
			productsInOrderToGUI(Context.order.getProducts());
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
	
	public void productsInOrderToGUI(ArrayList<ProductInOrder> prds) {	
		Context.order.setProducts(prds);
		initGrids(prds);
    	int i = 0;
		for (ProductInOrder p : prds) {
			setGridPane(i, p);
			i++;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnBack = new Button("Back");
				btnBack.setOnAction(actionEvent -> loadMainMenu());
				flow.getChildren().add(btnBack);
			}
		});
	}	
    
    /**
     * adding cmp to it's GridPane and to the list of components
     * @param cmp	-	the Label/Button which need to be 
     * @param col	-	the column in the GridPane to add to
     * @param row	-	the row in the GridPane to add to
     * @param i		-	the index of the GridPane in grids
     */
    private void setComponent(Node cmp, int col, int row, int i) {
    	grids[i].setConstraints(cmp, col, row);
		components.add(cmp);
		if(cmp instanceof Button)
		{
			Button btn = (Button)cmp;
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
						if(event.getSource() instanceof Button) {
							Button btn = (Button)event.getSource();
							int gridInx = (int) btn.getUserData();
							Integer quantity = null;
							
							try {
								quantity = Integer.parseInt(txtShowQuantity[gridInx].getText());
							}catch (NumberFormatException e) {
								lblMsg.setText("Quantity not number");
								return;
								
							}
							if(quantity==0) {
								products.remove(gridInx);
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										flow.getChildren().remove(grids[gridInx]);
										grids[gridInx] = new GridPane();
									}
								});
							}
							try {
								products.get(gridInx).setQuantity(quantity);
								products.get(gridInx).setFinalPrice();
								Context.fac.prodInOrder.updatePriceOfPIO(products.get(gridInx));
								lblShowPrice[gridInx].setText(((Float)products.get(gridInx).getFinalPrice()).toString() + "¤");
							} catch (IOException e) {
								System.err.println("prodInOrder.updatePriceOfPIO query failed");
								e.printStackTrace();
							}		
						}
				}
			});
		}
    }

    private void setGridPane(int i, ProductInOrder p) {
    	grids[i] = new GridPane();
		grids[i].setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		imgImages[i] = new ImageView(p.getProduct().getImage());
		grids[i].setConstraints(imgImages[i], 1, i);
		components.add(imgImages[i]);
		
		lblTitleID[i]=new Label("ID: "); 
		setComponent(lblTitleID[i] ,0, i+1, i);
		lblShowID[i] = new Label(((Integer)p.getProduct().getPrdID()).toString());
		setComponent(lblShowID[i],1, i+1, i);
		
		lblTitleName[i]=new Label("Name: ");
		setComponent(lblTitleName[i] ,0, i+2, i);
		lblShowName[i] = new Label(p.getProduct().getName());
		setComponent(lblShowName[i],1, i+2, i);
		
		lblTitleType[i]=new Label("Type: ");
		setComponent(lblTitleType[i] ,0, i+3, i);
		lblShowType[i] = new Label(p.getProduct().getType().toString());
		setComponent(lblShowType[i],1, i+3, i);
		
		lblTitleColor[i]=new Label("Color: ");
		setComponent(lblTitleColor[i] ,0, i+4, i);
		lblShowColor[i] = new Label(p.getProduct().getColor().toString());
		setComponent(lblShowColor[i],1, i+4, i);
		
		lblTitleQuantity[i]=new Label("Quantity: ");
		setComponent(lblTitleQuantity[i] ,0, i+5, i);
		txtShowQuantity[i] = new TextField(((Integer)p.getQuantity()).toString());
		setComponent(txtShowQuantity[i],1, i+5, i);
		
		lblTitlePrice[i]=new Label("Price: ");
		setComponent(lblTitlePrice[i] ,0, i+6, i);
		lblShowPrice[i] = new Label(((Float)p.getFinalPrice()).toString() + "¤");
		setComponent(lblShowPrice[i],1, i+6, i);
		
		btnViewProduct[i] = new Button("Update quantity");
		btnViewProduct[i].setUserData(i);
		setComponent(btnViewProduct[i],1, i+7, i);
		
		grids[i].getChildren().addAll(components);
		for (Node node : components)
			grids[i].setHalignment(node, HPos.CENTER);
		
		components.clear();
    }

	/**
	 * 
	 * @param items
	 */
	private void initGrids(ArrayList<ProductInOrder> prds) {	
    	components.clear();
    	products = prds;
		/* The GridPanes which include the all data of all products */
		grids = new GridPane[prds.size()];
		
		/* The labels which include each data of all products */
		lblShowID = lblShowName = lblShowType = lblShowColor = lblShowPrice = new Label[prds.size()];
		
		/* The labels which indicates the title of each data of all products */
		lblTitleID = lblTitleName = lblTitleType = lblTitleColor = lblTitlePrice = lblTitleQuantity = new Label[prds.size()];
		
		/* The images of all products */
		imgImages= new ImageView[prds.size()];
		
		/* The quantity of all products */
		txtShowQuantity = new TextField[prds.size()];
		
		/* The order buttons of all products */
		btnViewProduct = new Button[prds.size()];
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				flow.getChildren().addAll(grids);
				scroll.setContent(flow);
			}
		});
	}
}