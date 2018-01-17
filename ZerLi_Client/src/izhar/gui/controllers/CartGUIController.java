package izhar.gui.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import common.Context;
import entities.Order;
import entities.ProductInOrder;
import entities.Store;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class CartGUIController extends ProductsPresentationGUIController {
	public static ArrayList<ProductInOrder> allPIOS, notZeroPIOS;
	private @FXML JFXButton btnOrderNow;

	/** the store of the order */
	private Store store;

	public static Float ordPrice = 0f;
	public static boolean cartEmpty = true;

	protected void getProducts() {
		Order ord = Context.order;
		if (ord != null) {
			if (ord.getDelivery() != null && ord.getDelivery().getStore() != null)
				store = ord.getDelivery().getStore();
			else {
				Context.mainScene.ShowErrorMsg();
				return;
			}
			if (ord.getProducts() != null)
				setPIOs(ord.getProducts());
			else {
				try {
					Context.fac.prodInOrder.getPIOsByOrder(ord.getOrderID());
				} catch (IOException e) {
					System.err.println("View Catalog");
					e.printStackTrace();
					Context.mainScene.ShowErrorMsg();
				}
			}
		}
	}

	private void checkCartEmpty() {
		if (cartEmpty) {
			if (firstPagination == false)
				pagination.setPageFactory((pInd) -> null);
			pagination.setVisible(false);
			lblTitleFPrice.setVisible(false);
			lblFinalPrice.setText("");
			Context.mainScene.setMessage("Cart is EMPTY!");
			btnOrderNow.setDisable(true);
		} else {
			pagination.setVisible(true);
			lblFinalPrice.setText("");
			lblTitleFPrice.setVisible(true);
			btnOrderNow.setDisable(false);
		}
	}

	public static boolean firstPagination = false;

	public void setPIOs(ArrayList<ProductInOrder> prds) {
		if (firstPagination == true)
			firstPagination = false;
		try {
			Context.fac.prodInOrder.updatePricesByStock(prds, store);
		} catch (Exception e) {
			System.err.println("Found pio with no match to stoke : " + e.getMessage());
			Context.mainScene.ShowErrorMsg();
			return;
		}
		Context.order.setProducts(prds);
		initGrids(prds);
		int i = 0;
		if (pagination == null) {
			pagination = new Pagination(notZeroPIOS.size(), 0);
			firstPagination = true;
		}
		ordPrice = Context.order.getFinalPrice();
		if (notZeroPIOS != null && !notZeroPIOS.isEmpty()) {
			cartEmpty = false;
			for (ProductInOrder p : notZeroPIOS) {
				setVBox(i, p, updateQuantity());
				i++;
			}
		} else
			cartEmpty = true;
		if (Platform.isFxApplicationThread())
			updateView();
		else
			Platform.runLater(() -> updateView());
	}

	private void updateView() {
		if (cartEmpty == true) {
			if (Platform.isFxApplicationThread())
				checkCartEmpty();
			else
				Platform.runLater(() -> checkCartEmpty());
		} else {
			setLblFinalPrice();
			Context.mainScene.setMessage("");
		}
		if (!vbox.getChildren().contains(pagination))
			vbox.getChildren().add(0, pagination);
		vbox.setAlignment(Pos.CENTER);
		// vbox.getScene().getWindow().sizeToScene();
		vbox.getStylesheets().add(getClass().getResource("/gui/css/ParentCSS.css").toExternalForm());
	}

	private EventHandler<ActionEvent> updateQuantity() {
		return (event) -> {
			Button btn = (Button) event.getSource();
			ProductInOrder pio = (ProductInOrder) btn.getUserData();
			int gridInx = (int) btn.getParent().getUserData();
			Integer quantity = null;
			try {
				quantity = spnShowQuantity[gridInx].getValue();
			} catch (NumberFormatException e) {
				Context.mainScene.setMessage("Quantity not number");
				return;
			}

			float oldFinalPrice = pio.getFinalPrice();
			pio.setQuantity(quantity);
			pio.setFinalPrice();
			try {
				Context.fac.prodInOrder.updatePriceOfPIO(pio);
				lblShowPrice[gridInx].setText(pio.getFinalPriceAsString());
				ordPrice = ordPrice - oldFinalPrice + pio.getFinalPrice();
				setLblFinalPrice();
				Context.fac.order.calcFinalPriceOfOrder(Context.order);
				if (quantity == 0) {
					notZeroPIOS.remove(gridInx);
					grids[gridInx] = new GridPane();
					if (vbox.getChildren().contains(pagination))
						vbox.getChildren().remove(pagination);
					pagination = new Pagination(notZeroPIOS.size(), 0);
					setPIOs(allPIOS);
				}
			} catch (IOException e) {
				System.err.println("prodInOrder.updatePriceOfPIO query failed");
				e.printStackTrace();
			}
		};
	}

	private void setLblFinalPrice() {
		if (ordPrice == Math.round(ordPrice))
			lblFinalPrice.setText(((Integer) Math.round(ordPrice)).toString() + "¤");
		else
			lblFinalPrice.setText(ordPrice.toString() + "¤");
	}

	private void initGrids(ArrayList<ProductInOrder> prds) {
		components.clear();
		allPIOS = prds;
		notZeroPIOS = Context.fac.prodInOrder.getPIOsNot0Quantity(prds);

		/* The labels which indicates the title of each data of all products */
		lblTitleQuantity = new Label[notZeroPIOS.size()];

		/* The quantity of all products */
		spnShowQuantity = new Spinner[notZeroPIOS.size()];

		initArrays(notZeroPIOS.size());
	}

	public void createOrder() {
		if (notZeroPIOS != null && !notZeroPIOS.isEmpty()) {
			Context.order.setProducts(allPIOS);
			Context.fac.order.calcFinalPriceOfOrder(Context.order);

			Context.mainScene.loadGUI("DeliveryGUI", false);
		} else
			Context.mainScene.setMessage("Cart is empty!");
	}
}