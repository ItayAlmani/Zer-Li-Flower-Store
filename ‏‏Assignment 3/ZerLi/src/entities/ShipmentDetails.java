package entities;
public class ShipmentDetails extends DeliveryDetails {

	private string street;
	private string city;
	private string postCode;
	private string customerName;
	private string phoneNumber;

	public void setStreet(string street) {
		this.street = street;
	}

	public void setCity(string city) {
		this.city = city;
	}

	public void setPostCode(string postCode) {
		this.postCode = postCode;
	}

	public void setCustomerName(string customerName) {
		this.customerName = customerName;
	}

	public void setPhoneNumber(string phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 
	 * @param address
	 * @param cusName
	 * @param pNum
	 */
	public ShipmentDetails(string[] address, string cusName, string pNum) {
		// TODO - implement ShipmentDetails.ShipmentDetails
		throw new UnsupportedOperationException();
	}

}