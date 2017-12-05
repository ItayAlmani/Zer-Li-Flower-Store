package CS;

import java.io.IOException;
import java.util.ArrayList;

import Controllers.Factory;
import Entity.Product;
import client.MainClient;
import common.ProductType;
import gui.ProductViewGUIController;
import gui.ProductsFormGUIController;
import gui.TemplateGUI;

public class ClientController {
	public ProductsFormGUIController gui;
	
	
	public ClientController(ProductsFormGUIController gui) {
		super();
		this.gui = gui;
	}

	public void askProductsFromServer() throws IOException {
		MainClient.cc.handleMessageFromClientUI("SELECT * FROM product;", this, this.gui);
		//ArrayList<Product> x = EchoServer.db.getAllProducts();
		//System.out.println(x.get(0));
	}
	
	public Product parsingTheData(int id, String name, String type) {
		Product p = new Product(id, name);
		switch (type.toLowerCase()) {
		case "bouqute":
			p.setType(ProductType.Bouqute);
			break;
		default:
			p.setType(ProductType.Empty);
			break;
		}
		return p;
	}

}
