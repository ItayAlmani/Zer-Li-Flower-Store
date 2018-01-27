package orderNproducts.gui.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import common.Context;
import common.gui.controllers.ParentGUIController;
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
import orderNproducts.entities.Product;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Store;
import orderNproducts.entities.Product.Color;
import orderNproducts.entities.Product.ProductType;
import usersInfo.entities.Customer;
import usersInfo.entities.StoreWorker;
import usersInfo.entities.User;
import usersInfo.entities.User.UserType;

public class UpdateCatalogGUIController implements Initializable{

	private @FXML JFXComboBox<Stock> cbStocks;
	private JFXComboBox<Product> cbProducts = null;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXButton btnUpdate, btnAdd;
	private @FXML JFXTextField txtID, txtName, txtPrice, txtSale, txtQuantity;
	private ArrayList<JFXTextField> textFields;
	private @FXML VBox paneItem, paneScene, vbChain, vbImg;
	private @FXML HBox hbSale, hbStore, hbCBAndBtn;
	private @FXML ImageView imgImage;
	private @FXML JFXToggleButton tglInCatalog;
	private @FXML Label lblClickImg;
	private FileChooser fileChooser = new FileChooser();
	private File imgFile = null;
	private Product prdAdded = null;
	
	/** 
	 * true <=> the add button clicked.<br>
	 * false <=> a product selected from the combo box.
	 */
	private boolean is_product_for_add;
	
	/** if store worker/manager <=> true.<br>
	 * if chain store worker/manager <=> false.
	 */
	private boolean isStoreWorker;
	
