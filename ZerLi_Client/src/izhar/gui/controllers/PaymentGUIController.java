package izhar.gui.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;

import common.Context;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import entities.Customer;
import entities.DeliveryDetails;
import entities.Order;
import entities.Order.DeliveryType;
import entities.Order.OrderStatus;
import entities.Order.PayMethod;
import entities.PaymentAccount;
import entities.ShipmentDetails;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class PaymentGUIController implements Initializable {

	private @FXML JFXRadioButton rbCredit, rbCash;
	private @FXML Label lblPayMsg, lblFinalPrice;
	private @FXML JFXButton btnPay, btnBack;
	private @FXML JFXProgressBar piBill;
	private @FXML JFXTextArea txtGreeting;
	private @FXML ToggleGroup tGroup;
	private @FXML FontAwesomeIconView icnCash;
	private @FXML OctIconView icnCreditCard;
	private @FXML MaterialDesignIconView icnNext;
	private @FXML HBox panePaySelect;
	private PaymentAccount pa;
	private Float price_before_disc, price_after_disc, refund_before_disc, refund_after_disc;
	
	/**
	 * when it's manual order/transaction <=> cust_in_manual_order != null
	 */
	public static Customer cust_in_manual_order = null;
	private boolean is_manual_order = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tGroup = new ToggleGroup();
		rbCredit.setToggleGroup(tGroup);
		rbCash.setToggleGroup(tGroup);
		price_before_disc=Context.order.getFinalPrice();
		Customer cust;
		try {
			//if this is manual order
			if(cust_in_manual_order!=null) {
				cust=cust_in_manual_order;
				cust_in_manual_order=null;
				is_manual_order=true;
			}
			else {
				cust = Context.getUserAsCustomer();
				is_manual_order=false;
			}
			
			DeliveryDetails del = Context.order.getDelivery();
			pa = Context.fac.paymentAccount.getPaymentAccountOfStore(cust.getPaymentAccounts(),
					del.getStore());
			this.refund_before_disc = pa.getRefundAmount();
			this.price_after_disc = Context.fac.order.getFinalPriceByPAT(pa,
					Context.order,
					cust);
			//add delivery price after the discounts
			if(Context.order.getDeliveryType().equals(DeliveryType.Shipment)) {
				price_before_disc+=ShipmentDetails.shipmentPrice;
				this.price_after_disc+=ShipmentDetails.shipmentPrice;
			}
			if(refund_before_disc>0) {
				if(refund_before_disc>price_before_disc) {
					lblFinalPrice.setText(priceToString(price_before_disc) + "-" + 
							priceToString(price_before_disc) + "=" +
							priceToString(price_after_disc));
					refund_after_disc = refund_before_disc-price_before_disc;
					Context.order.setPaymentMethod(PayMethod.Refund);
					panePaySelect.setVisible(false);
					btnPay.setOnAction(e->pay());
				}
				else {
					lblFinalPrice.setText(priceToString(price_before_disc) + "-" + 
							priceToString(refund_before_disc) + "=" +
							priceToString(price_after_disc));
					refund_after_disc = 0f;
				}
				lblFinalPrice.setTooltip(new Tooltip("Price before refund amount - current refund amount = price to pay"));
				Context.mainScene.setMessage("You got "+priceToString(refund_after_disc) + " refund left");
			}
			else
				lblFinalPrice.setText(priceToString(price_before_disc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String priceToString(Float price) {
    	DecimalFormat df = new DecimalFormat("##.##");
		return df.format(price) + "¤";
    }

	public void selectedCreditCard() {
		lblPayMsg.setText("");
		lblPayMsg.setUnderline(false);
		btnPay.setText("Pay Now!");
		icnNext.setGlyphName(MaterialDesignIcon.CUBE_SEND.toString());
		
		btnPay.setOnAction(e->payWithCC());
		
		Paint ccColor = Color.ORANGE, cashColor = Color.RED;
		icnCreditCard.setFill(ccColor);
		rbCredit.setTextFill(ccColor);
		
		icnCash.setFill(cashColor);
		rbCash.setTextFill(cashColor);
		btnPay.setVisible(true);
	}

	public void selectedCash() {
		lblPayMsg.setText("Don't forget to pay!");
		lblPayMsg.setUnderline(true);
		btnPay.setOnAction(e->pay());
		
		Paint ccColor = Color.RED, cashColor = Color.ORANGE;
		icnCreditCard.setFill(ccColor);
		rbCredit.setTextFill(ccColor);
		
		icnCash.setFill(cashColor);
		rbCash.setTextFill(cashColor);
		piBill.setVisible(false);
		btnPay.setOnAction((event)->pay());
		btnPay.setDisable(false);
		btnPay.setVisible(true);
	}

	public void payWithCC() {
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
		billingProgress();	
	}
	
	private void setByBillResponse(boolean billResponse) {
		if(billResponse==false) {
			btnPay.setDisable(true);
			lblPayMsg.setText("Credit card denied - try again later or pay with cash");
		}
		else {
			btnPay.setDisable(false);
			lblPayMsg.setText("");
			pay();
		}
	}
	
	public void billingProgress() {
		Thread th = new Thread(()->{
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
			if(Platform.isFxApplicationThread())
				setByBillResponse(billResponse);
			else
				Platform.runLater(()->setByBillResponse(billResponse));
		});
		th.setDaemon(true);
		th.start();
	}
	
	private void pay() {
		Order ord = Context.order;
		//if not all the price paid with refund
		if(ord!=null && 
				(ord.getPaymentMethod() == null || 
				ord.getPaymentMethod() != null && 
				ord.getPaymentMethod().equals(PayMethod.Refund)==false)) {
			if(tGroup.getSelectedToggle().equals(rbCredit)) {
				//if part of the price removed because refund, and the rest paid with Credit Card
				if(refund_before_disc>0)
					Context.order.setPaymentMethod(PayMethod.RefundAndCreditCard);
				else
					ord.setPaymentMethod(PayMethod.CreditCard);
			}
			else if(tGroup.getSelectedToggle().equals(rbCash)) {
				//if part of the price removed because refund, and the rest paid with Cash
				if(refund_before_disc>0)
					Context.order.setPaymentMethod(PayMethod.RefundAndCash);
				else
					ord.setPaymentMethod(PayMethod.Cash);
			}
		}
		//Won't be null if it's payment for manual order
		if(ord.getOrderStatus().equals(OrderStatus.InProcess))
			ord.setOrderStatus(OrderStatus.Paid);
		if(txtGreeting.getText().isEmpty()==false)
			ord.setGreeting(txtGreeting.getText());
		else
			ord.setGreeting("");
		if(refund_before_disc!=0f) {
			pa.setRefundAmount(refund_after_disc);
			ord.setFinalPrice(price_after_disc);
		}
		else
			ord.setFinalPrice(price_before_disc);
		try {
			Context.fac.paymentAccount.update(pa);
			
			Context.mainScene.setMenuPaneDisable(true);
			btnBack.setDisable(true);
			Context.fac.order.add(ord,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setOrderID(BigInteger id) {
		Context.mainScene.setMenuPaneDisable(false);
		btnBack.setDisable(false);
		Context.mainScene.loadOrderDetails();
	}

	public void back() {
		Context.order.setFinalPrice(price_before_disc);	//prevent double discount
		if(is_manual_order)
			Context.mainScene.loadManualTransaction();
		else
			Context.mainScene.loadOrderTime();
	}
}