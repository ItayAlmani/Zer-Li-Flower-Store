package common;
import controllers.DataBaseController;
import itayNron.*;
import lior.*;
import orderNproducts.controllers.OrderController;
import orderNproducts.controllers.ProductController;
import orderNproducts.controllers.ProductInOrderController;
import orderNproducts.controllers.StockController;
import orderNproducts.controllers.StoreController;
import orderNproducts.interfaces.IProduct;
import orderNproducts.interfaces.IProductInOrder;
import orderNproducts.interfaces.IStock;
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
	public StoreController store = new StoreController();
	public ProductController product = new ProductController();
	public IProductInOrder prodInOrder = new ProductInOrderController();
	
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
