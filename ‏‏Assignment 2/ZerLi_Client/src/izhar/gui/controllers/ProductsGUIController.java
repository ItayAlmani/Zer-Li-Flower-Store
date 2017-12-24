package izhar.gui.controllers;

import java.util.ArrayList;

import entities.Product;
import gui.controllers.ParentGUIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public abstract class ProductsGUIController extends ParentGUIController{

	private @FXML ImageView[] imgImages;
	private @FXML Label[] lblNames;
	private @FXML Label[] lblPrices;

	/**
	 * 
	 * @param items
	 */
	public abstract void productsToGUI(ArrayList<Product> Products);

}