package entities;
public class PaymentAccount {

	private int paID;
	private int customerID;
	private float refundAmount = 0;
	private CreditCard creditCard;
	private Subscription sub;
	
	/*public enum PaymentAccountType {
		Subscribed,
		NonSubscribed
	}*/

	public PaymentAccount(int paID) {
		super();
		this.paID = paID;
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