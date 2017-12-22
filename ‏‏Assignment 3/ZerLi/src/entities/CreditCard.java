package entities;

import java.util.Date;

public class CreditCard {

	private String ccID;
	private String ccNumber;
	private Date ccValidity;
	private String ccCCV;

	private static Integer idCounter = 1;
	public CreditCard() {
		this.ccID = idCounter.toString();
		idCounter++;
	}
}