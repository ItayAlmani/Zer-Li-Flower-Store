package actors.Users;

public class StoreManager extends StoreWorker {

	public StoreManager(String uFName, String uLName, String uUName, String uPassword) {
		super(uFName, uLName, uUName, uPassword);
		this.userType=UserType.StoreManager;
	}

}
