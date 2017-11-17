package actors;

public class Item {
	private String	itID;
	private String	itName;
	private String	itType;
	private float	itPrice;
	
	/**Will be used by new ImageIcon(getClass().getResource(iImg))**/
	private String	itImg;
	
	/** If the item is on sale - true, else - false **/
	private Boolean itSaleStatus;	
	
	/** The price before the sale **/
	private float	itPriceBS;		
	
	public Item(String itID, String itName, String itType, float itPrice, String itImg) {
		super();
		this.itID = itID;
		this.itName = itName;
		this.itType = itType;
		this.itPrice = itPrice;
		this.itImg = itImg;
	}
	
	
/*----------------------------------Getters&Setters----------------------------------*/
	public String getID() {
		return itID;
	}

	public void setID(String itID) {
		this.itID = itID;
	}

	public String getName() {
		return itName;
	}

	public void setName(String itName) {
		this.itName = itName;
	}

	public float getPrice() {
		return itPrice;
	}

	public void setPrice(float itPrice) {
		this.itPrice = itPrice;
	}

	public String getImage() {
		return itImg;
	}

	public void setImage(String itImg) {
		this.itImg = itImg;
	}

	public float getPriceBeforeSale() {
		return itPriceBS;
	}

	public void setPriceBeforeSale(float itPriceBS) {
		this.itPriceBS = itPriceBS;
	}

	public String getType() {
		return itType;
	}
/*---------------------------------------------------------------------------------------*/
	
	/**
	 * Put the Item on sale, and the sale itPrice is newPrice
	 * @param newPrice - The sale itPrice of the Item
	 */
	public void setSaleOn(float newPrice) {
		this.itSaleStatus = true;
		this.itPriceBS = this.itPrice;
		this.itPrice=newPrice;
	}
	
	/**
	 * Put the Item on sale, and the sale itPrice is itPrice*(priceDiscount/100)
	 * @param priceDiscount - The amount of discount in percentage
	 */
	public void setSaleOn(int priceDiscount) {
		this.itSaleStatus = true;
		this.itPriceBS = this.itPrice;
		this.itPrice=this.itPrice*(priceDiscount/100);
	}
	
	/**
	 * Will take off the Item from the sale and restore the original price
	 * @return false - Item is not on sale,
	 * true - Item's price was restored, and the Item is not on sale
	 */
	public Boolean setSaleOff() {
		if(this.itSaleStatus==false)
			return false;
		this.itSaleStatus=false;
		this.itPrice=this.itPriceBS;
		this.itPriceBS = -1;
		return true;
	}
	
	/** 
	 * Will take off the Item from the sale and change itPrice to newPrice
	 * @param newPrice - The new price of the Item
	 * @return false - Item is not on sale,
	 * true - Item's price was changed, and the Item is not on sale
	 */
	public Boolean setSaleOff(float newPrice) {
		if(this.itSaleStatus==false)
			return false;
		this.itSaleStatus=false;
		this.itPrice=newPrice;
		this.itPriceBS = -1;
		return true;
	}
}
