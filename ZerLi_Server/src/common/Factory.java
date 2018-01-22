package common;
import controllers.*;
import itayNron.*;
import izhar.*;
import kfir.*;

/**
 * Will have all the controllers.
 * Will manage them by being static, but them will be non-static classes
 * @author izhar
 *
 */
public class Factory {	
	public DataBaseController dataBase = new DataBaseController();

	public PickupController pickup = new PickupController();
	public OrderController order = new OrderController();
	public ProductInOrderController prodInOrder = new ProductInOrderController();
	public ShipmentController shipment = new ShipmentController();
	public ProductController product = new ProductController();
	
	public StockController stock = new StockController();
	public StoreController store = new StoreController();
	public SurveyController survey = new SurveyController();
	public SurveyReportController surveyReport = new SurveyReportController();
	
	public StoreWorkerController storeWorker = new StoreWorkerController();
	public UserController user = new UserController();
	public PaymentAccountController paymentAccount = new PaymentAccountController();
	public CreditCardController creditCard = new CreditCardController();
	public ComplaintController complaint;	
	public CustomerController customer = new CustomerController();
	public SubscriptionController sub = new SubscriptionController();
	
	public void setComplaintController() {
		complaint = new ComplaintController();
	}
}
