package Entity;

public class Product{ 
	private int id;
	private String name;
	private ProductType type;
	
	public Product(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Product(int id, String name, ProductType type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public int getId() {
		return id;
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
}
