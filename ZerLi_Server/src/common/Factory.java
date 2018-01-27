package common;
import customersSatisfaction.ComplaintController;
import customersSatisfaction.SurveyController;
import customersSatisfaction.SurveyReportController;
import orderNproducts.OrderController;
import orderNproducts.PickupController;
import orderNproducts.ProductController;
import orderNproducts.ProductInOrderController;
import orderNproducts.ShipmentController;
import orderNproducts.StockController;
import orderNproducts.StoreController;
import quarterlyReports.HistogramOfCustomerComplaintsReportController;
import quarterlyReports.IncomesReportController;
import quarterlyReports.OrderReportController;
import quarterlyReports.QuarterlyReportController;
import quarterlyReports.SatisfactionReportController;
import usersInfo.CreditCardController;
import usersInfo.CustomerController;
import usersInfo.PaymentAccountController;
import usersInfo.StoreWorkerController;
import usersInfo.SubscriptionController;
import usersInfo.UserController;

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
