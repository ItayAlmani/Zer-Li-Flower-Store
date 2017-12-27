package common;
import itayNron.*;
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
	public ComplaintController complaint = new ComplaintController();
	public SurveyController survey = new SurveyController();
	public SurveyReportController surveyReport = new SurveyReportController();
	
	public BrowsingProcessHandler browsingProcess = new BrowsingProcessHandler();
	public OrderController order = new OrderController();
	public OrderProcessHandler orderProcess = new OrderProcessHandler();
	public PickupController pickup = new PickupController();
	public StoreController store = new StoreController();
	public TransactionController transaction = new TransactionController();
	public ProductController product = new ProductController();
	public ProductInOrderController prodInOrder = new ProductInOrderController();
	
	public UserController user = new UserController();
	public CustomerController customer = new CustomerController();
	
	public OrderReportController orderReport = new OrderReportController();
	/*public AdminController admin = new AdminController();
	public PaymantAccountController paymentAccount = new PaymantAccountController();
	public ServiceExpertController serviceExpert = new ServiceExpertController();
	public StoreManagerController storeManager = new StoreManagerController();
	
	public HistogramOfCustomerComplaintsReportController histogramReport = new HistogramOfCustomerComplaintsReportController();
	public IncomesReportController incomesReport = new IncomesReportController();
	
	public SatisfactionReportController satisfactionReport = new SatisfactionReportController();*/
}
