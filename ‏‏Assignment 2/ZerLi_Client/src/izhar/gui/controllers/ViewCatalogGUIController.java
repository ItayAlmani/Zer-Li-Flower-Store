package izhar.gui.controllers;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class ViewCatalogGUIController extends ViewItemsGUIController {

	private @FXML ScrollPane scroll;
	private @FXML FlowPane flow;
	private @FXML GridPane[] grids;
	
	private @FXML Button[] btnViewProduct;
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;
		
		getProducts();
	}

    private void getProducts() {
		try {
			Context.fac.catalog.getProductsInCatalog();
		} catch (IOException e) {
			System.err.println("View Catalog");
			e.printStackTrace();
		}
	}

    public void productsToGUI(ArrayList<Product> prds) {
		ArrayList<Node> components = new ArrayList<>();
		grids = new GridPane[prds.size()];
		Label[] lblShowID, lblShowType, lblShowColor, lblShowPrice, lblShowName;
		lblShowID = lblShowName = lblShowType = lblShowColor = lblShowPrice = new Label[prds.size()];
		ImageView[] imgImage= new ImageView[prds.size()];
		btnViewProduct = new Button[prds.size()];
		int i = 0;
		for (Product p : prds) {
			grids[i] = new GridPane();
			
			imgImage[i] = new ImageView(p.getImage());
			grids[i].setConstraints(imgImage[i], 1, i);
			components.add(imgImage[i]);
			
			lblShowID[i] = new Label(((Integer)p.getPrdID()).toString());
			grids[i].setConstraints(lblShowID[i], 1, i+1);
			components.add(lblShowID[i]);
			
			lblShowName[i] = new Label(p.getName());
			grids[i].setConstraints(lblShowName[i], 1, i+2);
			components.add(lblShowName[i]);
			
			lblShowType[i] = new Label(p.getType().toString());
			grids[i].setConstraints(lblShowType[i], 1, i+3);
			components.add(lblShowType[i]);
			
			lblShowColor[i] = new Label(p.getColor().toString());
			grids[i].setConstraints(lblShowColor[i], 1, i+4);
			components.add(lblShowColor[i]);
			
			lblShowPrice[i] = new Label(((Float)p.getPrice()).toString() + "¤");
			grids[i].setConstraints(lblShowPrice[i], 1, i+5);
			components.add(lblShowPrice[i]);
			
			btnViewProduct[i] = new Button("Add to cart");
			grids[i].setConstraints(btnViewProduct[i], 1, i+6);
			components.add(btnViewProduct[i]);
			
			grids[i].getChildren().addAll(components);
			components.clear();
			i++;
		}
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				flow.getChildren().addAll(grids);
				scroll.setContent(flow);
			}
		});
	}
    
	/**
	 * 
	 * @param btn
	 */
	public void clickedViewProduct(Button btn) {
		// TODO - implement ViewCatalogGUI.clickedViewItem
		throw new UnsupportedOperationException();
	}
	
}