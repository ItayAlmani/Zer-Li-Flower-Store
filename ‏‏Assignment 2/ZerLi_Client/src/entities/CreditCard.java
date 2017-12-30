package entities;

import java.math.BigInteger;
import java.util.Date;

public class CreditCard {

	private BigInteger ccID;
	private String ccNumber;
	private Date ccValidity;
	private String ccCVV;
	
	private static BigInteger idInc = null;

	public CreditCard(String ccNumber, Date ccValidity, String ccCVV) {
		super();
		this.ccNumber = ccNumber;
		this.ccValidity = ccValidity;
		this.ccCVV = ccCVV;
	}

	public BigInteger getCcID() {
		return ccID;
	}

	public void setCcID(BigInteger ccID) {
		this.ccID = ccID;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	public Date getCcValidity() {
		return ccValidity;
	}

	public void setCcValidity(Date ccValidity) {
		this.ccValidity = ccValidity;
	}

	public String getCcCVV() {
		return ccCVV;
	}

	public void setCcCVV(String ccCVV) {
		this.ccCVV = ccCVV;
	}

	public static BigInteger getIdInc() {
		return idInc;
	}

	public static void setIdInc(BigInteger idInc) {
		CreditCard.idInc = idInc;
	}
}