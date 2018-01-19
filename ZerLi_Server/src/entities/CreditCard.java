package entities;

import java.io.Serializable;
import java.math.BigInteger;

public class CreditCard implements Serializable {
	
	private static final long serialVersionUID = 2L;
	private BigInteger ccID;
	private String ccNumber;
	private String ccValidity;
	private String ccCVV;

	public CreditCard(BigInteger ccID,String ccNumber, String ccValidity, String ccCVV) {
		this.ccID = ccID;
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

	public String getCcValidity() {
		return ccValidity;
	}

	public void setCcValidity(String ccValidity) {
		this.ccValidity = ccValidity;
	}

	public String getCcCVV() {
		return ccCVV;
	}

	public void setCcCVV(String ccCVV) {
		this.ccCVV = ccCVV;
	}
}