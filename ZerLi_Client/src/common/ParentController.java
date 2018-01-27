package common;

import java.util.ArrayList;

public abstract class ParentController {
	protected static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	/**
	 * shows Success or Error message by {@code response}
	 * @param response if true, shows Success message, else Error message
	 */
	public static void sendResultToClient(boolean response) {
		if(response==true)
			Context.mainScene.ShowSuccessMsg();
		else
			Context.mainScene.ShowErrorMsg();
	}
}
