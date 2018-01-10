package izhar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.Product;

public class ProductController extends ParentController {	
	
	public Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL){
		return new Product(prdID, name, type,price,color,inCatalog, imageURL, insertImageToByteArr("/images/"+imageURL));
	}
	
	private byte[] insertImageToByteArr(String filePath) {
		try {
			File newFile = new File(getClass().getResource(filePath).toURI());
		      
			byte[] mybytearray  = new byte [(int)newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);			    
			bis.read(mybytearray,0,mybytearray.length);
			bis.close();
			return mybytearray;
		}catch (Exception e) {
			System.err.println(filePath+"\n");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) {
		if(obj==null) return null;
		ArrayList<Object> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 7)
				prds.add(parse(
						BigInteger.valueOf(Long.valueOf((int) obj.get(i))), 
						(String) obj.get(i + 1), 
						(String) obj.get(i + 2),
						(float) obj.get(i + 3),
						(String) obj.get(i + 4),
						((int)obj.get(i + 5))!= 0,
						(String)obj.get(i+6))
						);
		return prds;
	}

	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Product == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Product p = (Product)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);
		String res = "0";
		if(p.isInCatalog())
			res="1";
		String query = "INSERT INTO orders (productName, productType, price, color, inCatalog)"
				+ "VALUES ('" + p.getName() + "', '" 
				+ p.getType().toString() + "', '"
				+ p.getPrice() + "', '"
				+ p.getColor().toString() + "', '"
				+ res + "')";
		EchoServer.fac.dataBase.db.updateQuery(query);
		if(isReturnNextID) {
			query="SELECT Max(productID) from product";
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

	public ArrayList<Object> getProductByID(BigInteger prdID) throws SQLException {
		String query = "SELECT * FROM product WHERE productID = '"+prdID+"';";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getAllProducts() throws SQLException {
		String query = "SELECT * FROM product";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	public ArrayList<Object> getProductsInCatalog() throws SQLException{
		String query = "SELECT * FROM product WHERE inCatalog='1'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	public ArrayList<Object> getAllProductsNotInCatalog() throws SQLException{
		String query = "SELECT * FROM product WHERE inCatalog='0'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
