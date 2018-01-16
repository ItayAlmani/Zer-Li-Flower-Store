package izhar.gui.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import common.Context;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import entities.Product;
import entities.ProductInOrder;
import entities.Stock;
import gui.controllers.ParentGUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

public abstract class ProductsPresentationGUIController implements Initializable {
	protected @FXML Pagination pagination = null;
	protected @FXML GridPane[] grids;
	protected @FXML JFXButton[] btnFinalProduct;
	protected @FXML ImageView[] imgImages;
	protected @FXML Label[] lblShowType, lblShowPrice, lblShowName,
							lblTitleType, lblTitlePrice, lblTitleName;
	protected @FXML VBox vbox = new VBox();
	protected @FXML VBox[] vbxProduct;
	protected @FXML MaterialIconView[] icnButtons;
	protected ArrayList<Node> components = new ArrayList<>();
	
	/*Only for cartGUI*/
	protected @FXML Spinner<Integer>[] spnShowQuantity;
	protected @FXML Label[] lblTitleQuantity;
	protected @FXML Label lblFinalPrice, lblTitleFPrice;
	
	/** Will be saved for the id which will be return to setPIOID.
	 * Static for the use in the EventHandler*/
	private static ProductInOrder pio;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;
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
		btnFinalProduct = new JFXButton[size];
		
		icnButtons = new MaterialIconView[size];
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
    	if(col==0)
    		cmp.getStyleClass().add("menuTitle");
		components.add(cmp);
    }
	
	protected void setVBox(int i,Object o, EventHandler<ActionEvent> btnHandler) {
		int j = i;
		Product prd = null;
		ProductInOrder pio = null;
		Stock stk = null;
		String price = null, btnText = null, priceAfterSale = null;
		MaterialIcon mi;
		
		vbxProduct[i] = new VBox();
    	vbxProduct[i].setAlignment(Pos.CENTER);
    	grids[i] = new GridPane();
		
		if(o instanceof Stock) {
			stk = (Stock)o;
			prd = (Product)stk.getProduct();
			price=prd.getPriceAsString();
			if(stk.getSalePercetage()!=0)
				priceAfterSale=stk.getPriceAfterSaleAsString();
			btnText="Add to cart";
			mi = MaterialIcon.ADD_SHOPPING_CART;
		}
		else if(o instanceof ProductInOrder) {
			pio = (ProductInOrder)o;
			prd = pio.getProduct();
			price=pio.getFinalPriceAsString();
			btnText="Update quantity";
			mi = MaterialIcon.REPEAT;
		}
		else return;
		ByteArrayInputStream bais = new ByteArrayInputStream(prd.getMybytearray());
		imgImages[i] = new ImageView(new Image(bais));
		imgImages[i].prefHeight(200);
		try {
			bais.close();
			vbxProduct[i].getChildren().addAll(imgImages[i],grids[i]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lblTitleName[i]=new Label("Name: ");
		setComponent(lblTitleName[i] ,0, j, i);
		lblShowName[i] = new Label(prd.getName());
		lblShowName[i].setTextFill(getRandomColor(prd));
		if(lblShowName[i].getTextFill().equals(Color.WHITE))
			lblShowName[i].setBackground(new Background(new BackgroundFill(Color.GRAY,null,null)));
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
		if(priceAfterSale!=null) {
			lblShowPrice[i].getStyleClass().add("strike");
			Label curPrice = new Label(priceAfterSale);
			curPrice.setTextFill(Color.BLUE);
			HBox h = new HBox(5,lblShowPrice[i],curPrice);
			h.setAlignment(Pos.CENTER);
			setComponent(h,1, j, i);
		}
		else
			setComponent(lblShowPrice[i],1, j, i);
		
		btnFinalProduct[i] = new JFXButton(btnText);
		btnFinalProduct[i].setButtonType(ButtonType.RAISED);
		btnFinalProduct[i].setRipplerFill(Color.ORANGE);
		btnFinalProduct[i].setUserData(pio);
		btnFinalProduct[i].setOnAction(btnHandler);
		icnButtons[i] = new MaterialIconView(mi);
		icnButtons[i].setSize("15");
		icnButtons[i].setFont(Font.font("Material Icons", 15));
		icnButtons[i].setFill(Color.ORANGE);
		btnFinalProduct[i].setGraphic(icnButtons[i]);
		btnFinalProduct[i].setTextFill(Color.ORANGE);
		btnFinalProduct[i].setFont(Font.font(15));
		
		for (Node node : components) {
			GridPane.setHalignment(node, HPos.CENTER);
			GridPane.setHgrow(node, Priority.SOMETIMES);
		}
		grids[i].getChildren().addAll(components);
		vbxProduct[i].setFillWidth(true);
		vbxProduct[i].getChildren().add(btnFinalProduct[i]);
		vbxProduct[i].setUserData(i);
		vbxProduct[i].getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		
		components.clear();
		
		pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
            	return vbxProduct[pageIndex];
            }
		});
	}
	
	protected Color getRandomColor(Product prod) {
		if(prod.getColor().equals(entities.Product.Color.Colorfull)==false) {
			if(prod.getColor().equals(entities.Product.Color.Pink))
				return Color.DEEPPINK;
			else if(prod.getColor().equals(entities.Product.Color.Yellow))
				return Color.GOLD;
			return Color.valueOf(prod.getColor().toString());
		}
		double[] color = new double[3];
		double rangeMin = 0.05f, rangeMax = 0.6f;
		for (int i = 0; i < color.length; i++)
			color[i] = rangeMin + (rangeMax - rangeMin) * Math.random();
		return Color.color(color[0], color[1], color[2]);
	}
	
	/**
	 * 
	 * @param p
	 * @param price will indicate the product price - can be with or without sale.
	 * <b><i>The price is without subscription</i></b>
	 * @return
	 */
	protected EventHandler<ActionEvent> addToCart(Product p, Float price){
    	return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(event.getSource() instanceof Button) {
					Product prd = new Product(p);	//create new copy
					prd.setPrice(price);
					pio = Context.order.containsProduct(prd);
					
					if(pio==null) {
						pio = new ProductInOrder(prd, 1, Context.order.getOrderID());
						if(Context.order.getProducts()==null)
							Context.order.setProducts(new ArrayList<>());
						Context.order.getProducts().add(pio);
						try {
							Context.fac.prodInOrder.add(pio, true);
						} catch (IOException e) {
							System.err.println("Can't add product\n");
							e.printStackTrace();
							return;
						}
					}
					else {
						try {
							pio.addOneToQuantity();
							Context.fac.prodInOrder.updatePriceWithSubscription(Context.order,pio, Context.getUserAsCustomer());
							Context.fac.prodInOrder.update(pio);
						} catch (Exception e) {
							System.err.println("Can't update product\n");
							e.printStackTrace();
							return;
						}
					}
					Context.fac.order.calcFinalPriceOfOrder(Context.order);
				}
			}
		};
    }
	
	public void setPIOID(BigInteger id) {
		pio.setId(id);
	}
}