	public void setProductsInCB() {
		User u = Context.getUser();
		if(u.getPermissions().equals(UserType.StoreWorker) ||
			u.getPermissions().equals(UserType.StoreManager)) {
			isStoreWorker=true;
			try {
				StoreWorker sw = Context.getUserAsStoreWorker();
				Store s = sw.getStore();
				String title = "Update stock of store";
				if(s!=null)
					title += " "+s.getName();
				Context.mainScene.setTitle(title);
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
			isStoreWorker = false;
			if(Platform.isFxApplicationThread())
				rearrangeComboBoxes();
			else
				Platform.runLater(()->rearrangeComboBoxes());
			try {
				Context.mainScene.setMenuPaneDisable(true);
				Context.fac.product.getAllProducts();
			} catch (IOException e) {
				Context.mainScene.ShowErrorMsg();
				return;
			}
		}
	}
	
	private void rearrangeComboBoxes() {
		//Change from ComboBox of Stock to Products
		cbProducts = new JFXComboBox<>();
		int ind = hbCBAndBtn.getChildren().indexOf(cbStocks);
		hbCBAndBtn.getChildren().add(ind, cbProducts);
		cbProducts.setOnAction(cbStocks.getOnAction());
		cbProducts.setPromptText(cbStocks.getPromptText());
		hbCBAndBtn.getChildren().remove(cbStocks);
		
		btnUpdate.setOnAction((e)->{
			try {
				updateProd();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		cbStocks = null;
		if(!paneItem.getChildren().remove(hbStore))
			hbStore.setVisible(false);
		vbChain.setDisable(false);
		lblClickImg.setVisible(true);
		vbImg.setDisable(false);
		btnAdd.setVisible(true);
		btnUpdate.setText("Send");
	}
	
	public void setProducts(ArrayList<Product> prds) {
		Context.mainScene.setMenuPaneDisable(false);
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
		is_product_for_add=false;
		Context.mainScene.clearMsg();
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
			
			this.txtSale.setText(new DecimalFormat("##.##").format(s.getSalePercetage()*100f));
		else {
			this.txtSale.setText("0");
			Context.mainScene.ShowErrorMsg();
			System.err.println("Sale percentage is null - write the StoreWorkerCtrl");
		}
		this.txtQuantity.setText(String.valueOf(s.getQuantity()));
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
	
	public void updateStock() throws Exception {
		Stock stk = this.cbStocks.getValue();
		if(stk==null) {
			Context.mainScene.ShowErrorMsg();
			throw new Exception();
		}
		String saleStr = txtSale.getText(), 
				quStr = txtQuantity.getText();
		Float sale;
		Integer qu;
		try {
			if(saleStr == null || saleStr.isEmpty()) {
				Context.mainScene.ShowErrorMsg();
				throw new Exception();
			}
			else {
				sale = Float.parseFloat(saleStr);
				sale/=100f;
			}
			
			if(quStr == null || quStr.isEmpty()) {
				Context.mainScene.ShowErrorMsg();
				throw new Exception();
			}
			else
				qu = Integer.parseInt(quStr);
			
			stk.setSalePercetage(sale);
			stk.setQuantity(qu);
			Context.fac.stock.update(stk);
		}catch (NumberFormatException e) {
			Context.mainScene.setMessage("Sale % and quantity must be numbers");
		}catch (IOException e) {
			Context.mainScene.ShowErrorMsg();
		}
	}
	
	public void updateProd() throws Exception {
		Context.mainScene.clearMsg();
		Product p = null;
		if(is_product_for_add)	//asked to add new product
			p = new Product();
		else if(this.cbProducts != null)
			p = this.cbProducts.getValue();
		else {
			Context.mainScene.ShowErrorMsg();
			throw new Exception();
		}
		if(p!=null) {
			String name = txtName.getText(), priceStr = txtPrice.getText();
			Color color = cbColor.getValue();
			ProductType type = cbType.getValue();
			Boolean inCatalog = tglInCatalog.isSelected();
			if(name != null && !name.isEmpty() &&
					priceStr != null && !priceStr.isEmpty() &&
					color != null && 
					type != null && 
					inCatalog != null) {
				Float price = Float.parseFloat(priceStr);
				try {
					String imgName;
					byte[] barr;
					
					//a image already exists for the picture and hasn't changed
					if(imgFile == null && p.getImageName() != null && p.getMybytearray() != null) {
						imgName = p.getImageName();
						barr = p.getMybytearray();
					}
					else if(imgFile != null){
						imgName=imgFile.getName();
						barr = Context.fac.product.insertImageToByteArr(imgFile);
					}
					else {
						Context.mainScene.clearMsg();
						Context.mainScene.setMessage("Must choose picture");
						return;
					}
					p = new Product(p.getPrdID(),
							name, type, price, color, inCatalog, imgName, barr);
					if(is_product_for_add) {
						Context.mainScene.setMenuPaneDisable(true);
						Context.fac.product.add(p, true);
						prdAdded = p;
						cbProducts.getItems().add(p);
						paneItem.setVisible(false);
					}
					else
						Context.fac.product.update(p);
				}catch (NumberFormatException e) {
					Context.mainScene.setMessage("Price must be decimal numbers");
				}
			}
			else {
				Context.mainScene.clearMsg();
				Context.mainScene.setMessage("Some fields are empty!");
			}
		}
	}
	
	public void setProductID(BigInteger id) {
		Context.mainScene.setMenuPaneDisable(false);
		prdAdded.setPrdID(id);
		Context.mainScene.setMessage(prdAdded + " added successfully");
		try {
			Context.fac.stock.addProductToAllStores(prdAdded);
		} catch (IOException e) {
			e.printStackTrace();
			Context.mainScene.ShowErrorMsg();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbType.setItems(FXCollections.observableArrayList(ProductType.values()));
		cbColor.setItems(FXCollections.observableArrayList(Color.values()));
		setTextFieldSizeToContent();
		setProductsInCB();
		tglInCatalog.selectedProperty().addListener((observable,oldValue,newValue)->toggleChanged());
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
        	//fileChooser.setInitialDirectory(imgFile.getParentFile());
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
	
	public void addProd() {
		clearAllControls();
		is_product_for_add=true;
		paneItem.setVisible(true);
	}
	
	private void clearAllControls() {
		Context.mainScene.clearMsg();
		cbColor.setValue(null);
		if(cbProducts != null)
			cbProducts.setValue(null);
		else if(cbStocks != null)
			cbStocks.setValue(null);
		cbType.setValue(null);
		this.imgImage.setImage(null);
		this.txtID.setText("");
		this.txtName.setText("");
		this.cbType.setValue(null);
		this.cbColor.setValue(null);
		this.txtPrice.setText("");
		this.tglInCatalog.setSelected(false);
		if(isStoreWorker)
			this.txtSale.setText("");
	}
}
