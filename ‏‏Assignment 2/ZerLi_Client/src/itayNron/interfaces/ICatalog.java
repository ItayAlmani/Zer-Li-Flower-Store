package itayNron.interfaces;

import java.util.ArrayList;

import entities.Product;
import interfaces.IParent;

public interface ICatalog extends IParent{

	void getProductsInCatalog();

	void sendProductsInCatalog(ArrayList<Product> products);

}