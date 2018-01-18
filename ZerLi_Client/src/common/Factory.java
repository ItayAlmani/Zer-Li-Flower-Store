package common;
import controllers.DataBaseController;
import itayNron.*;
import itayNron.interfaces.IStock;
import izhar.*;
import izhar.interfaces.IProduct;
import lior.*;
import kfir.*;
import kfir.interfaces.ICustomer;

/**
 * Will have all the controllers.
 * Will manage them by being static, but them will be non-static classes
 * @author izhar
 *
 */
public class Factory {	
	public DataBaseController dataBase = new DataBaseController();
	
	public ComplaintController complaint = new ComplaintController();
	public SurveyController survey = new SurveyController();
	public SurveyReportController surveyReport = new SurveyReportController();
	public StockController stock = new StockController();
	
	public OrderController order = new OrderController();
	public PickupController pickup = new PickupController();
	public StoreController store = new StoreController();
	public ProductController product = new ProductController();
	public ProductInOrderController prodInOrder = new ProductInOrderController();
	public ShipmentController shipment = new ShipmentController();
	
	public UserController user = new UserController();
	public CustomerController customer = new CustomerController();
	public StoreWorkerController storeWorker = new StoreWorkerController();
	
	public OrderReportController orderReport = new OrderReportController();
	public IncomesReportController incomesReport = new IncomesReportController();
	public HistogramOfCustomerComplaintsReportController histogramReport = new HistogramOfCustomerComplaintsReportController();
	public SatisfactionReportController satisfactionReport = new SatisfactionReportController();
	
	public PaymentAccountController paymentAccount = new PaymentAccountController();
	public CreditCardController creditCard = new CreditCardController();
	public SubscriptionController sub = new SubscriptionController();
}
