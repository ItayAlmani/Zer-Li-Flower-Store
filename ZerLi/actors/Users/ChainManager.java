package actors.Users;

public class ChainManager extends ChainWorker {

	public ChainManager(String uFName, String uLName, String uUName, String uPassword) {
		super(uFName, uLName, uUName, uPassword);
		this.userType = UserType.ChainManager;
	}
}