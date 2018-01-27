package orderNproducts.gui.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import common.Context;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import entities.PaymentAccount;
import gui.controllers.ParentGUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
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
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Stock;

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
	
	protected ArrayList<Stock> stocks = new ArrayList<>();
	protected ArrayList<Product> prds = null;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI=this;
		try {
			ArrayList<PaymentAccount> pas = Context.getUserAsCustomer().getPaymentAccounts();
			if(pas != null && !pas.isEmpty() && Context.order!=null && 
	    			Context.order.getDelivery()!=null && 
	    			Context.order.getDelivery().getStore()!=null) {
				Context.mainScene.setMenuPaneDisable(true);
				Context.fac.stock.getStockByStore(Context.order.getDelivery().getStore().getStoreID());
			}
			else {
				Context.mainScene.setMenuPaneDisable(true);
				Context.fac.product.getAllProducts();
				Context.mainScene.setMessage("For creating an order, approach to a store and open payment account");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setProducts(ArrayList<Product> prds) {
		Context.mainScene.setMenuPaneDisable(false);
		if(prds!=null)
			getProducts(prds);
	}
	
	public void setStocks(ArrayList<Stock> stocks) {
		Context.mainScene.setMenuPaneDisable(false);
		if(stocks!=null) {
			if(this.stocks==null || this.stocks.isEmpty())
				this.stocks=stocks;
    		Context.order.getDelivery().getStore().setStock(stocks);
    		getProducts();
    	}
    }
	
	protected abstract void getProducts();
	protected abstract void getProducts(ArrayList<Product> prds);
	
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
		if(size>0)
			pagination.setPageFactory((pageIndex)->vbxProduct[pageIndex]);
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
	
	protected void setVBox(int i,Object o, Float price_sub, EventHandler<ActionEvent> btnHandler) {
		int j = i;
		Product prd = null;
		ProductInOrder pio = null;
		Stock stk = null;
		String price = null, btnText = null, price_pa_str = null, price_sub_str = null;
		MaterialIcon mi = null;
		
		vbxProduct[i] = new VBox();
    	vbxProduct[i].setAlignment(Pos.CENTER);
    	grids[i] = new GridPane();
		
		if(o instanceof Stock) {
			stk = (Stock)o;
			prd = (Product)stk.getProduct();
			price=prd.getPriceAsString();
			if(price_sub != null)
				price_sub_str = priceToStr(price_sub);
			if(stk.getSalePercetage()!=0)
				price_pa_str=stk.getPriceAfterSaleAsString();
			btnText="Add to cart";
			mi = MaterialIcon.ADD_SHOPPING_CART;
		}
		else if(o instanceof ProductInOrder) {
			pio = (ProductInOrder)o;
			prd = pio.getProduct();
			price = pio.getFinalPriceAsString();
			btnText="Update quantity";
			mi = MaterialIcon.REPEAT;
		}
		else if(o instanceof Product) {
			prd = (Product)o;
			price = prd.getPriceAsString();
		}
		else return;
		ByteArrayInputStream bais = new ByteArrayInputStream(prd.getMybytearray());
		imgImages[i] = new ImageView(new Image(bais));
		imgImages[i].prefHeight(200);
		imgImages[i].maxHeight(250);
		imgImages[i].maxWidth(250);
		imgImages[i].setFitHeight(200);
		imgImages[i].setFitWidth(200);
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
			Stock s = Context.fac.stock.getStockByProductByStocks(stocks, pio.getProduct());
			Integer maxVal = Integer.MAX_VALUE;
			if(s!=null)
				maxVal=s.getQuantity()+pio.getQuantity();
			spnShowQuantity[i]=setSpinnerOnlyNumbers(spnShowQuantity[i], maxVal, (Integer)pio.getQuantity());
			setComponent(spnShowQuantity[i],1, j, i);
		}
		
		lblTitlePrice[i]=new Label("Price: ");
		setComponent(lblTitlePrice[i] ,0, ++j, i);
		lblShowPrice[i] = new Label(price);
		if(price_pa_str!=null || price_sub_str != null) {
			Label lblPricePA = null;
			HBox h = null;
			if(price_pa_str!=null) {
				lblShowPrice[i].getStyleClass().add("strike");
				lblShowPrice[i].setTooltip(new Tooltip("Original price"));
				lblPricePA = new Label(price_pa_str);
				lblPricePA.setTooltip(new Tooltip("Price after store sale"));
				lblPricePA.setTextFill(Color.BLUE);
				h = new HBox(5,lblShowPrice[i],lblPricePA);
			}
			if(price_sub_str != null) {
				lblShowPrice[i].getStyleClass().add("strike");
				lblShowPrice[i].setTooltip(new Tooltip("Original price"));
				if(lblPricePA!=null)
					lblPricePA.getStyleClass().add("strike");
				Label lblPriceSub = new Label(price_sub_str);
				lblPriceSub.setTooltip(new Tooltip("Price after subscription discount"));
				lblPriceSub.setTextFill(Color.GREEN);
				if(h!=null)
					h.getChildren().add(lblPriceSub);
				else
					h = new HBox(5,lblShowPrice[i],lblPriceSub);
			}
			if(h!=null) {
				h.setAlignment(Pos.CENTER);
				setComponent(h,1, j, i);
			}
		}
		else
			setComponent(lblShowPrice[i],1, j, i);
		
		if(btnHandler!=null) {
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
		}
		
		for (Node node : components) {
			GridPane.setHalignment(node, HPos.CENTER);
			GridPane.setHgrow(node, Priority.SOMETIMES);
		}
		grids[i].getChildren().addAll(components);
		vbxProduct[i].setFillWidth(true);
		if(btnHandler!=null)
			vbxProduct[i].getChildren().add(btnFinalProduct[i]);
		vbxProduct[i].setUserData(i);
		vbxProduct[i].getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
		
		components.clear();
	}
	
	protected Color getRandomColor(Product prod) {
		if(prod.getColor().equals(orderNproducts.entities.Product.Color.Colorfull)==false) {
			if(prod.getColor().equals(orderNproducts.entities.Product.Color.Pink))
				return Color.DEEPPINK;
			else if(prod.getColor().equals(orderNproducts.entities.Product.Color.Yellow))
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
	 * @param stock the stock which contains the {@link Product}. Will be used for check and update specific stock 
	 * @return
	 */
	protected EventHandler<ActionEvent> addToCart(Product p, Float price, Stock stock){
    	return (event)->pio=Context.fac.order.manageCart(p, price, pio, stock);
    }
	
	public void setPIOID(BigInteger id) {
		Context.mainScene.setMenuPaneDisable(false);
		if(pio!=null)
			pio.setId(id);
		else
			Context.mainScene.ShowErrorMsg();
	}
	
	protected String priceToStr(Float price) {
		DecimalFormat df = new DecimalFormat("##.##");
		return df.format(price) + "¤";
	}
	
	private Spinner<Integer> setSpinnerOnlyNumbers(Spinner<Integer> spinner, Integer maxVal, Integer currVal) {
		// get a localized format for parsing
		NumberFormat format = NumberFormat.getIntegerInstance();
		UnaryOperator<TextFormatter.Change> filter = c -> {
		    if (c.isContentChange()) {
		        ParsePosition parsePosition = new ParsePosition(0);
		        // NumberFormat evaluates the beginning of the text
		        format.parse(c.getControlNewText(), parsePosition);
		        if (parsePosition.getIndex() == 0 ||
		                parsePosition.getIndex() < c.getControlNewText().length()) {
		            // reject parsing the complete text failed
		            return null;
		        }
		    }
		    return c;
		};
		TextFormatter<Integer> priceFormatter = new TextFormatter<Integer>(
		        new IntegerStringConverter(), currVal, filter);

		if(spinner == null)
			spinner = new Spinner<>();
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
		        0, maxVal, currVal));
		spinner.setEditable(true);
		spinner.getEditor().setTextFormatter(priceFormatter);
		spinner.setPrefWidth(Control.USE_COMPUTED_SIZE);
		
		spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		spinner.getEditor().setAlignment(Pos.CENTER);
		final Spinner<Integer> finalSpin = spinner;
		// useage in client code
		spinner.focusedProperty().addListener((s, ov, nv) -> {
		    if (nv) return;
		    //intuitive method on textField, has no effect, though
		    //spinner.getEditor().commitValue(); 
		    commitEditorText(finalSpin);
		});
		return spinner;
	}
	
	private <T> void commitEditorText(Spinner<T> spinner) {
	    if (!spinner.isEditable()) return;
	    String text = spinner.getEditor().getText();
	    SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
	    if (valueFactory != null) {
	        StringConverter<T> converter = valueFactory.getConverter();
	        if (converter != null) {
	            T value = converter.fromString(text);
	            valueFactory.setValue(value);
	        }
	    }
	}
}
