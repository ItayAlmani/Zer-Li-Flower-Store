package actors;
import java.util.Date;
import java.util.ArrayList;

public class Order {
	private int ordID;
	private ArrayList<Item> ordItems;
	private Date ordDate;
	
	/** Made of the sum of all the Items's prices	 */
	private float ordFinalPrice;
	
	/** Optional	 */
	private String ordGreeting;
	
	public void addItemToCart(Item item) {
		this.ordItems.add(item);	//Maybe we should check if already exist there
		this.ordFinalPrice+=item.getPrice();
	}
}