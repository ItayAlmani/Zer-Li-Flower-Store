package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import org.controlsfx.glyphfont.FontAwesome.Glyph;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;

import common.Context;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.PaymentAccount;
import entities.Order.OrderStatus;
import gui.controllers.ParentGUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;

public class PaymentGUIController implements Initializable {

	private @FXML JFXRadioButton rbCredit, rbCash;
	private @FXML Label lblPayMsg, lblFinalPrice;
	private @FXML JFXButton btnPay;
	private @FXML JFXProgressBar piBill;
	private @FXML JFXTextArea txtGreeting;
	private @FXML ToggleGroup tGroup;
	private @FXML FontAwesomeIconView icnCash;
	private @FXML OctIconView icnCreditCard;
	private @FXML MaterialDesignIconView icnNext;
	private String priceBeforeDiscount;
	
	private String getFinalPriceAsStr(Float ordPrice) {
    	if(ordPrice == Math.round(ordPrice))
			return ((Integer)Math.round(ordPrice)).toString()+ "¤";
		else
			return ordPrice.toString()+ "¤";
    }

	public void selectedCreditCard() {
		lblPayMsg.setText("");
		btnPay.setText("Pay Now!");
		icnNext.setGlyphName(MaterialDesignIcon.CUBE_SEND.toString());
		
		Paint ccColor = Color.ORANGE, cashColor = Color.RED;
		icnCreditCard.setFill(ccColor);
		rbCredit.setTextFill(ccColor);
		
		icnCash.setFill(cashColor);
		rbCash.setTextFill(cashColor);
		btnPay.setVisible(true);
	}

	public void selectedCash() {
		lblPayMsg.setText("Order won't be complete until payment");
		btnPay.setText("Next");
		icnNext.setGlyphName(MaterialDesignIcon.ARROW_RIGHT_BOLD_CIRCLE.toString());
		
		Paint ccColor = Color.RED, cashColor = Color.ORANGE;
		icnCreditCard.setFill(ccColor);
		rbCredit.setTextFill(ccColor);
		
		icnCash.setFill(cashColor);
		rbCash.setTextFill(cashColor);
		piBill.setVisible(false);
		btnPay.setDisable(false);
		btnPay.setVisible(true);
	}

	public void pay() {
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
				boolean billResponse = true;
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
							payWithCC();
						}
					}
				});
			}
		});
		th.setDaemon(true);
		th.start();		
	}
	
	private void payWithCC() {
		Order ord = Context.order;
		if(tGroup.getSelectedToggle().equals(rbCredit)) {
			ord.setOrderStatus(OrderStatus.Paid);
			ord.setPaymentMethod(entities.Order.PayMethod.CreditCard);
		}
		else
			ord.setOrderStatus(OrderStatus.WaitingForCashPayment);
		if(txtGreeting.getText().isEmpty()==false)
			ord.setGreeting(txtGreeting.getText());
		else
			ord.setGreeting("");
		try {
			Context.fac.order.add(ord,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setOrderID(BigInteger id) {
			try {
				if(Context.order!=null && Context.order.getOrderID()!=null)
					Context.fac.stock.update(Context.order);
			} catch (IOException e) {
				e.printStackTrace();
			}
		loadNextWindow();
	}
	
	public void loadNextWindow() {
		Context.mainScene.loadGUI("OrderGUI", false);
	}

	public void back() {
		Context.mainScene.loadGUI("OrderTimeGUI", false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tGroup = new ToggleGroup();
		rbCredit.setUserData("CreditCard");
		rbCredit.setToggleGroup(tGroup);
		rbCash.setUserData("Cash");
		rbCash.setToggleGroup(tGroup);
		priceBeforeDiscount=Context.order.getFinalPriceAsString();
		Customer cust;
		try {
			cust = Context.getUserAsCustomer();
			DeliveryDetails del = Context.order.getDelivery();
			PaymentAccount pa = Context.fac.paymentAccount.getPaymentAccountOfStore(cust.getPaymentAccounts(), del.getStore());
			Float refundAmount = pa.getRefundAmount(); 
			if(refundAmount>0) {
				Context.mainScene.setMessage("We have credit in the refund section");
				lblFinalPrice.setText(priceBeforeDiscount + "-" + 
						getFinalPriceAsStr(refundAmount) + "=" +
						Context.order.getFinalPriceAsString());
			}
			else
				lblFinalPrice.setText(priceBeforeDiscount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}