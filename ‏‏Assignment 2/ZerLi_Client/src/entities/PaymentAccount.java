package entities;
public class PaymentAccount {

	private String paID;
	private String customerID;
	private float refundAmount = 0;
	private CreditCard creditCard;
	private Subscription sub;
	
	private static Integer idCounter = 1;
	public PaymentAccount() {
		this.paID = idCounter.toString();
		idCounter++;
	}

	public float getRefundAmount() {
		return this.refundAmount;
	}

	public void setRefundAmount(float refundAmount) {
		this.refundAmount = refundAmount;
	}

	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Subscription getSub() {
		return this.sub;
	}

	public void setSub(Subscription sub) {
		this.sub = sub;
	}

}