package common;
import customersSatisfaction.ComplaintController;
import customersSatisfaction.SurveyController;
import customersSatisfaction.SurveyReportController;
import orderNproducts.OrderController;
import orderNproducts.ProductController;
import orderNproducts.ProductInOrderController;
import orderNproducts.StockController;
import orderNproducts.StoreController;
import orderNproducts.interfaces.IProductInOrder;
import quarterlyReports.HistogramOfCustomerComplaintsReportController;
import quarterlyReports.IncomesReportController;
import quarterlyReports.OrderReportController;
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
	public StoreController store = new StoreController();
	public ProductController product = new ProductController();
	public ProductInOrderController prodInOrder = new ProductInOrderController();
	
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
