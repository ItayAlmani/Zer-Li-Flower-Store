package controllers;

import java.util.ArrayList;

import common.DataBase;
import entities.Product;

public class ProductController {
	private DataBase db;
	
	public ProductController(DataBase db) {
		super();
		this.db = db;
	}
}
