package actors;

import java.util.Date;

public class CreditCard {
	protected String ccNumber;
	protected Date ccValidity;
	protected String ccCVV;
	
	public CreditCard(String ccNumber, Date ccValidity, String ccCVV) {
		super();
		this.ccNumber = ccNumber;
		this.ccValidity = ccValidity;
		this.ccCVV = ccCVV;
	}
}
