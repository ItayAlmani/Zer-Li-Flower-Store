package entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class CreditCard implements Serializable {

	private BigInteger ccID;
	private String ccNumber;
	private Date ccValidity;
	private String ccCVV;

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
}