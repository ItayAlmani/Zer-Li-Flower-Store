package izhar.gui.controllers;

import java.util.ArrayList;

import entities.Product;
import entities.ProductInOrder;
import gui.controllers.ParentGUIController;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ProductsPresentationGUIController extends ParentGUIController {
	protected @FXML ScrollPane scroll;
	protected @FXML Pagination pagination = null;
	protected @FXML GridPane[] grids;
	protected @FXML Button[] btnFinalProduct;
	protected @FXML ImageView[] imgImages;
	protected @FXML Label[] lblShowType, lblShowPrice, lblShowName,
							lblTitleType, lblTitlePrice, lblTitleName;
	protected @FXML VBox vbox;
	protected @FXML VBox[] vbxProduct;
	protected ArrayList<Node> components = new ArrayList<>();
	
	protected Button btnBack;
	
	/*Only for cartGUI*/
	protected @FXML Spinner<Integer>[] spnShowQuantity;
	protected @FXML Label[] lblTitleQuantity;
	protected @FXML Label lblFinalPrice, lblTitleFPrice;
	
	protected void initArrays(int size) {
		components.clear();
		/* The GridPanes which include the all data of all products */
		grids = new GridPane[size];
		/* The labels which include each data of all products */
		lblShowName = lblShowType = lblShowPrice = new Label[size];
		/* The labels which indicates the title of each data of all products */
		lblTitleName = lblTitleType = lblTitlePrice = new Label[size];
		/* The images of all products */
		imgImages= new ImageView[size];
		/* The order buttons of all products */
		btnFinalProduct = new Button[size];
		vbxProduct = new VBox[size];
	}
	
	/**
     * adding cmp to it's GridPane and to the list of components
     * @param cmp	-	the Label/Button which need to be 
	 * @param col	-	the column in the GridPane to add to
	 * @param row	-	the row in the GridPane to add to
	 * @param i		-	the index of the GridPane in grids
     */
	protected void setComponent(Node cmp, int col, int row, int i) {
    	grids[i].setConstraints(cmp, col, row);
    	//if its title and not show
    	if(col==0)
    		cmp.setId("lblTitle");
		components.add(cmp);
    }
	
	protected void setVBox(int i,Object p, EventHandler<ActionEvent> btnHandler) {
		int j = i;
		Product prd = null;
		ProductInOrder pio = null;
		String price = null, btnText = null;
		
		vbxProduct[i] = new VBox();
    	vbxProduct[i].setAlignment(Pos.CENTER);
    	grids[i] = new GridPane();
		
		if(p instanceof Product) {
			prd = (Product)p;
			price=prd.getPriceAsString();
			btnText="Add to cart";
		}
		else if(p instanceof ProductInOrder) {
			pio = (ProductInOrder)p;
			prd = pio.getProduct();
			price=pio.getFinalPriceAsString();
			btnText="Update quantity";
		}
		else return;
		
		imgImages[i] = new ImageView(new Image(getClass().getResourceAsStream(prd.getImageName())));
		vbxProduct[i].getChildren().addAll(imgImages[i],grids[i]);
		
		lblTitleName[i]=new Label("Name: ");
		setComponent(lblTitleName[i] ,0, j, i);
		lblShowName[i] = new Label(prd.getName());
		lblShowName[i].setTextFill(Color.color(Math.random(), Math.random(), Math.random()));
		setComponent(lblShowName[i],1, j, i);
		
		lblTitleType[i]=new Label("Type: ");
		setComponent(lblTitleType[i] ,0, ++j, i);
		lblShowType[i] = new Label(prd.getType().toString());
		setComponent(lblShowType[i],1, j, i);
		
		if(pio!=null) {
			lblTitleQuantity[i]=new Label("Quantity: ");
			setComponent(lblTitleQuantity[i] ,0, ++j, i);
			spnShowQuantity[i] = new Spinner<>(0, Integer.MAX_VALUE, (Integer)pio.getQuantity());
			spnShowQuantity[i].setPrefWidth(Control.USE_COMPUTED_SIZE);
			setComponent(spnShowQuantity[i],1, j, i);
		}
		
		lblTitlePrice[i]=new Label("Price: ");
		setComponent(lblTitlePrice[i] ,0, ++j, i);
		lblShowPrice[i] = new Label(price);
		setComponent(lblShowPrice[i],1, j, i);
		
		btnFinalProduct[i] = new Button(btnText);
		btnFinalProduct[i].setUserData(i);
		btnFinalProduct[i].setOnAction(btnHandler);
		
		grids[i].getChildren().addAll(components);
		for (Node node : components)
			grids[i].setHalignment(node, HPos.CENTER);
		
		vbxProduct[i].getChildren().add(btnFinalProduct[i]);
		
		components.clear();
		
		pagination .setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
            	 return vbxProduct[pageIndex];
            }
		});
	}
}
