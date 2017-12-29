package izhar.gui.controllers;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import common.Context;
import entities.Order;
import entities.ProductInOrder;
import entities.Transaction;
import entities.Transaction.PayMethod;
import entities.Order.OrderStatus;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

public class PaymentGUIController extends ParentGUIController {

	private @FXML RadioButton rbCredit, rbCash;
	private @FXML Label lblPayMsg;
	private @FXML Button btnPay;
	private @FXML ProgressIndicator piBill;
	private @FXML ToggleGroup tGroup;
	private @FXML TextArea txtGreeting;

	public void selectedCreditCard() {
		btnPay.setVisible(true);
		piBill.setVisible(true);
		lblPayMsg.setText("Billing - waiting for confiramtion");
		piBill.progressProperty().addListener((ov, oldValue, newValue) -> {
		     Text text = (Text) piBill.lookup(".percentage");
		     if(text!=null && text.getText().equals("Done")){
		        text.setText("");
		        piBill.setPrefWidth(text.getLayoutBounds().getWidth());
		        text.setStyle("-fx-text-inner-color: blue;");
		     }
		});
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				double prog = 0;
				piBill.setProgress(prog);
				while (prog < 1.0) {
					double toAdd =0.3f * new Random().nextDouble(); 
					if(prog+toAdd>=1.0)	prog=1;
					else				prog+=toAdd;
					piBill.setProgress(prog);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						System.err.println("Thread problem in PaymentGUI");
						e.printStackTrace();
					}
				}
				boolean billResponse = Context.fac.customer.billCreditCardOfCustomer(Context.getUserAsCustomer(), Context.order.getFinalPrice());
				((Text)piBill.lookup(".percentage")).setText(billResponse==true?"Confirmed":"Denied");
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(billResponse==false) {
							btnPay.setDisable(true);
							lblPayMsg.setText("Credit card denied - try again later or pay with cash");
						}
						else {
							btnPay.setDisable(false);
							lblPayMsg.setText("");
						}
						
					}
				});
			}
		});
		th.setDaemon(true);
		th.start();		
	}

	public void selectedCash() {
		lblPayMsg.setText("Order won't be complete until payment");
		btnPay.setText("Next");
		piBill.setVisible(false);
		btnPay.setDisable(false);
		btnPay.setVisible(true);
	}

	public void pay() {
		Order ord = Context.order;
		if(tGroup.getSelectedToggle().getUserData().equals("CreditCard")) {
			ord.setOrderStatus(OrderStatus.Paid);
			Transaction t = new Transaction(PayMethod.CreditCard, Context.order);
			ord.setTransaction(t);
		}
		else
			ord.setOrderStatus(OrderStatus.WaitingForCashPayment);
		if(txtGreeting.getText().isEmpty()==false)
			ord.setGreeting(txtGreeting.getText());
		Context.fac.orderProcess.updateFinilizeOrder(ord);
	}

	public void back() {
		try {
			loadGUI("OrderTimeGUI", false);
		} catch (Exception e) {
			lblMsg.setText("Loader failed");
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Context.currentGUI = this;

		tGroup = new ToggleGroup();
		rbCredit.setUserData("CreditCard");
		rbCredit.setToggleGroup(tGroup);
		rbCash.setUserData("Cash");
		rbCash.setToggleGroup(tGroup);
		
	}

}