package actors.Users;

public abstract class User {
	private static int 	idIndex = 1;		//Every new user will get the current value, and afterwards will increase by 1
	protected int		userID;				//Increasing by 1 for each new user
	private String		userFName;
	private String		userLName;
	private String		userUName;
	private String		userPassword;
	private Boolean		userIs_Connected;	//On success login - true, on success log out - false
	protected UserType	userType;			//Each userserType has it's own permissions
	
	public User(UserType userType) {
		this.userType = userType;
		this.userID=User.idIndex;
		User.idIndex++;
	}
	
	public User(String userFName, String userLName, String userUName, String userPassword, UserType userType) {
		this(userType);
		this.userFName = userFName;
		this.userLName = userLName;
		this.userUName = userUName;
		this.userPassword = userPassword;
		this.userIs_Connected=false;
	}	
}
