package entities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

import izhar.ProductController;

public class Product implements Comparable<Product>, Serializable  {
	private BigInteger prdID;
	private String name;
	private ProductType type;
	private float price;
	private Color color;
	private boolean inCatalog;
	private String imageName;
	private byte[] mybytearray = null;

	public enum ProductType {
		Bouquet("Bouquet"),
		Single("Single"), 
		Empty("Empty"),
		FlowerArrangment("Flower Arrangment"),
		FloweringPlant("Flowering Plant"), 
		BridalBouquet("Bridal Bouquet"),
		FlowersCluster("Flowers Cluster");
		private final String name;
		
		private ProductType(String name) {
	        this.name = name;
	    }
		
		@Override
		public String toString() {
			return name;
		}
	}

	public enum Color {
		White, Yellow, Red, Gray, Purple, Blue, Green, Black, Orange, Brown, Pink, Colorfull;
	}

	public Product() {
	}

	public Product(BigInteger prdID, String name) {
		super();
		this.prdID = prdID;
		this.name = name;
	}

	public Product(BigInteger prdID, String name, ProductType type) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.type = type;
	}

	public Product(BigInteger prdID, String name, ProductType type, float price, Color color, boolean inCatalog) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.type = type;
		this.price = price;
		this.color = color;
		this.inCatalog = inCatalog;
	}
	
	public Product(Product p) {
		this.prdID = p.prdID;
		this.name = p.name;
		this.type = p.type;
		this.price = p.price;
		this.color = p.color;
		this.inCatalog = p.inCatalog;
		this.imageName = p.imageName;
		this.mybytearray = p.mybytearray;
	}

	public Product(BigInteger prdID, String name, String type) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.setType(type);
	}

	public Product(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog) {
		super();
		this.prdID = prdID;
		this.name = name;
		this.setType(type);
		this.price = price;
		this.setColor(color);
		this.inCatalog = inCatalog;
	}

	public Product(BigInteger prdID, String name, String type, float price, String color, boolean inCatalog,
			String imageName, byte[] mybytearray) {
		this.prdID = prdID;
		this.name = name;
		this.setType(type);
		this.price = price;
		this.setColor(color);
		this.inCatalog = inCatalog;
		this.imageName = imageName;
		this.mybytearray=mybytearray;
	}

	public BigInteger getPrdID() {
		return prdID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public void setType(String type) {
		try {
			this.setType(ProductType.valueOf(type));
		} catch (IllegalArgumentException e) {
			System.err.println("No enum constant of type as " + type);
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setColor(String color) {
		try {
			this.setColor(Color.valueOf(color));
		} catch (IllegalArgumentException e) {
			System.err.println("No enum constant of color as " + color);
		}
	}

	public void setPrdID(BigInteger prdID) {
		this.prdID = prdID;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isInCatalog() {
		return inCatalog;
	}

	public void setInCatalog(boolean inCatalog) {
		this.inCatalog = inCatalog;
	}

	public String getImageName() throws URISyntaxException, IOException {
		URI uri = ProductController.class.getResource("/images/").toURI();
		File f = new File(new URI(uri.toString()+this.imageName));
		f.createNewFile();
		f.deleteOnExit();
		FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
		setImageName(f.getName());
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		bos.write(getMybytearray());
		bos.close();
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(Product o) {
		return getPrdID().compareTo(o.getPrdID());
	}
	
	public String getPriceAsString() {
		return ((Float)getPrice()).toString() + "¤";
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}

	public void setMybytearray(byte[] mybytearray) {
		this.mybytearray = mybytearray;
	}
}
