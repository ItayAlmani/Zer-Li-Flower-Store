package izhar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import common.EchoServer;
import controllers.ParentController;
import entities.Product;

public class ProductController extends ParentController {	
	
	public Product parse(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog, String imageURL){
		return new Product(prdID, name, type,price,color,inCatalog, imageURL, insertImageToByteArr(imageURL));
	}
	
	private byte[] insertImageToByteArr(String fileName) {
		File newFile = null;
		try {
			newFile = new File(getClass().getResource("/images/"+fileName).getPath());
		}catch (NullPointerException e) {
			try {
				newFile = new File(System.getProperty("user.dir")+"\\images\\"+fileName);
			}catch (NullPointerException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		try {
			byte[] mybytearray  = new byte [(int)newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);			    
		
			bis.read(mybytearray,0,mybytearray.length);
			bis.close();
			System.out.println("The array size is "+mybytearray.length);
			return mybytearray;
		} catch (IOException e) {
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
		saveImageToProjectDir(p);
		String res = "0";
		if(p.isInCatalog())
			res="1";
		String query = "INSERT INTO product (productName, productType, price, color, inCatalog, image)"
				+ " VALUES ('" + p.getName() + "', '" 
				+ p.getType().name() + "', '"
				+ p.getPrice() + "', '"
				+ p.getColor().name() + "', '"
				+ res + "', '"
				+ p.getImageName() + "')";
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
		if(obj instanceof Product) {
			Product p = (Product)obj;
			saveImageToProjectDir(p);
			String query = String.format("UPDATE product"
					+ " SET productName='%s',"
					+ " productType='%s',"
					+ " price='%f',"
					+ " color='%s',"
					+ " inCatalog='%s',"
					+ " image='%s'"
					+ " WHERE productID='%d'", p.getName(),p.getType().name(),p.getPrice(),p.getColor().name(),
					p.isInCatalog()?"1":"0",p.getImageName(),p.getPrdID().intValue());
			EchoServer.fac.dataBase.db.updateQuery(query);
			myMsgArr.clear();
			myMsgArr.add(true);
			return myMsgArr;
		}
		throw new Exception();
	}
	
	/**
	 * create an folder called "images" (if not exists) to the project
	 * directory, and saves there the picture.
	 * @param p the product which includes all the details of the new picture.
	 * @throws IOException
	 * <ul>
	 * 		<li>{@link FileOutputStream#FileOutputStream(String)} - file not exists at the path</li>
	 * 		<li>{@link BufferedOutputStream#write(byte[])}</li>
	 * 		<li>{@link BufferedOutputStream#close()}</li>
	 * </ul>
	 */
	private void saveImageToProjectDir(Product p) throws IOException  {
		File dir = new File(System.getProperty("user.dir")+"\\images\\");
		if(dir.exists()==false)
			dir.mkdir();
		File f = new File(dir,p.getImageName());
		FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(p.getMybytearray());
		bos.close();
	}
}
