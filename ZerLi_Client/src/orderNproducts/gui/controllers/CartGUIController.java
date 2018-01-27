package orderNproducts.gui.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import common.Context;
import common.gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import orderNproducts.entities.Order;
import orderNproducts.entities.Product;
import orderNproducts.entities.ProductInOrder;
import orderNproducts.entities.Stock;
import orderNproducts.entities.Store;

public class CartGUIController extends ProductsPresentationGUIController {
	public static ArrayList<ProductInOrder> allPIOS, notZeroPIOS;
	private @FXML JFXButton btnOrderNow;

	/** the store of the order */
	private Store store;

	public static Float ordPrice = 0f;
	public static boolean cartEmpty = true;
	public static boolean firstPagination = false;

	@Override
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
					Context.mainScene.setMenuPaneDisable(true);
					Context.fac.prodInOrder.getPIOsByOrder(ord.getOrderID());
				} catch (IOException e) {
					System.err.println("View Catalog");
					e.printStackTrace();
					Context.mainScene.ShowErrorMsg();
				}
			}
		}
	}

	/** checks if cart empty and set the GUI by it */
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

	/** The {@link ProductInOrder}s that asked in {@link #getProducts()} */
	public void setPIOs(ArrayList<ProductInOrder> prds) {
		Context.mainScene.setMenuPaneDisable(false);
		try {
			Context.fac.prodInOrder.updatePricesByStock(prds, store);
		} catch (Exception e) {
			System.err.println("Found pio with no match to stoke : " + e.getMessage());
			Context.mainScene.ShowErrorMsg();
			return;
		}
		Context.order.setProducts(prds);
		
		int i = 0;
		initGrids(prds);
		ordPrice = Context.order.getFinalPrice();
		if (notZeroPIOS != null && !notZeroPIOS.isEmpty()) {
			cartEmpty = false;
			for (ProductInOrder p : notZeroPIOS) {
				Float newPrice;
				Stock stk;
				try {
					stk = Context.fac.stock.getStockByProductFromStore(store, p.getProduct());
					if(stk == null) {
						System.err.println("Stock is null");
						continue;
					}
					
					newPrice = p.getFinalPrice();//Context.fac.product.getPriceWithSubscription(Context.order,stk.getProduct(), stk.getPriceAfterSale(), Context.getUserAsCustomer());
					setVBox(i, p, 
							stk.getPriceAfterSale().equals(newPrice)?null:newPrice, 
							updateQuantity(stk));
					i++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else
			cartEmpty = true;
		if (Platform.isFxApplicationThread())
			updateView();
		else
			Platform.runLater(() -> updateView());
	}

	/** updating the screen after changes */
	private void updateView() {
		if (cartEmpty == true) {
			if (Platform.isFxApplicationThread())
				checkCartEmpty();
			else
				Platform.runLater(() -> checkCartEmpty());
		} else {
			lblFinalPrice.setText(priceToStr(ordPrice));
			Context.mainScene.setMessage("");
		}
		if (!vbox.getChildren().contains(pagination))
			vbox.getChildren().add(0, pagination);
		vbox.setAlignment(Pos.CENTER);
		vbox.getStylesheets().add(getClass().getResource(ParentGUIController.css_path+"ParentCSS.css").toExternalForm());
	}

	/**
	 * the {@link EventHandler} of the Button which updates the quantity
	 * @param stock update the stock after adding more quantity of the pio
	 * @return the {@link EventHandler}
	 */
	private EventHandler<ActionEvent> updateQuantity(Stock stock) {
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
			int quantity_before_change = pio.getQuantity();
			pio.setQuantity(quantity);
			pio.setFinalPrice();
			try {
				Context.fac.prodInOrder.updatePriceOfPIO(pio);
				lblShowPrice[gridInx].setText(pio.getFinalPriceAsString());
				ordPrice = ordPrice - oldFinalPrice + pio.getFinalPrice();
				lblFinalPrice.setText(priceToStr(ordPrice));
				Context.fac.order.calcFinalPriceOfOrder(Context.order);
				if (quantity == 0) {
					notZeroPIOS.remove(gridInx);
					grids[gridInx] = new GridPane();
					if (vbox.getChildren().contains(pagination))
						vbox.getChildren().remove(pagination);
					pagination = new Pagination(notZeroPIOS.size(), 0);
					setPIOs(allPIOS);
				}
				stock.setQuantity(stock.getQuantity()+quantity_before_change-quantity);
				Context.fac.stock.update(stock);
			} catch (IOException e) {
				System.err.println("prodInOrder.updatePriceOfPIO query failed");
				e.printStackTrace();
			}
		};
	}

	/**
	 * 
	 * @param prds
	 */
	private void initGrids(ArrayList<ProductInOrder> prds) {
		components.clear();
		allPIOS = prds;
		notZeroPIOS = Context.fac.prodInOrder.getPIOsNot0Quantity(prds);

		/* The labels which indicates the title of each data of all products */
		lblTitleQuantity = new Label[notZeroPIOS.size()];

		/* The quantity of all products */
		spnShowQuantity = new Spinner[notZeroPIOS.size()];

		if (firstPagination == true)
			firstPagination = false;
		
		if (pagination == null) {
			pagination = new Pagination(notZeroPIOS.size(), 0);
			firstPagination = true;
		}
		
		initArrays(notZeroPIOS.size());
	}

	/** The {@link EventHandler} of the {@link #btnOrderNow}.
	 * Invoke onAction and forwarding to the {@link DeliveryGUIController}*/
	public void createOrder() {
		if (notZeroPIOS != null && !notZeroPIOS.isEmpty()) {
			Context.order.setProducts(allPIOS);
			Context.fac.order.calcFinalPriceOfOrder(Context.order);
			Context.mainScene.loadDelivery();
		} 
		else {
			Context.mainScene.setMenuPaneDisable(false);
			Context.mainScene.setMessage("Cart is empty!");
		}
	}

	@Override
	protected void getProducts(ArrayList<Product> prds) {}
}