package controllers;

import java.util.ArrayList;

import common.Context;
import gui.controllers.ParentGUIController;
import javafx.fxml.Initializable;

public abstract class ParentController {
	protected static ArrayList<Object> myMsgArr = new ArrayList<>();
	
	public static void sendResultToClient(boolean response) {
		if(response==true)
			Context.mainScene.ShowSuccessMsg();
		else
			Context.mainScene.ShowErrorMsg();
	}
}
