package izhar.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Product;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
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

public class CatalogGUIController extends ProductsGUIController {
	
	private Button btnBack;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		getProducts();
	}

    private void getProducts() {
		try {
			Context.fac.product.getProductsInCatalog();
		} catch (IOException e) {
			System.err.println("View Catalog");
			e.printStackTrace();
		}
	}

    public void productsToGUI(ArrayList<Product> prds) {	
    	super.productsToGUI(prds);
    	int i = 0;
		for (Product p : prds) {
			setGridPane(i, p);
			i++;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnBack = new Button("Back");
				btnBack.setOnAction(actionEvent -> loadMainMenu(actionEvent));
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
							//System.out.println((int) btn.getUserData());
							Context.fac.order.addProductToOrder(products.get((int) btn.getUserData()));			
						}
				}
			});
		}
    }

    private void setGridPane(int i, Product p) {
    	grids[i] = new GridPane();
		grids[i].setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		imgImages[i] = new ImageView(p.getImage());
		grids[i].setConstraints(imgImages[i], 1, i);
		components.add(imgImages[i]);
		
		lblTitleID[i]=new Label("ID: "); 
		setComponent(lblTitleID[i] ,0, i+1, i);
		lblShowID[i] = new Label(((Integer)p.getPrdID()).toString());
		setComponent(lblShowID[i],1, i+1, i);
		
		lblTitleName[i]=new Label("Name: ");
		setComponent(lblTitleName[i] ,0, i+2, i);
		lblShowName[i] = new Label(p.getName());
		setComponent(lblShowName[i],1, i+2, i);
		
		lblTitleType[i]=new Label("Type: ");
		setComponent(lblTitleType[i] ,0, i+3, i);
		lblShowType[i] = new Label(p.getType().toString());
		setComponent(lblShowType[i],1, i+3, i);
		
		lblTitleColor[i]=new Label("Color: ");
		setComponent(lblTitleColor[i] ,0, i+4, i);
		lblShowColor[i] = new Label(p.getColor().toString());
		setComponent(lblShowColor[i],1, i+4, i);
		
		lblTitlePrice[i]=new Label("Price: ");
		setComponent(lblTitlePrice[i] ,0, i+5, i);
		lblShowPrice[i] = new Label(((Float)p.getPrice()).toString() + "¤");
		setComponent(lblShowPrice[i],1, i+5, i);
		
		btnViewProduct[i] = new Button("Add to cart");
		btnViewProduct[i].setUserData(i);
		setComponent(btnViewProduct[i],1, i+6, i);
		
		grids[i].getChildren().addAll(components);
		for (Node node : components)
			grids[i].setHalignment(node, HPos.CENTER);
		
		components.clear();
    }
}