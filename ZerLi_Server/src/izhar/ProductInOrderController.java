package izhar;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.Product;
import entities.ProductInOrder;

public class ProductInOrderController extends ParentController implements IProductInOrderController{	
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#add(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof ProductInOrder == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		ProductInOrder p = (ProductInOrder)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String query = "INSERT INTO cart (orderID, productID, quantity, totalprice) " + 
				" VALUES ('" 
				+ p.getOrderID()+ "', '"
				+ p.getProduct().getPrdID() + "', '"
				+ p.getQuantity() + "', '"
				+ p.getFinalPrice() + "');";
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		if(isReturnNextID) {
			query="SELECT Max(productInOrderID) from cart";
			myMsgArr =  EchoServer.fac.dataBase.db.getQuery(query);
			if(myMsgArr!=null && myMsgArr.size()==1 && myMsgArr.get(0) instanceof Integer) {
				myMsgArr.set(0, BigInteger.valueOf((Integer)myMsgArr.get(0)));
			}
			else throw new Exception();
		}
		else
			myMsgArr.add(true);
		return myMsgArr;
	}

	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#getPIOsByOrder(java.math.BigInteger)
	 */
	@Override
	public ArrayList<Object> getPIOsByOrder(BigInteger orderID) throws Exception {
		String query = "SELECT prodInOrd.productInOrderID, prd.*,orderID, quantity, totalprice FROM" + 
				" (" + 
				"	SELECT ordCart.* FROM" + 
				"	(" + 
				"		SELECT crt.*" + 
				"		FROM cart AS crt" + 
				"		JOIN orders ON crt.orderID=orders.orderID" + 
				"		where crt.orderID = '"+orderID+"'" + 
				"	) AS ordCart" + 
				"	JOIN product ON ordCart.productID=product.productID" + 
				" ) AS prodInOrd, product prd" + 
				" WHERE prodInOrd.productID = prd.productID"
				;
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#update(java.lang.Object)
	 */
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof ProductInOrder) {
			ProductInOrder p = (ProductInOrder)obj;
			String query = String.format("UPDATE cart " + 
					" SET orderID=%d, productID=%d,quantity=%d,totalprice=%f" + 
					" WHERE productInOrderID=%d",
					p.getOrderID(),p.getProduct().getPrdID(),p.getQuantity(),p.getFinalPrice(),p.getId());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		throw new Exception();
	}

	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#updatePriceOfPIO(entities.ProductInOrder)
	 */
	@Override
	public ArrayList<Object> updatePriceOfPIO(ProductInOrder p) throws SQLException{
		String query = "update cart" + 
				" set quantity='"+p.getQuantity()+
				"', totalprice='"+p.getFinalPrice()+"'" + 
				" where orderID='"+p.getOrderID()+"' and productID='"+p.getProduct().getPrdID()+"'";
		EchoServer.fac.dataBase.db.updateQuery(query);
		myMsgArr.clear();
		myMsgArr.add(true);
		return myMsgArr;
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#isAllPIOsFromSameOrder(java.util.ArrayList)
	 */
	@Override
	public boolean isAllPIOsFromSameOrder(ArrayList<ProductInOrder> products) {
		BigInteger ordID = products.get(0).getOrderID();
		for (ProductInOrder pio : products) {
			if(pio.getOrderID().equals(ordID)==false)
				return false;
		}
		return true;
	}

	
	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#handleGet(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj==null) return null;
		ArrayList<Object> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 12) {
			prds.add(parse(BigInteger.valueOf((Integer)obj.get(i)),
					EchoServer.fac.product.parse(
						BigInteger.valueOf((Integer) obj.get(i+1)), 
						(String) obj.get(i + 2), 
						(String) obj.get(i + 3),
						(float) obj.get(i + 4),
						(String) obj.get(i + 5),
						((int)obj.get(i + 6))!= 0,
						(String)obj.get(i+7),
						(byte[])obj.get(i+8)
					)
					,BigInteger.valueOf((Integer)obj.get(i+9))
					,(int)obj.get(i+10),
					(float)obj.get(i+11)
					));
		}
		return prds;
	}

	/* (non-Javadoc)
	 * @see izhar.IProductInOrderController#parse(java.math.BigInteger, entities.Product, java.math.BigInteger, int, float)
	 */
	@Override
	public ProductInOrder parse(BigInteger id, Product prod, BigInteger orderID, int quantity, float finalPrice) {
		return new ProductInOrder(id, prod,orderID,quantity,finalPrice);
	}
}
