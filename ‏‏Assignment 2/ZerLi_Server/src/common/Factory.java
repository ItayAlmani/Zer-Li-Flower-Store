package common;
import controllers.*;
import izhar.*;

/**
 * Will have all the controllers.
 * Will manage them by being static, but them will be non-static classes
 * @author izhar
 *
 */
public class Factory {	
	public DataBaseController dataBase = new DataBaseController();
	public SurveyController survey = new SurveyController();
	public SurveyReportController surveyReport = new SurveyReportController();
	public PickupController pickup = new PickupController();
	public BrowsingProcessHandler browsingProcess = new BrowsingProcessHandler();
	public OrderController order = new OrderController();
	public ProductInOrderController prodInOrder = new ProductInOrderController();
	public ShipmentController shipment = new ShipmentController();
	public ProductController product = new ProductController();
	
	/*public ComplaintController complaint = new ComplaintController();
	
	public StockController stock = new StockController();
	
	
	public OrderProcessHandler orderProcess = new OrderProcessHandler();
	public StoreController store = new StoreController();
	
	
	public UserController user = new UserController();
	public CustomerController customer = new CustomerController();
	public StoreWorkerController storeWorker = new StoreWorkerController();
	
	public OrderReportController orderReport = new OrderReportController();
	public IncomesReportController incomesReport = new IncomesReportController();*/
	
	/*public AdminController admin = new AdminController();
	public PaymantAccountController paymentAccount = new PaymantAccountController();
	
	public HistogramOfCustomerComplaintsReportController histogramReport = new HistogramOfCustomerComplaintsReportController();
	
	
	public SatisfactionReportController satisfactionReport = new SatisfactionReportController();*/
}
