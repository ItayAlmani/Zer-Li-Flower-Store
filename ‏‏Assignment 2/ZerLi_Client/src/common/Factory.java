package common;
import controllers.ProductController;
import itayNron.*;
import izhar.*;
import lior.*;
import kfir.*;

/**
 * Will have all the controllers.
 * Will manage them by being static, but them will be non-static classes
 * @author izhar
 *
 */
public class Factory {
	/*public CatalogController catalog = new CatalogController();
	public ComplaintController complaint = new ComplaintController();
	public ItemController item = new ItemController();
	public SurveyController survey = new SurveyController();
	public SurveyReportController surveyReport = new SurveyReportController();
	
	public BrowsingProcessHandler browsingProcess = new BrowsingProcessHandler();
	public OrderController order = new OrderController();
	public OrderProcessHandler orderProcess = new OrderProcessHandler();
	public PickupController pickup = new PickupController();
	public ShoppingCartController cart = new ShoppingCartController();
	public StoreController store = new StoreController();
	public TransactionController transaction = new TransactionController();
	
	public AdminController admin = new AdminController();
	public CustomerController customer = new CustomerController();
	public PaymantAccountController paymentAccount = new PaymantAccountController();
	public ServiceExpertController serviceExpert = new ServiceExpertController();
	public StoreManagerController storeManager = new StoreManagerController();
	public UserController user = new UserController();
	
	public HistogramOfCustomerComplaintsReportController histogramReport = new HistogramOfCustomerComplaintsReportController();
	public IncomesReportController incomesReport = new IncomesReportController();
	public OrderReportController orderReport = new OrderReportController();
	public SatisfactionReportController satisfactionReport = new SatisfactionReportController();*/
	
	public ProductController product = new ProductController();
}
