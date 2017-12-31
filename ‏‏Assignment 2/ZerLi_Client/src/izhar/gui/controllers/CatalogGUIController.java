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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
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
import javafx.util.Callback;

public class CatalogGUIController extends ParentGUIController {
	
	private @FXML ScrollPane scroll;
	private @FXML Pagination pagination;
	private @FXML GridPane[] grids;
	private @FXML Button[] btnViewProduct;
	private @FXML ImageView[] imgImages;
	private @FXML Label[] lblShowID, lblShowType, lblShowColor, lblShowPrice, lblShowName,
			lblTitleID, lblTitleType, lblTitleColor, lblTitlePrice, lblTitleName;
	private @FXML Label lblMsg;
	private @FXML VBox vbox;
	
	private ArrayList<Node> components = new ArrayList<>();
	
	private Button btnBack;
	
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
		
    	productsInCatalog = prds;
    	
    	
    	pagination  = new Pagination(productsInCatalog.size(), 0);
    	int i = 0;
		for (Product p : prds) {
			setGridPane(i, p);
			pagination .setPageFactory(new Callback<Integer, Node>() {
	            @Override
	            public Node call(Integer pageIndex) {
	            	 return grids[pageIndex];
	            }
			});
			i++;
		}
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnBack = new Button("Back");
				btnBack.setOnAction(actionEvent -> loadMainMenu());
				lblMsg = new Label();
				
				vbox.getChildren().addAll(pagination ,btnBack,lblMsg);
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
			btn.setOnAction(addToCart());
		}
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
							try {
								Context.fac.order.addOrder(
										new Order(Context.getUserAsCustomer().getCustomerID(),
										OrderType.InfoSystem,
										OrderStatus.InProcess));
								Context.order.getProducts().add(pio);
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

    private GridPane setGridPane(int i, Product p) {
    	grids[i] = new GridPane();
		/*grids[i].setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));*/
		
		imgImages[i] = new ImageView(p.getImage());
		grids[i].setConstraints(imgImages[i], 1, i);
		components.add(imgImages[i]);
		
		lblTitleID[i]=new Label("ID: "); 
		setComponent(lblTitleID[i] ,0, i+1, i);
		lblShowID[i] = new Label(p.getPrdID().toString());
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
		return grids[i];
    }
}