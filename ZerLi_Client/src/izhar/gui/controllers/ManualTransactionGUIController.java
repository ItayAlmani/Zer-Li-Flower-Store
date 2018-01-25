package izhar.gui.controllers;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;

import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.OrderType;
import entities.ProductInOrder;
import entities.Stock;
import entities.Store;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ManualTransactionGUIController implements Initializable {
	private @FXML JFXButton btnSend;
	private @FXML VBox vbox;
	private ObservableList<Customer> custList;
	private @FXML JFXComboBox<Customer> cbCustomers;
	private @FXML JFXTextField txtFilter;
	private TreeItem<ProductInOrder> root;
	private @FXML VBox paneProducts;
	private @FXML JFXTreeTableView<ProductInOrder> ttvProducts;
	private Store store;
	private Order order = new Order();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ParentGUIController.currentGUI = this;

		vbox.setSpacing(5);
		try {
			Store s = Context.getUserAsStoreWorker().getStore();
			if (s == null || s.getStoreID() == null) {
				Context.mainScene.ShowErrorMsg();
				return;
			}

			this.store = s;

			// set disable until answer
			Context.mainScene.setMenuPaneDisable(true);
			Context.fac.customer.getAllCustomersOfStore(store.getStoreID());
		} catch (Exception e) {
			Context.mainScene.ShowErrorMsg();
			e.printStackTrace();
		}
		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> filterChanged(newValue));
		root = new TreeItem<>();
		ttvProducts.setRoot(root);
		addColumn("Product");
		addColumn("Quantity");
		addColumn("Price");
		addColumn("Reset Quantity");
		ttvProducts.setShowRoot(false);
	}

	private void filterChanged(String filter) {
		if (filter.isEmpty()) {
			ttvProducts.setRoot(root);
		} else {
			TreeItem<ProductInOrder> filteredRoot = new TreeItem<>();
			filter(root, filter, filteredRoot);
			ttvProducts.setRoot(filteredRoot);
		}
	}

	private void filter(TreeItem<ProductInOrder> root, String filter, TreeItem<ProductInOrder> filteredRoot) {
		for (TreeItem<ProductInOrder> child : root.getChildren()) {
			TreeItem<ProductInOrder> filteredChild = new TreeItem<>();
			filteredChild.setValue(child.getValue());
			filteredChild.setExpanded(true);
			filter(child, filter, filteredChild);
			if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter))
				filteredRoot.getChildren().add(filteredChild);
		}
	}

	private boolean isMatch(ProductInOrder value, String filter) {
		return value.getProduct().getName().toLowerCase().contains(filter.toLowerCase());
	}

	private void setup() {
		root.getChildren().clear();
		for (Stock s : store.getStock()) {
			s.getProduct().setPrice(s.getPriceAfterSale());
			createItem(root, s, 0, 0f);
		}
	}

	private TreeItem<ProductInOrder> createItem(TreeItem<ProductInOrder> parent, Stock stock, Integer quantity,
			Float price) {
		TreeItem<ProductInOrder> item = new TreeItem<>();
		ProductInOrder value = new ProductInOrder(stock.getProduct(), quantity, Context.order.getOrderID());
		item.setValue(value);
		parent.getChildren().add(item);
		item.setExpanded(true);
		return item;
	}

	protected void addColumn(String label) {
		final String label_lower_case = label.toLowerCase();
		JFXTreeTableColumn<ProductInOrder, String> column = new JFXTreeTableColumn<>(label);
		column.setPrefWidth(150);
		column.setCellValueFactory((JFXTreeTableColumn.CellDataFeatures<ProductInOrder, String> param) -> {
			ObservableValue<String> result = new SimpleStringProperty("");
			ProductInOrder pio = param.getValue().getValue();
			if (param.getValue().getValue() != null) {
				switch (label_lower_case) {
				case "product":
					result = new SimpleStringProperty("" + pio.getProduct().toString());
					break;
				case "quantity":
					result = new SimpleStringProperty("" + pio.getQuantity());
					break;
				case "price":
					result = new SimpleStringProperty("" + pio.getFinalPriceAsString());
					break;
				default:
					break;
				}
			}
			return result;
		});
		if (label_lower_case.equals("quantity")) {
			column.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
			column.setOnEditCommit(new EventHandler<JFXTreeTableColumn.CellEditEvent<ProductInOrder, String>>() {

				@Override
				public void handle(CellEditEvent<ProductInOrder, String> event) {
					TreeItem<ProductInOrder> item = event.getRowValue();
					ProductInOrder pio = item.getValue();
					try {
						Integer quan = Integer.parseInt(event.getNewValue());
						pio.setQuantity(quan);
						pio.setFinalPrice();
						ttvProducts.refresh();
						boolean order_contains_pio = order.getProducts().contains(pio);
						if(quan==0) {
							if(order_contains_pio)
								order.getProducts().remove(pio);
						}
						else {
							if(!order_contains_pio)
								order.addPIOToProducts(pio);
						}
					} catch (NumberFormatException e) {
						Context.mainScene.setMessage("Quantity must be a number");
					}
				}
			});
		} else if (label_lower_case.equals("reset quantity")) {
			column.setCellFactory(
					new Callback<TreeTableColumn<ProductInOrder, String>, TreeTableCell<ProductInOrder, String>>() {

						@Override
						public JFXTreeTableCell<ProductInOrder, String> call(
								TreeTableColumn<ProductInOrder, String> param) {
							return setCellWithButton();
						}
					});
		}
		column.resizableProperty().set(true);
		ttvProducts.getColumns().add(column);
	}

	private JFXTreeTableCell<ProductInOrder, String> setCellWithButton() {
		final JFXTreeTableCell<ProductInOrder, String> cell = new JFXTreeTableCell<ProductInOrder, String>() {

			final JFXButton btn = new JFXButton("Reset Quantity");

			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
					setText(null);
				} else {
					btn.setRipplerFill(Color.ORANGE);
					btn.setButtonType(ButtonType.RAISED);
					btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							ProductInOrder pio = getTreeTableView().getTreeItem(getIndex()).getValue();
							pio.setQuantity(0);
							pio.setFinalPrice();
							ttvProducts.refresh();
						}
					});
					setGraphic(btn);
					setText(null);
				}
			}
		};
		return cell;
	}

	public void setCustomers(ArrayList<Customer> customers) {
		custList = FXCollections.observableArrayList(customers);
		Context.mainScene.setMenuPaneDisable(false);
		if (Platform.isFxApplicationThread()) {
			cbCustomers.setItems(custList);
			//cbCustomers.setPromptText("Choose customer");
			cbCustomers.setDisable(false);
		} else {
			Platform.runLater(() -> {
				cbCustomers.setItems(custList);
				//cbCustomers.setPromptText("Choose customer");
				cbCustomers.setDisable(false);
			});
		}
	}

	/** the onAction handler of {@link #cbCustomers} */
	public void customerSelected() {
		paneProducts.setVisible(false);
		if (cbCustomers.getValue() == null)
			return;
		Customer c = cbCustomers.getValue();
		if (Context.order == null || Context.order.getCustomerID().equals(c.getCustomerID()) == false) {
			Context.order = null;
			Context.askOrder(c.getCustomerID(), store);
		}

		// Thread that waits until the order returns from Server
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Context.order == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
				order = Context.order;
				btnSend.setDisable(false);
				if (cbCustomers.getValue() != null && cbCustomers.getValue().getCustomerID() != null)
					order.setCustomerID(cbCustomers.getValue().getCustomerID());
				paneProducts.setVisible(true);
				setup();
			}
		});
		t.start();
	}

	/** the onAction handler of {@link #btnSend} */
	public void sendNewOrder() {
		Context.mainScene.clearMsg();
		if(order.getProducts().isEmpty() || order.getFinalPrice()==0) {
			Context.mainScene.setMessage("Order must contain at least 1 product");
			return;
		}
		order.setType(OrderType.Manual);
		try {
			order.setDelivery(new DeliveryDetails(order.getOrderID(), LocalDateTime.now(), true,
					Context.getUserAsStoreWorker().getStore()));
			order.setDeliveryType(DeliveryType.Pickup);
			order.setOrderStatus(OrderStatus.Delivered);
			PaymentGUIController.cust_in_manual_order=cbCustomers.getValue();
			Context.mainScene.loadPayment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}