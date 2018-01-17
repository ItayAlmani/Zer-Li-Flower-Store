package izhar.gui.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import entities.Customer;
import entities.Product;
import entities.Product.Color;
import entities.Product.ProductType;
import entities.User.UserType;
import entities.Stock;
import entities.Store;
import entities.StoreWorker;
import entities.User;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class UpdateCatalogGUIController implements Initializable{

	private @FXML JFXComboBox<Stock> cbStocks;
	private JFXComboBox<Product> cbProducts = null;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXButton btnUpdate;
	private @FXML JFXTextField txtID, txtName, txtPrice, txtSale;
	private ArrayList<JFXTextField> textFields;
	private @FXML VBox paneItem, paneScene;
	private @FXML HBox hbSale;
	private @FXML ImageView imgImage;
	private @FXML JFXToggleButton tglInCatalog;
	private FileChooser fileChooser = new FileChooser();
	private File imgFile = null;
	
	public void setProductsInCB() {
		User u = Context.getUser();
		if(u.getPermissions().equals(UserType.StoreWorker) ||
			u.getPermissions().equals(UserType.StoreManager)) {
			try {
				/////////WAIT FOR GET STORE WORKER!!
				StoreWorker sw = Context.getUserAsStoreWorker();
				Store s = sw.getStore();
				if(s.getStock()!=null)
					setItems(s.getStock(),cbStocks);
				else
					throw new NullPointerException("s.getStock() is null");
			} catch (Exception e) {
				System.err.println("Not store worker - is a " + u.getPermissions());
				System.err.println(e.getMessage());
				e.printStackTrace();
				Context.mainScene.ShowErrorMsg();
			}
		}
		else if(u.getPermissions().equals(UserType.ChainStoreWorker) || 
				u.getPermissions().equals(UserType.ChainStoreManager)) {
			if(Platform.isFxApplicationThread())
				rearrangeComboBoxes();
			else
				Platform.runLater(()->rearrangeComboBoxes());
			try {
				Context.fac.product.getAllProducts();
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				return;
			}
		}
	}
	
	private void rearrangeComboBoxes() {
		cbProducts = new JFXComboBox<>();
		int ind = paneScene.getChildren().indexOf(cbStocks);
		paneScene.getChildren().add(ind, cbProducts);
		cbProducts.setOnAction(cbStocks.getOnAction());
		cbProducts.setPromptText(cbStocks.getPromptText());
		paneScene.getChildren().remove(cbStocks);
		cbStocks = null;
		if(!paneItem.getChildren().remove(hbSale)) {
			System.err.println("Can't remove txtSale");
			hbSale.setVisible(false);
		}
	}
	
	public void setProducts(ArrayList<Product> prds) {
		if(prds!=null)
			setItems(prds,cbProducts);
		else
			throw new NullPointerException("prds is null");
	}
	
	private <E> void setItems(ArrayList<E> arr, JFXComboBox<E> cb) {
		if(Platform.isFxApplicationThread()) {
			cb.setItems(FXCollections.observableArrayList(arr));
			paneItem.setVisible(false);
		}
		else {
			Platform.runLater(()->{
				cb.setItems(FXCollections.observableArrayList(arr));
				paneItem.setVisible(false);
			});
		}
	}
	
	public void showProduct(ActionEvent event) throws Exception {
		if(event.getSource() != null && event.getSource().equals(cbStocks)) {
			if(cbStocks.getValue()!=null)
				loadStock(cbStocks.getValue());
		}
		else if(event.getSource() != null && event.getSource().equals(cbProducts)){
			if(cbProducts.getValue()!=null)
				loadProduct(cbProducts.getValue());
		}
	}
	
	public void loadStock(Stock s) {
		if(s.getSalePercetage()!=null)
			this.txtSale.setText(((Float)(s.getSalePercetage()*100)).toString());
		else {
			this.txtSale.setText("");
			Context.mainScene.ShowErrorMsg();
			System.err.println("Sale percentage is null - write the StoreWorkerCtrl");
		}
		loadProduct(s.getProduct());
	}
	
	public void loadProduct(Product p) {
		paneItem.setVisible(false);
		if(p==null) {
			Context.mainScene.ShowErrorMsg();
			return;
		}		
		BigInteger id = p.getPrdID();
		this.txtID.setText(id.toString());
		this.txtName.setText(p.getName());
		this.cbType.setValue(p.getType());
		this.cbColor.setValue(p.getColor());
		this.txtPrice.setText(p.getPriceAsString().substring(0, p.getPriceAsString().length()-1));
		this.tglInCatalog.setSelected(p.isInCatalog());
		ByteArrayInputStream bais = new ByteArrayInputStream(p.getMybytearray());
		imgImage.setImage(new Image(bais));
		imgImage.prefHeight(200);
		imgImage.maxWidth(250);
		try {
			bais.close();
			paneItem.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProd() throws Exception {
		Context.mainScene.clearMsg();
		boolean isStock;
		Product p = null;
		if(this.cbStocks != null) {
			isStock = true;
			Stock s = this.cbStocks.getValue();
			if(s!=null)
				p = s.getProduct();
		}
		else if(this.cbProducts != null) {
			isStock = false;
			p = this.cbProducts.getValue();
		}
		else {
			Context.mainScene.ShowErrorMsg();
			throw new Exception();
		}
		if(p!=null) {
			String name = txtName.getText(), priceStr = txtPrice.getText(), saleStr = null;
			if(isStock)		saleStr = txtPrice.getText();
			Color color = cbColor.getValue();
			ProductType type = cbType.getValue();
			Boolean inCatalog = tglInCatalog.isSelected();
			if(name != null && !name.isEmpty() &&
					priceStr != null && !priceStr.isEmpty() &&
					color != null && 
					type != null && 
					inCatalog != null) {
				Float price = Float.parseFloat(priceStr), sale = null;
				if(isStock){
					if(saleStr == null || !saleStr.isEmpty()) {
						Context.mainScene.ShowErrorMsg();
						throw new Exception();
					}
					else
						sale = Float.parseFloat(saleStr);
				}
				try {
					String imgName;
					byte[] barr;
					if(imgFile == null) {
						imgName = p.getImageName();
						barr = p.getMybytearray();
					}
					else {
						imgName=imgFile.getName();
						barr = Context.fac.product.insertImageToByteArr(imgFile);
					}
					p = new Product(p.getPrdID(),
							name, type, price, color, inCatalog, imgName, barr);
					Context.fac.product.update(p);
					
					//============Do update for stock================
					
					//===============================================
				}catch (NumberFormatException e) {
					Context.mainScene.setMessage("Price and sale % must be decimal numbers");
				}
			}
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initComboBoxes();
	}
	
	private void initComboBoxes() {
		cbType.setItems(FXCollections.observableArrayList(ProductType.values()));
		cbColor.setItems(FXCollections.observableArrayList(Color.values()));
		/*setComboBoxToCenter(cbType);
		setComboBoxToCenter(cbColor);
		setComboBoxToCenter(cbProducts);*/
		setTextFieldSizeToContent();
		setProductsInCB();
		tglInCatalog.selectedProperty().addListener((observable,oldValue,newValue)->toggleChanged());
	}
	
	private <T> void setComboBoxToCenter(JFXComboBox<T> cb) {
		cb.setButtonCell(new ListCell<T>() {
		    @Override
		    public void updateItem(T item, boolean empty) {
		        super.updateItem(item, empty);
		        if (item != null) {
		            setText(item.toString());
		            setAlignment(Pos.CENTER);
		            Insets old = getPadding();
		            setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
		        }
		    }
		});
		
		cb.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
		    @Override
		    public ListCell<T> call(ListView<T> list) {
		        return new ListCell<T>() {
		            @Override
		            public void updateItem(T item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item != null) {
		                    setText(item.toString());
		                    setAlignment(Pos.CENTER);
		                    setPadding(new Insets(3, 3, 3, 0));
		                }
		            }
		        };
		    }
		});
	}
	
	private void setTextFieldSizeToContent() {
		textFields = new ArrayList<>();
		textFields.add(txtID);
		textFields.add(txtName);
		textFields.add(txtPrice);
		textFields.add(txtSale);
		for (JFXTextField textField : textFields) {
			textField.textProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	textField.setMinWidth(50);
			    	textField.setPrefWidth(50);
			    	textField.setMaxWidth(200);
			    	Integer len = Math.max(textField.getPromptText().length(), textField.getText().length());
			        textField.setPrefWidth(len * 7);
			        
			    }
			});
		}
	}

	public void browseImage() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files","*.png", "*.jpg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        imgFile = fileChooser.showOpenDialog(ParentGUIController.primaryStage);
        if(imgFile != null) {
        	this.imgImage.setImage(new Image("file:"+imgFile.getAbsolutePath()));
        	fileChooser.setInitialDirectory(imgFile.getParentFile());
        }
	}
	
	public void toggleChanged() {
		String text = "Not in Catalog";
		javafx.scene.paint.Color color = javafx.scene.paint.Color.RED;
		if(tglInCatalog.isSelected()) {
			text = "In Catalog";
			color=javafx.scene.paint.Color.ORANGE;
		}
		tglInCatalog.setText(text);
		tglInCatalog.setTextFill(color);
	}
}
