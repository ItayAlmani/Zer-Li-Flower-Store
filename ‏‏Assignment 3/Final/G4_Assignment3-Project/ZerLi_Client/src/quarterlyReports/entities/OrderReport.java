package quarterlyReports.entities;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;

import orderNproducts.entities.Product.ProductType;

public class OrderReport extends QuarterlyReport {

	private static final long serialVersionUID = 10L;
	
	private HashMap<ProductType, Integer> counterPerType = new HashMap<>();
	private HashMap<ProductType, Float> sumPerType = new HashMap<>();
	
	
	public OrderReport(LocalDate endOfQuarterDate, BigInteger storeID) {
		super(endOfQuarterDate, storeID);
	}

	public void setCounterPerType(HashMap<ProductType, Integer> counterPerType) {
		this.counterPerType = counterPerType;
	}
	
	public void setSumPerType(HashMap<ProductType, Float> sumPerType) {
		this.sumPerType = sumPerType;
	}

	public HashMap<ProductType, Integer> getCounterPerType() {
		return counterPerType;
	}

	public HashMap<ProductType, Float> getSumPerType() {
		return sumPerType;
	}
	
	public void addToCounterPerType(ProductType type,Integer cnt) {
		this.counterPerType.put(type, cnt);
	}

	public void addToSumPerType(ProductType type,Float sum) {
		this.sumPerType.put(type, sum);
	}
}