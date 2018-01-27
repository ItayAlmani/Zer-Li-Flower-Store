package izhar;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import interfaces.IProductController;
import orderNproducts.entities.Product;

public class ProductController extends ParentController implements IProductController {	
	
	/* (non-Javadoc)
	 * @see izhar.IProductController#parse(java.math.BigInteger, java.lang.String, java.lang.String, float, java.lang.String, boolean, java.lang.String, byte[])
	 */
	@Override
	public Product parse(BigInteger prdID,
			String name,
			String type,
			float price, String color, boolean inCatalog, String imageURL,
			byte[] byteArr) throws Exception{
		if(byteArr.length == 0) {
			Product p = new Product(prdID, name, type,price,color,inCatalog, imageURL, insertImageToByteArr(imageURL));
			try {
				update(p);
				return p;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		else
			return new Product(prdID, name, type,price,color,inCatalog, imageURL, byteArr);
	}
	
	private byte[] insertImageToByteArr(String fileName) {
		File newFile = null;
		boolean useInStream = true;
		try {
			newFile = new File(getClass().getResource("/images/"+fileName).getPath());
		}catch (NullPointerException e) {
			try {
				newFile = new File(System.getProperty("user.dir")+"//images//"+fileName);
				useInStream = false;
			}catch (NullPointerException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		try {
			byte[] mybytearray  = new byte [(int)newFile.length()];
			BufferedInputStream bis = null;
			FileInputStream fis = null;
			if(useInStream==true)
				bis = new BufferedInputStream(getClass().getResourceAsStream("/images/"+fileName));
			else {
				fis = new FileInputStream(newFile);
				bis = new BufferedInputStream(fis);
			}
			if(bis != null) {
				bis.read(mybytearray,0,mybytearray.length);
				bis.close();
				if(fis!=null)
					fis.close();
			}
			return mybytearray;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see izhar.IProductController#handleGet(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> handleGet(ArrayList<Object> obj) throws Exception {
		if(obj==null) return null;
		ArrayList<Object> prds = new ArrayList<>();
		for (int i = 0; i < obj.size(); i += 8)
				prds.add(parse(
						BigInteger.valueOf((Integer) obj.get(i)), 
						(String) obj.get(i + 1), 
						(String) obj.get(i + 2),
						(float) obj.get(i + 3),
						(String) obj.get(i + 4),
						((int)obj.get(i + 5))!= 0,
						(String)obj.get(i+6),
						(byte[])obj.get(i+7)
						));
		return prds;
	}

	/* (non-Javadoc)
	 * @see izhar.IProductController#add(java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> add(ArrayList<Object> arr) throws Exception {
		if(arr!=null && (arr.get(0) instanceof Product == false) || arr.get(1) instanceof Boolean == false)
			throw new Exception();
		Product p = (Product)arr.get(0);
		boolean isReturnNextID = (boolean)arr.get(1);

		PreparedStatement pst;
		String query="INSERT INTO product (productName, productType, price, color, inCatalog, image, imageBlob)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
		pst=EchoServer.fac.dataBase.db.con.prepareStatement(query);

		pst.setString(1, p.getName());
		pst.setString(2, p.getType().name());
		pst.setFloat(3, p.getPrice());
		pst.setString(4, p.getColor().name());
		pst.setInt(5, p.isInCatalog()?1:0);
		pst.setString(6, p.getImageName());
		ByteArrayInputStream bais = new ByteArrayInputStream(p.getMybytearray());
		pst.setBinaryStream(7, bais, p.getMybytearray().length);
		bais.close();
		pst.executeUpdate();
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

	/* (non-Javadoc)
	 * @see izhar.IProductController#getProductByID(java.math.BigInteger)
	 */
	@Override
	public ArrayList<Object> getProductByID(BigInteger prdID) throws Exception {
		String query = "SELECT * FROM product WHERE productID = '"+prdID+"';";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductController#getAllProducts()
	 */
	@Override
	public ArrayList<Object> getAllProducts() throws Exception {
		String query = "SELECT * FROM product";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductController#getProductsInCatalog()
	 */
	@Override
	public ArrayList<Object> getProductsInCatalog() throws Exception{
		String query = "SELECT * FROM product WHERE inCatalog='1'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}

	/* (non-Javadoc)
	 * @see izhar.IProductController#getAllProductsNotInCatalog()
	 */
	@Override
	public ArrayList<Object> getAllProductsNotInCatalog() throws Exception{
		String query = "SELECT * FROM product WHERE inCatalog='0'";
		return handleGet(EchoServer.fac.dataBase.db.getQuery(query));
	}
	
	/* (non-Javadoc)
	 * @see izhar.IProductController#update(java.lang.Object)
	 */
	@Override
	public ArrayList<Object> update(Object obj) throws Exception {
		if(obj instanceof Product) {
			Product p = (Product)obj;
			PreparedStatement pst;
			String query="UPDATE product"
					+ " SET productName=?,"
					+ " productType=?,"
					+ " price=?,"
					+ " color=?,"
					+ " inCatalog=?,"
					+ " image=?,"
					+ " imageBlob=?"
					+ " WHERE productID=?";
			pst=EchoServer.fac.dataBase.db.con.prepareStatement(query);

			pst.setString(1, p.getName());
			pst.setString(2, p.getType().name());
			pst.setFloat(3, p.getPrice());
			pst.setString(4, p.getColor().name());
			pst.setInt(5, p.isInCatalog()?1:0);
			pst.setString(6, p.getImageName());
			//byte[] barr=insertImageToByteArr(p.getImageName());
			ByteArrayInputStream bais = new ByteArrayInputStream(p.getMybytearray());
			pst.setBinaryStream(7, bais, p.getMybytearray().length);
			bais.close();
			pst.setInt(8, p.getPrdID().intValue());
			pst.executeUpdate();
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		throw new Exception();
	}
}
