package Controllers;

import java.util.ArrayList;

import Entity.Product;
import jdbc.DataBase;

public class ProductController {
	private DataBase db;
	
	public ProductController(DataBase db) {
		super();
		this.db = db;
	}

	public ArrayList<Product> getAllProductsFromDB(){
		return db.getAllProducts();
	}
}
