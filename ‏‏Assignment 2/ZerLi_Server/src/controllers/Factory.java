package controllers;

import common.DataBase;

public class Factory {
	public static DataBase db;
	public static ProductController pc= new ProductController(db);
	
	public static void setDataBase(DataBase setDB) {
		db=setDB;
	}
}
