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
import entities.Stock;
import entities.Store;
import entities.StoreWorker;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class UpdateCatalogGUIController implements Initializable{

	private @FXML JFXComboBox<Stock> cbProducts;
	private @FXML JFXComboBox<Color> cbColor;
	private @FXML JFXComboBox<ProductType> cbType;
	private @FXML JFXButton btnUpdate;
	private @FXML JFXTextField txtID, txtName, txtPrice, txtSale;
	private ArrayList<JFXTextField> textFields;
	private @FXML VBox paneItem, paneScene;
	private @FXML ImageView imgImage;
	private @FXML JFXToggleButton tglInCatalog;
	private FileChooser fileChooser = new FileChooser();
	
	public void setProductsInCB() {
		try {
			/////////WAIT FOR GET STORE WORKER!!
			StoreWorker sw = Context.getUserAsStoreWorker();
			Store s = sw.getStore();
			if(s.getStock()!=null) {
				cbProducts.setItems(FXCollections.observableArrayList(s.getStock()));
				paneItem.setVisible(false);
			}
			else
				throw new NullPointerException("s.getStock() is null");
		} catch (Exception e) {
			System.err.println("Not store worker - is a " + Context.getUser().getPermissions());
			System.err.println(e.getMessage());
			Context.mainScene.ShowErrorMsg();
		}
	}
	
	public void showProduct() throws Exception {
		if(cbProducts.getValue()!=null)
			loadProduct(cbProducts.getValue());
	}
	
	public void loadProduct(Stock s) {
		Product p = s.getProduct();
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
		this.txtPrice.setText(p.getPriceAsString());
		this.tglInCatalog.setSelected(p.isInCatalog());
		if(s.getSalePercetage()!=null)
			this.txtSale.setText(((Float)(s.getSalePercetage()*100)).toString()+"%");
		else {
			this.txtSale.setText("");
			Context.mainScene.ShowErrorMsg();
			System.err.println("Sale percentage is null - write the StoreWorkerCtrl");
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(p.getMybytearray());
		imgImage.setImage(new Image(bais));
		imgImage.prefHeight(200);
		try {
			bais.close();
			paneItem.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProd() throws Exception {
		Stock s = this.cbProducts.getValue();
		if(s!=null) {
			Product p = s.getProduct();
			if(p!=null) {
				/*if(txtName.getText()!=null) {
					if(txtName.getText().equals(p.getName())==false) {//Name changed
						p.setName(txtName.getText());
						Context.fac.product.update(p);
						Platform.runLater(()->setProductsInCB());
					}
				}*/
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
		tglInCatalog.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleChanged();
			}
		});
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
        File file = fileChooser.showOpenDialog(ParentGUIController.primaryStage);
        if(file != null) {
        	this.imgImage.setImage(new Image("file:"+file.getAbsolutePath()));
        	fileChooser.setInitialDirectory(file.getParentFile());
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
