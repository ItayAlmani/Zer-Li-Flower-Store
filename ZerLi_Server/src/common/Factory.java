package common;
import itayNron.ComplaintController;
import itayNron.StockController;
import itayNron.StoreController;
import itayNron.SurveyController;
import itayNron.SurveyReportController;
import izhar.IOrder;
import izhar.IPickupController;
import izhar.IProductController;
import izhar.IProductInOrderController;
import izhar.IShipmentController;
import izhar.OrderController;
import izhar.PickupController;
import izhar.ProductController;
import izhar.ProductInOrderController;
import izhar.ShipmentController;
import kfir.CreditCardController;
import kfir.CustomerController;
import kfir.PaymentAccountController;
import kfir.StoreWorkerController;
import kfir.SubscriptionController;
import kfir.UserController;
import lior.HistogramOfCustomerComplaintsReportController;
import lior.IncomesReportController;
import lior.OrderReportController;
import lior.QuarterlyReportController;
import lior.SatisfactionReportController;
import lior.interfaces.IQuarterlyReportController;

/**
 * Will have all the controllers.
 * Will manage them by being static, but them will be non-static classes
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
	
	public QuarterlyReportController qurReport = new QuarterlyReportController();
	public OrderReportController ordReport = new OrderReportController();
	public IncomesReportController incReport = new IncomesReportController();
	public HistogramOfCustomerComplaintsReportController hisReport = new HistogramOfCustomerComplaintsReportController();
	public SatisfactionReportController satReport = new SatisfactionReportController();
	
	public void setComplaintController() {
		complaint = new ComplaintController();
	}
}
