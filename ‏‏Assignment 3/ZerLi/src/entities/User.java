package entities;
public class User {

	private string privateID;
	private string firstName;
	private string lastName;
	private string userName;
	private string password;
	private UserType permissions;

	public UserType getPermissions() {
		return this.permissions;
	}

	public void setPermissions(UserType permissions) {
		this.permissions = permissions;
	}

}