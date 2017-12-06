package Controllers;

import java.util.ArrayList;

import Entity.Product;
import Server.DataBase;

public class ProductController {
	private DataBase db;
	
	public ProductController(DataBase db) {
		super();
		this.db = db;
	}
}
