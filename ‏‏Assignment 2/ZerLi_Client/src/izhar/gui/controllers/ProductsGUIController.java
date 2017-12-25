package izhar.gui.controllers;

import java.util.ArrayList;

import common.Context;
import entities.Product;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public abstract class ProductsGUIController extends ParentGUIController{

	protected @FXML ScrollPane scroll;
	protected @FXML FlowPane flow;
	protected @FXML GridPane[] grids;
	protected @FXML Button[] btnViewProduct;
	protected @FXML ImageView[] imgImages;
	protected @FXML Label[] lblShowID, lblShowType, lblShowColor, lblShowPrice, lblShowName,
			lblTitleID, lblTitleType, lblTitleColor, lblTitlePrice, lblTitleName;
	
	protected ArrayList<Node> components = new ArrayList<>();
	
	protected ArrayList<Product> products;

	/**
	 * 
	 * @param items
	 */
	public void productsToGUI(ArrayList<Product> prds) {	
    	components.clear();
    	products = prds;
		/* The GridPanes which include the all data of all products */
		grids = new GridPane[prds.size()];
		
		/* The labels which include each data of all products */
		lblShowID = lblShowName = lblShowType = lblShowColor = lblShowPrice = new Label[prds.size()];
		
		/* The labels which indicates the title of each data of all products */
		lblTitleID = lblTitleName = lblTitleType = lblTitleColor = lblTitlePrice = new Label[prds.size()];
		
		/* The images of all products */
		imgImages= new ImageView[prds.size()];
		
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