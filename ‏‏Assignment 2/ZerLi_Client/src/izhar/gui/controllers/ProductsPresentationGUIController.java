package izhar.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Context;
import entities.Product;
import entities.ProductInOrder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public abstract class ProductsPresentationGUIController implements Initializable {
	protected @FXML Pagination pagination = null;
	protected @FXML GridPane[] grids;
	protected @FXML Button[] btnFinalProduct;
	protected @FXML ImageView[] imgImages;
	protected @FXML Label[] lblShowType, lblShowPrice, lblShowName,
							lblTitleType, lblTitlePrice, lblTitleName;
	protected @FXML VBox vbox = new VBox();
	protected @FXML VBox[] vbxProduct;
	protected ArrayList<Node> components = new ArrayList<>();
	
	protected Button btnBack;
	
	/*Only for cartGUI*/
	protected @FXML Spinner<Integer>[] spnShowQuantity;
	protected @FXML Label[] lblTitleQuantity;
	protected @FXML Label lblFinalPrice, lblTitleFPrice;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Context.currentGUI = this;
		getProducts();
	}
	
	protected abstract void getProducts();	
	
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
    	GridPane.setConstraints(cmp, col, row);
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
		lblShowName[i].setTextFill(getRandomColor());
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
		
		for (Node node : components) {
			GridPane.setHalignment(node, HPos.CENTER);
			GridPane.setHgrow(node, Priority.SOMETIMES);
		}
		grids[i].getChildren().addAll(components);
		vbxProduct[i].setFillWidth(true);
		vbxProduct[i].getChildren().add(btnFinalProduct[i]);
		
		components.clear();
		
		pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
            	//vbox.getScene().getWindow().sizeToScene();
            	return vbxProduct[pageIndex];
            }
		});
	}
	
	private Color getRandomColor() {
		double[] color = new double[3];
		double rangeMin = 0.05f, rangeMax = 0.6f;
		for (int i = 0; i < color.length; i++)
			color[i] = rangeMin + (rangeMax - rangeMin) * Math.random();
		return Color.color(color[0], color[1], color[2]);
	}
	
	protected EventHandler<ActionEvent> addToCart(ArrayList<Product> productsInCatalog){
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
							System.err.println("Can't add product\n");
							e.printStackTrace();
						}
					}
					else {
						pio.addOneToQuantity();
						
						Context.fac.prodInOrder.updatePriceWithSubscription(pio, Context.getUserAsCustomer());
						try {
							Context.fac.prodInOrder.updatePIO(pio);
						} catch (IOException e) {
							System.err.println("Can't update product\n");
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
}
