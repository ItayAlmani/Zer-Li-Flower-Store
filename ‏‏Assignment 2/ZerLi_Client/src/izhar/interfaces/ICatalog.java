package izhar.interfaces;

import java.io.IOException;
import java.util.ArrayList;

import entities.Product;
import interfaces.IParent;

public interface ICatalog extends IParent{

	void getProductsInCatalog() throws IOException;

	void sendProductsInCatalog(ArrayList<Product> products);

}